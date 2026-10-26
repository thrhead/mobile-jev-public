# Third-Party Notices — Private Staging Draft

**Status: incomplete; not publication clearance.**

This file records third-party components established by the frozen SDK POMs, exact bundle inspection, and existing LDM build/SBOM evidence. It is not yet a complete notice set for a future public binary distribution.

## Exact frozen bundle inspection

The exact frozen SDK bundle with SHA256 `08adf8ced4fab0de66ac39f6f6a90543569305954bedf40621bf21409e4d2d80` was inspected.

The bundle contains the frozen LDM runtime AAR, the vendored ExecuTorch AAR, their POM files, a README, and checksums. No LICENSE, NOTICE, COPYING, or equivalent license-notice file is present in the outer ZIP, either AAR, or their nested `classes.jar` files.

The runtime AAR also contains private frozen model/PTE resources and therefore is not eligible for public export under the approved public boundary.

## ExecuTorch

- Component: ExecuTorch
- LDM vendored Maven coordinate: `com.ldm.vendor:executorch-android:1.5.1-ldm1`
- Version basis: upstream ExecuTorch 1.5.x / frozen LDM-pinned binary
- Upstream project: `pytorch/executorch`
- License: BSD-3-Clause

Binary redistribution under the upstream BSD-3-Clause terms requires preservation of the required copyright, license conditions, and disclaimer in the distribution documentation and/or other materials.

The exact vendored AAR does not itself contain that license/notice material. A future repaired public distribution must provide it explicitly and must validate any additional notices required by components actually incorporated into the native binary.

## POM-declared dependencies

The frozen Maven metadata declares these additional dependencies:

- `com.facebook.fbjni:fbjni:0.7.0` — upstream project identifies fbjni as Apache-2.0.
- `com.facebook.soloader:nativeloader:0.10.5` — part of SoLoader; upstream project identifies SoLoader as Apache-2.0.
- `androidx.core:core-ktx:1.13.1` — AndroidX dependency resolved by consumers.
- `org.jetbrains.kotlin:kotlin-stdlib:2.0.21` — Kotlin runtime dependency resolved by consumers.

These transitive artifacts are referenced by POM metadata rather than embedded as separate artifacts inside the frozen SDK ZIP. Their notice obligations must be evaluated against the final distribution model actually chosen.

## Public source/sample license

LDM-authored source/sample code exported into this staging repository is licensed under Apache-2.0. That license does not relicense ExecuTorch, AndroidX, Kotlin, fbjni, NativeLoader, model/PTE resources, or any other third-party/private binary.

## Gate

```text
THIRD_PARTY_NOTICE_COMPLIANCE = NOT_ESTABLISHED_REPACK_REQUIRED
PUBLIC_SDK_BINARY_PUBLICATION = BLOCKED
```

Before any public binary distribution, create a distributable artifact set that excludes private model/PTE resources and carries a complete third-party notice inventory supported by the binaries and dependency graph actually shipped.
