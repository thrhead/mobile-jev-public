# Distribution Boundary

This repository publishes **source, reference samples, and documentation only**. It does not publish the frozen LDM SDK binary or any model/runtime payload.

## Authoritative evidence

Authoritative private source snapshot:

`thrhead/mobile_jev@2cfa0ef54f8a951c93a0962b23767efc8cb3aa54`

Frozen Android SDK identity:

- Maven coordinate: `com.ldm:ldm-runtime-android:1.0.0-alpha1`
- runtime AAR SHA256: `3bbb9cc6abc74c632702ac74f7dbce1c3218d4601ff61f11284a565250ad2ad26`
- vendored ExecuTorch coordinate: `com.ldm.vendor:executorch-android:1.5.1-ldm1`
- vendored ExecuTorch AAR SHA256: `cc5f0cc577793b4221587285e7a7f685c271c85a0b3bfd2a3a36457e4084af85`
- immutable private SDK bundle SHA256: `08adf8ced4fab0de66ac39f6f6a90543569305954bedf40621bf21409e4d2d80`

## Exact frozen SDK inspection

The exact immutable SDK bundle was inspected and matched the frozen identity above.

The inspection established that:

- the runtime AAR and vendored ExecuTorch AAR match their previously recorded identities;
- the runtime AAR embeds the frozen LDM model/PTE resources and frozen protocol lock as library assets;
- those embedded private model/PTE resources are outside the approved public export boundary;
- the exact SDK ZIP, runtime AAR, vendored ExecuTorch AAR, and nested `classes.jar` files contain no LICENSE, NOTICE, COPYING, or equivalent license-notice entry;
- the runtime POM declares the vendored ExecuTorch artifact, AndroidX Core KTX 1.13.1, and Kotlin stdlib 2.0.21;
- the vendored ExecuTorch POM declares fbjni 0.7.0 and NativeLoader 0.10.5.

The frozen SDK bundle is therefore **not eligible for public export as-is**. This is a packaging/export-boundary result only; it does not reopen or modify the frozen runtime, model, PTE, protocol, or STEP11A contracts.

## Source-only publication decision

The selected public publication model is:

```text
PUBLIC_SURFACE = SOURCE + REFERENCE_SAMPLES + DOCUMENTATION
PUBLIC_SDK_BINARY_DISTRIBUTION = OUT_OF_SCOPE / BLOCKED
```

For this source-only surface:

- no SDK ZIP/AAR is redistributed;
- no APK/AAB/native `.so` artifact is redistributed;
- no model/PTE resource is redistributed;
- no Maven binary repository is published;
- public samples remain intentionally dependent on a separately authorized SDK repository if someone needs to exercise them as runnable projects.

Because no third-party binary artifact is redistributed by this repository, complete SDK binary notice packaging is **not a prerequisite for source-only repository visibility**. It remains a prerequisite for any future public SDK binary distribution.

## Current gate state

```text
LDM_SOURCE_SAMPLE_LICENSE = APACHE_2_0_APPROVED
FROZEN_SDK_EXACT_BINARY_INSPECTION = PASS
FROZEN_SDK_PUBLIC_EXPORT_BOUNDARY = BLOCKED_PRIVATE_MODEL_PTE_EMBEDDED
PUBLIC_SOURCE_ONLY_NOTICE_HYGIENE = PASS_NO_THIRD_PARTY_BINARY_REDISTRIBUTION
PUBLIC_REPO_BUILD = NOT_APPLICABLE_REFERENCE_SOURCE_ONLY_SURFACE
PUBLIC_SDK_BINARY_PUBLICATION = BLOCKED_PRIVATE_MODEL_PTE_EMBEDDED
PUBLIC_SDK_REDISTRIBUTION_RIGHTS = NOT_ESTABLISHED
PUBLIC_SDK_THIRD_PARTY_NOTICE_COMPLIANCE = NOT_ESTABLISHED_REPACK_REQUIRED
```

## Future SDK work

If a public SDK is desired later, treat that as a separate bounded workstream. The distributable must keep private model/PTE resources outside the public export and must carry a complete third-party license/notice inventory supported by the exact binaries and dependency graph actually shipped.
