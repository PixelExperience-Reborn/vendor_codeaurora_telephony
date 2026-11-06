# Qualcomm / CodeLinaro first — telephony source selection (P2 row 163)

## Policy for PE-Reborn Android 17 (mondrian only)

1. **Prefer Qualcomm's official `clo/la/platform/vendor/codeaurora/telephony` source**, using a specific compatible Android 17 source revision (SHA), if an appropriate public source ref exists and its modules match our future device ABI.
2. The current LineageOS `lineage-24.0` telephony sources serve as a valuable *adaptation/compatibility reference*, not the default upstream by decree. The root `.gitupstream` of the legacy PE repository also identifies this same Qualcomm CodeLinaro project.
3. Keep the old PixelExperience history intact; do not reactivate `seventeen` (Android14 era), and do not introduce any second telephony source owner into the staged AOSP17 ROM manifest without satisfying compatibility gates.
4. **A Qualcomm QSSI17 platform release does not automatically prove that this specific vendor telephony project exposes the same tag, supports the old SM8450/WAIPIO proprietary IMS stack or is binary-compatible with existing Xiaomi APK/JARs.**

## Verified starting points

- PE `seventeen` SHA: `cdfe819ba8d596f435417931463d769a8e59f2da` (2024-01-08, Motorola IMS VT API fix), tree `6cb2a34ada97b95ecec2ecaf868c9eb2df070b59`.
- PE root `.gitupstream`: `https://git.codelinaro.org/clo/la/platform/vendor/codeaurora/telephony`.
- Its snapshot has 175 source and resource blobs in `extphone/`, `ims/`, `internal/`, `qti-hidl-wrapper/`, `qti-utils/`.
- Read-only LineageOS telephony `lineage-24.0` SHA `5a048ec75771d4cc7becce6ab71c06de55f7a374`, tree `3ca07b8e4cf6d6c0f6d8fabd1d20ac9de41069fb`: 188 blobs, 25 new, 26 changed and 12 deleted compared with the 2024 PE file inventory; historical author dates on tip commit are not proof that the tree is stale, since it also contains 2026 upstream merges.
- The LineageOS reference `ims/ims-ext-common/Android.bp` sets `min_sdk_version: "36"`. Some legacy Qualcomm Java HIDL dependencies (e.g. `android.hidl.base-V1.0-java`) remain, so Android17 Soong API availability must be demonstrated, not presumed.
- Existing **read-only** `LF52406/mondrian_android_device_xiaomi_sm8450-common` `lineage-24.0` `common.mk` lists `extphonelib`, `ims-ext-common`, `qti-telephony-hidl-wrapper`, `qti-telephony-utils`, `telephony-ext` and `qcrilNrDb_vendor`. These are only reference requirements pending an *independent PE-Reborn device/common tree*.
- Existing read-only vendor blobs include `ims.apk`, `QtiTelephonyService.apk`, `qcrilNrd`, IMS daemon(s), QTI permissions/uses-libraries and `qtiradio-saidl.xml`. Proprietary vendor services are **not replaced** by this open-source Java API project.

## Direct Qualcomm Android17 source now pinned and imported in isolation

