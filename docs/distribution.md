# Distribution Status

This repository is private staging for a future public LDM surface. Nothing in this document authorizes public publication of the LDM SDK binary.

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

- Upstream ExecuTorch `v1.5.1` is distributed under BSD-3-Clause terms.
- Those terms permit source and binary redistribution subject to preservation of the required copyright, conditions, and disclaimer notices.
- Existing private LDM supply-chain checks establish the frozen SDK/bundle identity but do not verify third-party license/notice completeness.

## What is not established

The exact immutable SDK bundle could not be inspected in this staging session because the historical Actions copy has expired and the available GitHub connector does not expose private Release asset bytes for local inspection.

Therefore this work does **not** establish:

- whether the frozen LDM runtime AAR embeds frozen `.pte` model resources;
- whether the vendored ExecuTorch AAR contains every third-party notice required by its packaged components;
- whether the existing Maven bundle contains sufficient license/notice material for public binary redistribution;
- an owner-selected license for LDM-authored public source/sample code.

## Current gate state

```text
LDM_SOURCE_SAMPLE_LICENSE = PENDING_OWNER_DECISION
PUBLIC_SDK_REDISTRIBUTION_RIGHTS = NOT_ESTABLISHED
THIRD_PARTY_NOTICE_COMPLIANCE = NOT_ESTABLISHED
PUBLIC_SDK_BINARY_PUBLICATION = BLOCKED
```

## Publication rule

Do not publish the frozen SDK ZIP, runtime AAR, vendored ExecuTorch AAR, model/PTE resources, or a public Maven repository until both redistribution and notice gates are established from exact binary evidence and the LDM source/sample license has been explicitly selected.
