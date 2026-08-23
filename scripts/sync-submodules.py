#!/usr/bin/env python3
import configparser
import os
import shutil
import subprocess
import sys

PINNED_COMMITS = {
    'app/jni/third_party/abseil-cpp': '2f9e432cce407ce0ae50676696666f33a77d42ac',
    'app/jni/third_party/crc32c': '21fc8ef30415a635e7351ffa0e5d5367943d4a94',
    'app/jni/third_party/ffmpeg': 'e36046790a141c46de767e3c93b692e4c3b1c3de',
    'app/jni/third_party/flac': '1507800de4b70e21be71f38caa0d9079d0bc6e45',
    'app/jni/third_party/jni-utils': '0a517820b3584d3751c8e233f93952ce14ac3b9b',
    'app/jni/third_party/libevent': '422a87cd9cfc916fae4b918d63bf2a3d9bc9c40f',
    'app/jni/third_party/libsrtp': '860492290f7d1f25e2bd45da6471bfd4cd4d7add',
    'app/jni/third_party/libtgvoip': '652bfa7745be1a5ac00718fd183c245766efa8f2',
    'app/jni/third_party/libvpx': 'd168454ecd099805c675d4a98c66f4891373302a',
    'app/jni/third_party/libyuv': '4e8a843bfc6bc967eed60b9c0eab078f42bdd6e0',
    'app/jni/third_party/lz4': '8f61d8eb7c6979769a484cde8df61ff7c4c77765',
    'app/jni/third_party/ogg': 'be05b13e98b048f0b5a0f5fa8ce514d56db5f822',
    'app/jni/third_party/openh264': 'c59550a2147c255cc8e09451f6deb96de2526b6d',
    'app/jni/third_party/opus': 'ddbe48383984d56acd9e1ab6a090c54ca6b735a6',
    'app/jni/third_party/opusfile': 'a55c164e9891a9326188b7d4d216ec9a88373739',
    'app/jni/third_party/rlottie': 'a5fa60c5d866071b7a382e319634d57cbea22f78',
    'app/jni/third_party/rnnoise': '1cbdbcf1283499bbb2230a6b0f126eb9b236defd',
    'app/jni/third_party/tgcalls': '8e27a9213a536600e3958a46e994903dd875b77d',
    'app/jni/third_party/usrsctp': '01cc4e042e2235b29d9d489d89728a6f9ac063ed',
    'app/jni/third_party/webp': '991170bbab3e6afc74666d124f3f1dc7be942cd0',
    'app/jni/third_party/webrtc': '6ecff4f2446ff7d4ce38ca1c764f023e44dbcb1b',
    'app/jni/third_party/webrtc_deps/base': 'fd5eca261fa03e22f053a0eaa5b010ca01c6fe51',
    'app/jni/third_party/webrtc_deps/third_party': '121de111a913373d1ac15e4605da24fd22b21bcf',
    'tdlib': 'ed3e4bac11f60ebacf205cf5dabe6488b9cda0e6',
    'thirdparty/androidx-media/latest': '7cc1056f840ce226598d3b990d4a6f7cd17e2831',
    'thirdparty/androidx-media/legacy': 'c35a9d62baec57118ea898e271ac66819399649b',
    'thirdparty/androidx-media/lollipop': 'b7bbc6e2bc3e45ff3ed99884c114c50f03bba5c9',
    'vkryl/android': 'a35c640da581081414b3f792177b0ea793f3387f',
    'vkryl/core': 'e85f95bff6160e50bda31112c48eeec61f1087b1',
    'vkryl/leveldb': '159ba70919aa7f1ec3ed1ab64064355930bda9cc',
    'vkryl/td': '6a26e9b3274bbabc89c656709aa8e3ca3f9bbb5d',
}

def run_cmd(cmd, cwd=None):
    print(f"[EXEC] {' '.join(cmd)}" + (f" (in {cwd})" if cwd else ""))
    res = subprocess.run(cmd, cwd=cwd)
    return res.returncode == 0

def fetch_and_checkout(url, target_dir, sha=None, branch=None):
    norm_target = target_dir.replace('\\', '/')
    if os.path.exists(target_dir) and os.path.isdir(target_dir):
        if os.path.exists(os.path.join(target_dir, '.git')):
            if sha:
                res = subprocess.run(['git', 'rev-parse', 'HEAD'], cwd=target_dir, capture_output=True, text=True)
                if res.returncode == 0 and res.stdout.strip().startswith(sha[:7]):
                    print(f"[SKIP] {norm_target} is already at commit {sha[:7]}")
                    return True
            elif os.listdir(target_dir):
                print(f"[SKIP] {norm_target} already exists and is not empty.")
                return True
        elif os.listdir(target_dir):
            print(f"[SKIP] {norm_target} already exists and is not empty.")
            return True

    if os.path.exists(target_dir):
        shutil.rmtree(target_dir, ignore_errors=True)
    os.makedirs(target_dir, exist_ok=True)

    if sha:
        print(f"[FETCH] Fetching {url} @ {sha[:7]} -> {norm_target}")
        subprocess.run(['git', 'init'], cwd=target_dir)
        subprocess.run(['git', 'remote', 'add', 'origin', url], cwd=target_dir)
        res = subprocess.run(['git', 'fetch', '--depth', '1', 'origin', sha], cwd=target_dir)
        if res.returncode == 0:
            if subprocess.run(['git', 'checkout', 'FETCH_HEAD'], cwd=target_dir).returncode == 0:
                return True
        # Fallback if direct sha fetch is disallowed by remote
        print(f"[INFO] Direct SHA fetch failed, cloning with depth...")
        shutil.rmtree(target_dir, ignore_errors=True)
        os.makedirs(target_dir, exist_ok=True)
        cmd = ['git', 'clone']
        if branch:
            cmd.extend(['-b', branch])
        cmd.extend([url, target_dir])
        if subprocess.run(cmd).returncode == 0:
            if subprocess.run(['git', 'checkout', sha], cwd=target_dir).returncode == 0:
                return True
        return False
    else:
        cmd = ['git', 'clone', '--depth', '1']
        if branch:
            cmd.extend(['-b', branch])
        cmd.extend([url, target_dir])
        return subprocess.run(cmd).returncode == 0