The official CodeLinaro Git server was reached from GitHub Actions; the exact Qualcomm telephony **project** (not just a platform release announcement) advertises Android17 tags and branch refs. See [official discovery CI #42300338502](https://github.com/PixelExperience-Reborn/vendor_codeaurora_telephony/actions/runs/42300338502).

- Chosen verified tag: `LA.QSSI.17.0.r1-12400-qssi.0` (official tag object `c3872c75d407dfbf05e332a1b6c0c0924906edf9`).
- Git tag **peeled source commit**: `7c646f8194d7915073701168b7c0ffd559f32773`.
- Exact official Qualcomm root tree: `358d126b6232e39c7bc961b8810df207afee9d12`.
- Note that the source commit itself has a 2026-06-17 author/commit date; the `LA.QSSI.17.0.r1-12400-qssi.0` release tag published in November 2026 points to that commit. **Do not confuse tag date and last underlying source commit date.**
- The pinned Qualcomm tree includes **187 files** (the archived PE has 175 blobs); `extphonelib`, `ims-ext-common`, `qti-telephony-hidl-wrapper`, `qti-telephony-utils`, and the `IExtPhone.aidl` / `IQtiImsExt.aidl` interfaces were independently verified in [CodeLinaro tag CI #42300587067](https://github.com/PixelExperience-Reborn/vendor_codeaurora_telephony/actions/runs/42300587067). It retains `android.hardware.radio-V1.6-java`, `android.hidl.base-V1.0-java` and sets `min_sdk_version: "36"` for `ims-ext-common`. **These remain unresolved Android17 Soong/compatibility constraints**, not automatically accepted device APIs.

A genuine **two-parent Git source import** was prepared, tested and published to [`integrate/a17-qti-telephony-source-20261106`](https://github.com/PixelExperience-Reborn/vendor_codeaurora_telephony/tree/integrate/a17-qti-telephony-source-20261106):

- Integration merge SHA: `58543d6299a7c99eac59eda56774372bd3c4e618`.
- First parent: original PE `cdfe819ba8d596f435417931463d769a8e59f2da`.
- Second parent: official Qualcomm `7c646f8194d7915073701168b7c0ffd559f32773`.
- Resulting root Git tree is **exactly `358d126b6232e39c7bc961b8810df207afee9d12`**, so no LineageOS code or custom unreviewed patch has been silently inserted.
- [Source-only two-parent import CI #42300836797 SUCCESS](https://github.com/PixelExperience-Reborn/vendor_codeaurora_telephony/actions/runs/42300836797) verifies provenance, Soong/AIDL shape, and the existing staged AOSP17 graph. GitHub API confirmed the published commit/tree and both parents. Old `seventeen` HEAD is unchanged.
- `PixelExperience-Reborn/manifest` validator now explicitly rejects premature `vendor/codeaurora/telephony` import and includes a negative regression test. The active `default.xml` is intentionally unchanged, pending real Xiaomi proprietary IMS and Android17 compilation tests.

**Only the direct Qualcomm source is imported. LineageOS 24.0 remains a reference for adaptations and vendor-tested interfaces, not the source baseline.**

## Direct Qualcomm discovery gate

The separate `.github/workflows/a17-qti-codeaurora-source-discovery.yml` checks the actual official CodeLinaro Git remote, lists *real available* QSSI Android17 / WAIPIO-related refs if any, and reports whether a candidate can be pinned. It is an **audit/discovery workflow only**: no source import and no automatic selection of a moving Git branch. If CodeLinaro cannot be reached or no matching ref exists, CI must record the blocker rather than manufacture a version.

After selecting a candidate, compare its complete `Android.bp`/AIDL/uses-library graph against Android17 Soong, **and** against the actual vendor-required IMS/ExtPhone APIs. Preserve only justified LineageOS fixes, Xiaomi-specific needs and relevant PE history. Treat the AIDL and Java method signatures as ABI/API; superficial compilation is insufficient.

## Conditions before enabling in the ROM

- Proven and pinned official CodeLinaro SHA/tag, license provenance, Android17 source/APIs with supported Java HIDL/AIDL dependency graph.
- Verified coexistence with framework `telephony-ext`, `frameworks/opt/telephony`, `hardware/ril` and the vendor IMS APK/JAR and QTI manifests; no duplicate Java modules or permissions libraries.
- Independent PE-Reborn **mondrian-only** device/common/vendor implementation, checked against the modern LF52406 read-only reference, not a wholesale LineageOS tree copy.
- Soong/Make build, system_ext/product permissions, VINTF/SELinux policy, IMS registration, LTE/NR, VoLTE/VoWiFi, SIM/eSIM if applicable, data and emergency calls tested on hardware.

**Current status**: official Qualcomm Android17 telephony source selected, pinned and merged in an isolated two-parent PE-Reborn branch. Full vendor IMS ABI, Soong build, calls, VoLTE/VoWiFi and hardware compatibility **still OPEN**. No telephony source added to Android17 `default.xml`. Existing LF52406 trees untouched.

Links: [Qualcomm source](https://git.codelinaro.org/clo/la/platform/vendor/codeaurora/telephony), [PE source](https://github.com/PixelExperience-Reborn/vendor_codeaurora_telephony), [modern comparison source](https://github.com/LineageOS/android_vendor_codeaurora_telephony/tree/lineage-24.0), [PE AOSP17 matrix](https://github.com/PixelExperience-Reborn/manifest/blob/port/a17-aosp17-manifest-20261102/docs/ANDROID_17_135_REPOSITORY_PORT_MATRIX.md).
