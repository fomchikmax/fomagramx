#!/usr/bin/env python3
import configparser
import os
import shutil
import subprocess
import sys

def run_cmd(cmd, cwd=None):
    print(f"[EXEC] {' '.join(cmd)}" + (f" (in {cwd})" if cwd else ""))
    res = subprocess.run(cmd, cwd=cwd)
    return res.returncode == 0

def clone_repo(url, target_dir, branch=None):
    if os.path.exists(target_dir):
        if os.path.isdir(target_dir) and os.listdir(target_dir):
            print(f"[SKIP] {target_dir} already exists and is not empty.")
            return True
        else:
            if os.path.isdir(target_dir):
                shutil.rmtree(target_dir, ignore_errors=True)
            else:
                os.remove(target_dir)

    os.makedirs(os.path.dirname(target_dir), exist_ok=True)
    cmd = ['git', 'clone', '--depth', '1']
    if branch:
        cmd.extend(['-b', branch])
    cmd.extend([url, target_dir])

    if not run_cmd(cmd):
        if branch:
            print(f"[WARN] Failed with branch '{branch}', retrying default branch...")
            shutil.rmtree(target_dir, ignore_errors=True)
            cmd = ['git', 'clone', '--depth', '1', url, target_dir]
            if not run_cmd(cmd):
                return False
        else:
            return False
    return True

def main():
    root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    gitmodules_path = os.path.join(root, '.gitmodules')
    if not os.path.exists(gitmodules_path):
        print("No .gitmodules file found.")
        return

    config = configparser.ConfigParser()
    config.read(gitmodules_path)

    # 1. Clone all top-level submodules from .gitmodules
    for section in config.sections():
        path = config.get(section, 'path', fallback=None)
        url = config.get(section, 'url', fallback=None)
        branch = config.get(section, 'branch', fallback=None)

        if not path or not url:
            continue

        target_dir = os.path.join(root, path)
        print(f"\n--- Processing {path} ---")
        if not clone_repo(url, target_dir, branch):
            print(f"[ERROR] Failed to clone {url} to {path}")
            sys.exit(1)

    # 2. Specific setup for tdlib
    tdlib_dir = os.path.join(root, 'tdlib')
    print("\n--- Setting up tdlib dependencies ---")
    run_cmd(['git', 'lfs', 'pull'], cwd=tdlib_dir)

    # OpenSSL in tdlib
    openssl_dir = os.path.join(tdlib_dir, 'source', 'openssl')
    if not clone_repo('https://github.com/openssl/openssl', openssl_dir, 'OpenSSL_1_1_1-stable'):
        print("[ERROR] Failed to clone OpenSSL for tdlib")
        sys.exit(1)

    # TDLib source in tdlib
    td_source_dir = os.path.join(tdlib_dir, 'source', 'td')
    if not clone_repo('https://github.com/tdlib/td', td_source_dir, 'master'):
        print("[ERROR] Failed to clone td source for tdlib")
        sys.exit(1)

    # 3. Specific setup for vkryl/leveldb
    leveldb_dir = os.path.join(root, 'vkryl', 'leveldb')
    if os.path.exists(os.path.join(leveldb_dir, '.gitmodules')):
        print("\n--- Initializing vkryl/leveldb submodules ---")
        run_cmd(['git', 'submodule', 'update', '--init', '--depth', '1'], cwd=leveldb_dir)

    # 4. Verify critical header files
    opensslv_h = os.path.join(openssl_dir, 'include', 'openssl', 'opensslv.h')
    td_cmake = os.path.join(td_source_dir, 'CMakeLists.txt')

    if not os.path.exists(opensslv_h):
        print(f"[FATAL] Required file missing: {opensslv_h}")
        sys.exit(1)
    if not os.path.exists(td_cmake):
        print(f"[FATAL] Required file missing: {td_cmake}")
        sys.exit(1)

    print("\n[SUCCESS] All submodules and nested dependencies configured successfully!")

if __name__ == '__main__':
    main()
