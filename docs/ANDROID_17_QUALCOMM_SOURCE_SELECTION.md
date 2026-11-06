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

## Direct Qualcomm discovery gate

The separate `.github/workflows/a17-qti-codeaurora-source-discovery.yml` checks the actual official CodeLinaro Git remote, lists *real available* QSSI Android17 / WAIPIO-related refs if any, and reports whether a candidate can be pinned. It is an **audit/discovery workflow only**: no source import and no automatic selection of a moving Git branch. If CodeLinaro cannot be reached or no matching ref exists, CI must record the blocker rather than manufacture a version.

After selecting a candidate, compare its complete `Android.bp`/AIDL/uses-library graph against Android17 Soong, **and** against the actual vendor-required IMS/ExtPhone APIs. Preserve only justified LineageOS fixes, Xiaomi-specific needs and relevant PE history. Treat the AIDL and Java method signatures as ABI/API; superficial compilation is insufficient.

## Conditions before enabling in the ROM

- Proven and pinned official CodeLinaro SHA/tag, license provenance, Android17 source/APIs with supported Java HIDL/AIDL dependency graph.
- Verified coexistence with framework `telephony-ext`, `frameworks/opt/telephony`, `hardware/ril` and the vendor IMS APK/JAR and QTI manifests; no duplicate Java modules or permissions libraries.
- Independent PE-Reborn **mondrian-only** device/common/vendor implementation, checked against the modern LF52406 read-only reference, not a wholesale LineageOS tree copy.
- Soong/Make build, system_ext/product permissions, VINTF/SELinux policy, IMS registration, LTE/NR, VoLTE/VoWiFi, SIM/eSIM if applicable, data and emergency calls tested on hardware.

**Current status**: source comparison and discovery only. No verified Qualcomm branch has been chosen yet; no telephony source has been added to Android17 `default.xml`. Existing LF52406 trees untouched.

Links: [Qualcomm source](https://git.codelinaro.org/clo/la/platform/vendor/codeaurora/telephony), [PE source](https://github.com/PixelExperience-Reborn/vendor_codeaurora_telephony), [modern comparison source](https://github.com/LineageOS/android_vendor_codeaurora_telephony/tree/lineage-24.0), [PE AOSP17 matrix](https://github.com/PixelExperience-Reborn/manifest/blob/port/a17-aosp17-manifest-20261102/docs/ANDROID_17_135_REPOSITORY_PORT_MATRIX.md).
