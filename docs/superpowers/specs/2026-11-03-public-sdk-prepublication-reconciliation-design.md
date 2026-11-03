# LDM C1-B — Public Repository Pre-Publication Reconciliation Design

**Status:** APPROVED IN CONVERSATION  
**Date:** 2026-11-03

## Intent

Reconcile `thrhead/mobile-jev-public` with the verified model-free Android SDK candidate without claiming that Maven Central or GitHub Release publication has occurred.

C1-B is split into:

- C1-B1: pre-publication reconciliation
- C1-B2: post-publication activation

This design covers C1-B1 only.

## Baselines

Public repository:

- `thrhead/mobile-jev-public`
- main: `68f0c15d056ac11926a5f0c1cb0f03ba65a4c3dd`

Authoritative private candidate source:

- `thrhead/mobile_jev@253b8a6ac2425928d649126dfe997d7cb7b31ea8`

Verified public candidate coordinates:

- `io.github.thrhead:ldm-runtime-android:2.0.0-alpha1`
- `io.github.thrhead:ldm-runtime-testkit-android:2.0.0-alpha1`

## Required state

The public repository must distinguish:

1. Legacy frozen/private SDK:
   - `com.ldm:ldm-runtime-android:1.0.0-alpha1`
   - embeds private model/PTE/protocol payloads
   - not public-distributable as-is

2. New model-free public candidate:
   - `io.github.thrhead:ldm-runtime-android:2.0.0-alpha1`
   - `io.github.thrhead:ldm-runtime-testkit-android:2.0.0-alpha1`
   - prepared and verified
   - not yet published/resolvable from Maven Central

Correct the legacy runtime AAR SHA typo in `docs/distribution.md`:

- wrong: `3bbb9cc6abc74c632702ac74f7dbce1c3218d4601ff61f11284a565250ad2ad26`
- correct: `3bbb9cc6abc74c632702ac74f7dbce1c3218d4601ff61f11284a565250ad2ad2`

## Provenance

Keep `EXPORT_MANIFEST.json` unchanged. It remains source/sample/docs export provenance.

Create `SDK_RELEASE_MANIFEST_2.0.0-alpha1.json` with:

- `schema_version = 1`
- `stage = PRE_PUBLICATION`
- `version = 2.0.0-alpha1`
- exact runtime/testkit coordinates above
- `authoritative_private_source_sha = 253b8a6ac2425928d649126dfe997d7cb7b31ea8`
- `publication_approved = false`
- `maven_central_publication = NOT_PERFORMED`
- `github_release_publication = NOT_PERFORMED`
- `private_model_pack_included = false`
- `frozen_contracts_modified = false`

Do not self-reference a public commit SHA.

## Public CI

Retain all existing source-only/private-boundary checks.

Add checks for the SDK release manifest and documentation state. No signing credentials, Maven Central credentials, binary upload, or release creation.

## Samples

Do not modify during C1-B1:

- `samples/task-priority-android/app/build.gradle.kts`
- `samples/task-priority-android/settings.gradle.kts`
- `samples/notification-timing-android/app/build.gradle.kts`
- `samples/notification-timing-android/settings.gradle.kts`

C1-B2 activates the new Maven coordinate only after Maven Central publication is verified.

## Non-goals

- no Maven Central publication
- no GitHub Release
- no AAR/ZIP upload
- no sample activation
- no model/PTE publication
- no signing credential addition
- no rewrite of `EXPORT_MANIFEST.json`
- no private evidence export
- no production-readiness claim
