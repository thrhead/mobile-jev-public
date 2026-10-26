# Distribution Status

This repository is private staging for a future public LDM surface. Nothing in this document authorizes public publication of the frozen LDM SDK binary.

## Evidence scope

Authoritative private source snapshot:

`thrhead/mobile_jev@2cfa0ef54f8a951c93a0962b23767efc8cb3aa54`

Frozen Android SDK identity:

- Maven coordinate: `com.ldm:ldm-runtime-android:1.0.0-alpha1`
- runtime AAR SHA256: `3bbb9cc6abc74c632702ac74f7dbce1c3218d4601ff61f11284a565250ad2ad26`
- vendored ExecuTorch coordinate: `com.ldm.vendor:executorch-android:1.5.1-ldm1`
- vendored ExecuTorch AAR SHA256: `cc5f0cc577793b4221587285e7a7f685c271c85a0b3bfd2a3a36457e4084af85`
- immutable private SDK bundle SHA256: `08adf8ced4fab0de66ac39f6f6a90543569305954bedf40621bf21409e4d2d80`

The existing private immutable GitHub Release is an acquisition artifact for CI. Its release description explicitly does not expand public-publication-readiness claims.

## What is established

- LDM-authored public source/sample code in this staging repository is owner-approved for Apache-2.0 licensing.
- The exact immutable SDK bundle was inspected and its SHA256 matched the frozen identity above.
- The exact runtime AAR and vendored ExecuTorch AAR matched their previously recorded SHA256 identities.
- The runtime AAR embeds the frozen LDM model/PTE resources and frozen protocol lock as library assets.
- Those embedded private model/PTE resources are outside the approved public export boundary.
- The exact SDK ZIP, runtime AAR, vendored ExecuTorch AAR, and their nested `classes.jar` files contain no LICENSE, NOTICE, COPYING, or equivalent license-notice entry.
- The runtime POM depends on the vendored ExecuTorch artifact, AndroidX Core KTX 1.13.1, and Kotlin stdlib 2.0.21.
- The vendored ExecuTorch POM depends on fbjni 0.7.0 and NativeLoader 0.10.5.
- Upstream ExecuTorch is BSD-3-Clause licensed; binary redistribution requires reproduction of the required copyright, conditions, and disclaimer in distribution documentation and/or other materials.

## Exact-binary publication result

The frozen SDK bundle is **not an acceptable public distribution artifact as-is** for two independent reasons:

1. **Public export boundary:** the runtime AAR contains private frozen model/PTE assets that the approved export boundary keeps private.
2. **Notice packaging:** the exact bundle/AARs do not themselves carry the required ExecuTorch BSD license/notice material, and complete notice compliance for a repaired public distribution has not yet been established.

This is not a runtime defect and does not reopen any frozen runtime/model/protocol gate. It is a public packaging/distribution boundary result only.

## Current gate state

```text
LDM_SOURCE_SAMPLE_LICENSE = APACHE_2_0_APPROVED
FROZEN_SDK_EXACT_BINARY_INSPECTION = PASS
FROZEN_SDK_PUBLIC_EXPORT_BOUNDARY = BLOCKED_PRIVATE_MODEL_PTE_EMBEDDED
PUBLIC_SDK_REDISTRIBUTION_RIGHTS = NOT_ESTABLISHED
THIRD_PARTY_NOTICE_COMPLIANCE = NOT_ESTABLISHED_REPACK_REQUIRED
PUBLIC_SDK_BINARY_PUBLICATION = BLOCKED
```

## Publication rule

The Apache-2.0 license covers the LDM-authored public source/sample surface in this staging repository. It does not by itself authorize publication of the frozen SDK ZIP, runtime AAR, vendored ExecuTorch AAR, model/PTE resources, or a public Maven repository.

Do not publish the current frozen SDK bundle or its AARs. A future public binary distribution, if desired, must be separately designed so that private model/PTE resources remain outside the public export and complete third-party license/notice obligations are carried by the actual distributable artifact set.
