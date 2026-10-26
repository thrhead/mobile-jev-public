# Third-Party Notice Boundary

This repository publishes LDM-authored **source, reference samples, and documentation only**. It does not redistribute the frozen LDM SDK binary, vendored ExecuTorch AAR, APK/AAB files, native `.so` libraries, Maven binary repositories, or model/PTE payloads.

## Source-only public surface

For the current repository scope:

- LDM-authored source/sample/documentation content is licensed under Apache-2.0;
- third-party dependencies may be named by build metadata or imported by source code, but their binary artifacts are not copied into or redistributed by this repository;
- the current public source surface therefore does not claim to provide a complete binary third-party notice bundle.

Current source-only gate:

```text
PUBLIC_SOURCE_ONLY_NOTICE_HYGIENE = PASS_NO_THIRD_PARTY_BINARY_REDISTRIBUTION
```

## Frozen SDK inspection record

The exact frozen SDK bundle with SHA256 `08adf8ced4fab0de66ac39f6f6a90543569305954bedf40621bf21409e4d2d80` was inspected.

It contains the frozen LDM runtime AAR, the vendored ExecuTorch AAR, their POM files, a README, and checksums. No LICENSE, NOTICE, COPYING, or equivalent license-notice file is present in the outer ZIP, either AAR, or their nested `classes.jar` files.

The runtime AAR also embeds private frozen model/PTE resources, so the frozen bundle is not eligible for public export under the approved boundary.

## ExecuTorch and declared dependencies

The frozen SDK evidence identifies:

- `com.ldm.vendor:executorch-android:1.5.1-ldm1` — upstream ExecuTorch, BSD-3-Clause basis;
- `com.facebook.fbjni:fbjni:0.7.0` — declared by the vendored ExecuTorch POM;
- `com.facebook.soloader:nativeloader:0.10.5` — declared by the vendored ExecuTorch POM;
- `androidx.core:core-ktx:1.13.1` — declared by the runtime POM;
- `org.jetbrains.kotlin:kotlin-stdlib:2.0.21` — declared by the runtime POM.

These entries document the frozen private SDK evidence. They do **not** mean those binaries are redistributed by this public source repository.

## Future binary distribution

A future public SDK distribution, if created, must separately establish the license/notice obligations for the exact artifact set actually shipped. For the current frozen bundle:

```text
PUBLIC_SDK_BINARY_PUBLICATION = BLOCKED_PRIVATE_MODEL_PTE_EMBEDDED
PUBLIC_SDK_THIRD_PARTY_NOTICE_COMPLIANCE = NOT_ESTABLISHED_REPACK_REQUIRED
```

Do not copy the frozen SDK ZIP/AARs into this repository. A future distributable must exclude private model/PTE resources and include the complete third-party notices required by its actual binaries and dependency graph.