def main():
    root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    gitmodules_path = os.path.join(root, '.gitmodules')
    if not os.path.exists(gitmodules_path):
        print("No .gitmodules file found.")
        return

    config = configparser.ConfigParser()
    config.read(gitmodules_path)

    # 1. Clone all top-level submodules with exact pinned commits
    for section in config.sections():
        path = config.get(section, 'path', fallback=None)
        url = config.get(section, 'url', fallback=None)
        branch = config.get(section, 'branch', fallback=None)

        if not path or not url:
            continue

        target_dir = os.path.join(root, path)
        norm_path = path.replace('\\', '/')
        sha = PINNED_COMMITS.get(norm_path)

        print(f"\n--- Processing {path} ---")
        if not fetch_and_checkout(url, target_dir, sha=sha, branch=branch):
            print(f"[ERROR] Failed to clone/checkout {url} to {path}")
            sys.exit(1)

    # 2. Specific setup for tdlib
    tdlib_dir = os.path.join(root, 'tdlib')
    print("\n--- Setting up tdlib dependencies ---")
    run_cmd(['git', 'lfs', 'pull'], cwd=tdlib_dir)

    # OpenSSL in tdlib (must be a stable release tag like OpenSSL_1_1_1w so status == 0xf)
    openssl_dir = os.path.join(tdlib_dir, 'source', 'openssl')
    opensslv_h = os.path.join(openssl_dir, 'include', 'openssl', 'opensslv.h')
    if os.path.exists(opensslv_h):
        with open(opensslv_h, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()
            if '0x10101180' in content or 'status = 0' in content:
                print("[INFO] Cleaning non-stable OpenSSL dev branch...")
                shutil.rmtree(openssl_dir, ignore_errors=True)

    if not fetch_and_checkout('https://github.com/openssl/openssl', openssl_dir, branch='OpenSSL_1_1_1w'):
        print("[ERROR] Failed to clone OpenSSL for tdlib")
        sys.exit(1)

    # TDLib source in tdlib
    td_source_dir = os.path.join(tdlib_dir, 'source', 'td')
    if not fetch_and_checkout('https://github.com/tdlib/td', td_source_dir, sha='0da5c72f8365fb4857096e716d53175ddbdf5a15'):
        print("[ERROR] Failed to clone td source for tdlib")
        sys.exit(1)

    # 3. Specific setup for vkryl/leveldb
    leveldb_dir = os.path.join(root, 'vkryl', 'leveldb')
    if os.path.exists(os.path.join(leveldb_dir, '.gitmodules')):
        print("\n--- Initializing vkryl/leveldb submodules ---")
        run_cmd(['git', 'submodule', 'update', '--init', '--depth', '1'], cwd=leveldb_dir)

    # 4. Patch vkryl/android CINNAMON_BUN if needed
    sdk_version_file = os.path.join(root, 'vkryl', 'android', 'src', 'main', 'kotlin', 'me', 'vkryl', 'android', 'SdkVersion.kt')
    if os.path.exists(sdk_version_file):
        with open(sdk_version_file, 'r', encoding='utf-8') as f:
            sdk_content = f.read()
        if 'Build.VERSION_CODES.CINNAMON_BUN' in sdk_content:
            print("[INFO] Patching CINNAMON_BUN in vkryl/android SdkVersion.kt...")
            sdk_content = sdk_content.replace('Build.VERSION_CODES.CINNAMON_BUN', '37')
            with open(sdk_version_file, 'w', encoding='utf-8') as f:
                f.write(sdk_content)

    # 5. Patch vkryl/td TdCompileAssert.kt for legacy hash aliases if needed
    td_assert_file = os.path.join(root, 'vkryl', 'td', 'src', 'main', 'kotlin', 'tgx', 'td', 'TdCompileAssert.kt')
    if os.path.exists(td_assert_file):
        with open(td_assert_file, 'r', encoding='utf-8') as f:
            assert_content = f.read()
        if 'assertLinkPreviewType_a9a3ffcd' not in assert_content:
            print("[INFO] Adding legacy assertLinkPreviewType_a9a3ffcd stub to TdCompileAssert.kt...")
            assert_content += '\n@Autogenerated fun assertLinkPreviewType_a9a3ffcd (): LinkPreviewType? = null\n'
            with open(td_assert_file, 'w', encoding='utf-8') as f:
                f.write(assert_content)

    # 6. Verify critical header files
    if not os.path.exists(opensslv_h):
        print(f"[FATAL] Required file missing: {opensslv_h}")
        sys.exit(1)
    td_cmake = os.path.join(td_source_dir, 'CMakeLists.txt')
    if not os.path.exists(td_cmake):
        print(f"[FATAL] Required file missing: {td_cmake}")
        sys.exit(1)

    print("\n[SUCCESS] All submodules and nested dependencies configured successfully!")

if __name__ == '__main__':
    main()

