# Mobile JEV / LDM — Public Staging

This repository is the **private staging area** for a future sanitized public-facing surface of the Local Decision Model (LDM) mobile integration work.

## Status

- Visibility: **private staging**
- Production readiness: **not established**
- Public release readiness: **not established**
- LDM public source/sample license: **Apache-2.0**
- Frozen SDK binary publication: **blocked**

This repository was created with fresh Git history. It does **not** import the private authoritative repository history, frozen model/PTE bundles, golden fixtures, internal validation evidence, signing material, or historical release artifacts.

## What is staged here

Two sanitized Android application samples are currently exported from the approved authoritative source snapshot:

- `samples/task-priority-android/` — local Task Inbox demonstrating semantic task-state collection, Room persistence, public LDM decisions, and explicit `APPLICATION_FALLBACK` provenance.
- `samples/notification-timing-android/` — notification timing sample demonstrating semantic availability and application-owned `delay`, `show_now`, and `silent` actions.

The samples contain application-side source only. The private model/PTE bundle and internal validation infrastructure are not part of the export.

## License scope

LDM-authored source and sample code in this public-staging repository is licensed under the Apache License 2.0; see [`LICENSE`](LICENSE).

This license decision does **not** by itself license or authorize publication of the authoritative private repository, frozen model/PTE resources, the private SDK ZIP/AARs, vendored third-party binaries, private validation evidence, or signing material. Those remain governed by the explicit export and redistribution gates.

## Documentation

- [`docs/public-contract.md`](docs/public-contract.md) — public ownership boundary, canonical candidates, semantic fields, and outcome handling.
- [`docs/integration-guide.md`](docs/integration-guide.md) — Android public API integration pattern.
- [`docs/android-quickstart.md`](docs/android-quickstart.md) — private-staging build instructions.
- [`docs/distribution.md`](docs/distribution.md) — exact frozen SDK inspection result and current publication block.
- [`THIRD_PARTY_NOTICES.md`](THIRD_PARTY_NOTICES.md) — provisional third-party notice inventory; not publication clearance.

## SDK distribution

The Android public API coordinate is documented as:

```text
com.ldm:ldm-runtime-android:1.0.0-alpha1
```

The exact frozen SDK bundle was inspected and matched its immutable SHA256 identity. Its runtime AAR embeds the private frozen model/PTE resources and protocol lock, so the frozen bundle is **not eligible for public export as-is** under the approved boundary. The exact bundle/AARs also do not carry complete license/notice material for a future public binary distribution.

No public SDK binary distribution is provided by this staging repository. The sample Gradle configuration remains intentionally distribution-gated.

## Authoritative source provenance

Sanitized exports are derived only from the approved private source snapshot recorded in `EXPORT_MANIFEST.json`.

The authoritative private repository remains separate and is not mirrored here.

## Staging rules

Do not add:

- model or `.pte` files;
- golden fixtures or internal conformance/parity harnesses;
- credentials, `.env` secrets, keystores, signing keys, or signing passwords;
- private validation evidence or private workflow history;
- the current frozen SDK ZIP/AARs.

Repository visibility must remain private until every pre-publication gate is explicitly reviewed.
