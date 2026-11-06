#!/usr/bin/env bash
# SOURCE-ONLY official Qualcomm telephony integration for PE-Reborn.
# Do NOT add this tree to Android17 default.xml or change device trees.
set -euo pipefail

TARGET=${1:?usage: import_official_qti.sh NEW_DIRECTORY}
PE_URL=https://github.com/PixelExperience-Reborn/vendor_codeaurora_telephony.git
PE_BRANCH=integrate/a17-qti-telephony-source-20261106
PE_BASE=cdfe819ba8d596f435417931463d769a8e59f2da
CLO_URL=https://git.codelinaro.org/clo/la/platform/vendor/codeaurora/telephony
CLO_TAG=LA.QSSI.17.0.r1-12400-qssi.0
CLO_TAG_OBJECT=c3872c75d407dfbf05e332a1b6c0c0924906edf9
CLO_COMMIT=7c646f8194d7915073701168b7c0ffd559f32773
CLO_TREE=358d126b6232e39c7bc961b8810df207afee9d12

[[ ! -e "$TARGET" ]] || { echo "FAIL: target directory already exists" >&2; exit 1; }
git clone -q --filter=blob:none --branch "$PE_BRANCH" "$PE_URL" "$TARGET"
head=$(git -C "$TARGET" rev-parse HEAD)
if [[ "$head" != "$PE_BASE" ]]; then
    # Once the exact source import is published, reruns must verify it
    # without modifying any already-reviewed Git branch.
    REVIEWED_MERGE=58543d6299a7c99eac59eda56774372bd3c4e618
    parents=$(git -C "$TARGET" rev-list --parents -n 1 HEAD)
    actual_tree=$(git -C "$TARGET" rev-parse HEAD^{tree})
    if [[ "$head" != "$REVIEWED_MERGE" || "$actual_tree" != "$CLO_TREE" ||
          "$parents" != "$head $PE_BASE $CLO_COMMIT" ]]; then
        echo "FAIL: PE integration branch moved without review: $head" >&2
        exit 1
    fi
    echo "PASS: existing direct Qualcomm import $head; verified two parents and exact tree"
    exit 0
fi

ref=$(timeout 80 git ls-remote --tags --refs "$CLO_URL" "refs/tags/$CLO_TAG" | cut -f1)
[[ "$ref" == "$CLO_TAG_OBJECT" ]] || { echo "FAIL: unexpected official CodeLinaro tag ref: $ref" >&2; exit 1; }

# The SECOND parent must have its full reachable history for a GitHub push,
# not only a shallow source tree object.
git -C "$TARGET" fetch -q --no-tags "$CLO_URL" "refs/tags/$CLO_TAG"
commit=$(git -C "$TARGET" rev-parse 'FETCH_HEAD^{commit}')
tree=$(git -C "$TARGET" rev-parse 'FETCH_HEAD^{tree}')
[[ "$commit" == "$CLO_COMMIT" && "$tree" == "$CLO_TREE" ]] || {
    echo "FAIL: official Qualcomm commit/tree identity mismatch: $commit $tree" >&2
    exit 1
}

git -C "$TARGET" config user.name "PixelExperience-Reborn source port"
git -C "$TARGET" config user.email "noreply@users.noreply.github.com"

git -C "$TARGET" merge --allow-unrelated-histories -q --no-ff --no-commit -s ours "$commit"
git -C "$TARGET" ls-files -z | xargs -0 -r git -C "$TARGET" rm -q --
git -C "$TARGET" checkout -q "$commit" -- .

actual=$(git -C "$TARGET" write-tree)
[[ "$actual" == "$CLO_TREE" ]] || { echo "FAIL: imported tree differs from Qualcomm $actual" >&2; exit 1; }

python3 - "$TARGET" <<'PY'
from pathlib import Path
import subprocess
import sys
tree=Path(sys.argv[1])
def get(f):
    return (tree / f).read_text(encoding="utf-8")
expected={
    "extphone/Android.bp": "extphonelib",
    "ims/ims-ext-common/Android.bp": "ims-ext-common",
    "qti-hidl-wrapper/Android.bp": "qti-telephony-hidl-wrapper",
    "qti-utils/Android.bp": "qti-telephony-utils",
}
for path,name in expected.items():
    assert f'name: "{name}"' in get(path), f"missing Qualcomm Soong library {path}"
assert "android.hardware.radio-V1.6-java" in get("extphone/Android.bp")
assert "android.hidl.base-V1.0-java" in get("qti-hidl-wrapper/Android.bp")
assert 'min_sdk_version: "36"' in get("ims/ims-ext-common/Android.bp")
assert (tree / "extphone/src/com/qti/extphone/IExtPhone.aidl").is_file()
assert (tree / "ims/ims-ext-common/src/org/codeaurora/ims/internal/IQtiImsExt.aidl").is_file()
assert not (tree/"ims/ConfURIDialer").exists()
assert not (tree/".github/workflows").exists(), "unreviewed upstream CI workflow"
files=subprocess.check_output(
    ["git","-C",str(tree),"ls-files"],text=True).splitlines()
assert len(files) == 187, f"unexpected Qualcomm 17.0 source blob count {len(files)}"
print("PASS: exact 187-file Qualcomm Android17 telephony library/AIDL tree")
print("OPEN: HIDL/radio Java Soong modules, Xiaomi IMS proprietary and ABI runtime")
PY

git -C "$TARGET" -c commit.gpgsign=false commit -qm \
    "telephony: import official Qualcomm QSSI17 source with PE history

Use exact tag $CLO_TAG, official peeled SHA $CLO_COMMIT.
Import exact official root tree $CLO_TREE without changing its files.
Preserve PE $PE_BASE as first Git parent and official Qualcomm
as second parent. This is a SOURCE-ONLY isolated integration branch.

No AOSP17 manifest import, no mondrian PE device tree, no claim of
compatibility with proprietary Xiaomi IMS/RIL or working telephony."

parents=$(git -C "$TARGET" rev-list --parents -n 1 HEAD)
read -r sha p1 p2 extra <<< "$parents"
[[ -z "${extra:-}" && "$p1" == "$PE_BASE" && "$p2" == "$CLO_COMMIT" ]] || {
    echo "FAIL: unexpected Git parentage: $parents" >&2; exit 1;
}
[[ "$(git -C "$TARGET" rev-parse HEAD^{tree})" == "$CLO_TREE" ]] || {
    echo "FAIL: signed source snapshot tree mismatch" >&2; exit 1;
}

echo "PASS: original PixelExperience first parent $p1"
echo "PASS: official Qualcomm CodeLinaro second parent $p2"
echo "PASS: exact pinned Qualcomm Android17 tree $CLO_TREE"
echo "PASS: source-only local integration candidate $sha"
