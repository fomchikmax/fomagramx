#!/usr/bin/env python3
import configparser
import os
import shutil
import subprocess
import sys

def main():
    root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    gitmodules_path = os.path.join(root, '.gitmodules')
    if not os.path.exists(gitmodules_path):
        print("No .gitmodules file found.")
        return

    config = configparser.ConfigParser()
    config.read(gitmodules_path)

    for section in config.sections():
        path = config.get(section, 'path', fallback=None)
        url = config.get(section, 'url', fallback=None)
        branch = config.get(section, 'branch', fallback=None)

        if not path or not url:
            continue

        target_dir = os.path.join(root, path)
        if os.path.exists(target_dir):
            if os.path.isdir(target_dir) and os.listdir(target_dir):
                print(f"[SKIP] {path} already exists and is not empty.")
                continue
            else:
                if os.path.isdir(target_dir):
                    shutil.rmtree(target_dir, ignore_errors=True)
                else:
                    os.remove(target_dir)

        os.makedirs(os.path.dirname(target_dir), exist_ok=True)
        print(f"[CLONE] {url} -> {path} (branch: {branch})...")

        cmd = ['git', 'clone', '--depth', '1', '--recursive']
        if branch:
            cmd.extend(['-b', branch])
        cmd.extend([url, target_dir])

        res = subprocess.run(cmd)
        if res.returncode != 0:
            if branch:
                print(f"[WARN] Failed with branch '{branch}', retrying default branch for {url}...")
                shutil.rmtree(target_dir, ignore_errors=True)
                fallback_cmd = ['git', 'clone', '--depth', '1', '--recursive', url, target_dir]
                subprocess.run(fallback_cmd, check=True)
            else:
                print(f"[ERROR] Failed to clone {url}")
                sys.exit(1)

    print("All submodules successfully cloned and initialized!")

if __name__ == '__main__':
    main()
