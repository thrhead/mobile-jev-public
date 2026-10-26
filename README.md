# Mobile JEV / LDM — Public Staging

This repository is the **private staging area** for a future sanitized public-facing surface of the Local Decision Model (LDM) mobile integration work.

## Status

- Visibility: **private staging**
- Production readiness: **not established**
- Public release readiness: **not established**
- SDK binary redistribution: **not yet approved**
- LDM source/sample license: **pending owner decision**

This repository was created with fresh Git history. It does **not** import the private authoritative repository history, frozen model/PTE bundles, golden fixtures, internal validation evidence, signing material, or historical release artifacts.

## What is staged here

Two sanitized Android application samples are currently exported from the approved authoritative source snapshot:

- `samples/task-priority-android/` — local Task Inbox demonstrating semantic task-state collection, Room persistence, public LDM decisions, and explicit `APPLICATION_FALLBACK` provenance.
- `samples/notification-timing-android/` — notification timing sample demonstrating semantic availability and application-owned `delay`, `show_now`, and `silent` actions.

The samples contain application-side source only. The private model/PTE bundle and internal validation infrastructure are not part of the export.

## Documentation

- [`docs/public-contract.md`](docs/public-contract.md) — public ownership boundary, canonical candidates, semantic fields, and outcome handling.
- [`docs/integration-guide.md`](docs/integration-guide.md) — Android public API integration pattern.
- [`docs/android-quickstart.md`](docs/android-quickstart.md) — private-staging build instructions.
- [`docs/distribution.md`](docs/distribution.md) — current SDK redistribution gate and explicit blockers.
- [`THIRD_PARTY_NOTICES.md`](THIRD_PARTY_NOTICES.md) — provisional third-party notice inventory; not publication clearance.

## SDK distribution

The Android public API coordinate is documented as:

```text
com.ldm:ldm-runtime-android:1.0.0-alpha1
```

No public SDK binary distribution is provided by this staging repository yet. The sample Gradle configuration requires an explicitly approved Maven repository URI and fails intentionally when one is not supplied.

Do not publish the SDK ZIP/AARs or a public Maven repository until redistribution-rights and third-party-notice gates are established from exact binary evidence.

## Authoritative source provenance

Sanitized exports are derived only from the approved private source snapshot recorded in `EXPORT_MANIFEST.json`.

The authoritative private repository remains separate and is not mirrored here.

## Staging rules

Do not add:

- model or `.pte` files;
- golden fixtures or internal conformance/parity harnesses;
- credentials, `.env` secrets, keystores, signing keys, or signing passwords;
- private validation evidence or private workflow history;
- SDK binaries until redistribution and third-party notice gates are explicitly established.

Repository visibility must remain private until every pre-publication gate is explicitly reviewed.
