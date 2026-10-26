# Mobile JEV / LDM — Public Source & Samples

This repository is the sanitized **source/sample/documentation surface** for the Local Decision Model (LDM) mobile integration work.

## Publication scope

- Public surface: **source + reference samples + documentation only**
- LDM public source/sample license: **Apache-2.0**
- Frozen SDK binary publication: **blocked and out of scope for this repository**
- Production readiness: **not established**

This repository was created with fresh Git history. It does **not** import the private authoritative repository history, frozen model/PTE bundles, golden fixtures, internal validation evidence, signing material, historical release artifacts, or the frozen SDK binaries.

## Reference samples

Two sanitized Android application-side samples are exported from the approved authoritative source snapshot:

- `samples/task-priority-android/` — local Task Inbox demonstrating semantic task-state collection, Room persistence, LDM public-boundary usage, and explicit `APPLICATION_FALLBACK` provenance.
- `samples/notification-timing-android/` — notification timing sample demonstrating semantic availability and application-owned `delay`, `show_now`, and `silent` actions.

These are **reference integration samples**, not standalone public SDK distributions. They intentionally do not contain the LDM runtime binary or private model resources and therefore are not expected to build from this repository alone unless a separately authorized SDK repository is supplied.

## License scope

LDM-authored source, sample code, and documentation in this repository are licensed under the Apache License 2.0; see [`LICENSE`](LICENSE).

This license decision does **not** by itself license or authorize publication of the authoritative private repository, frozen model/PTE resources, the private SDK ZIP/AARs, vendored third-party binaries, private validation evidence, or signing material. Those remain outside this repository's public scope.

## Documentation

- [`docs/public-contract.md`](docs/public-contract.md) — public ownership boundary, canonical candidates, semantic fields, and outcome handling.
- [`docs/integration-guide.md`](docs/integration-guide.md) — Android public API integration pattern.
- [`docs/android-quickstart.md`](docs/android-quickstart.md) — how to read and, where separately authorized, exercise the reference samples.
- [`docs/distribution.md`](docs/distribution.md) — exact frozen SDK inspection result and why binary publication is outside this repository's scope.
- [`THIRD_PARTY_NOTICES.md`](THIRD_PARTY_NOTICES.md) — source-only notice scope and future binary-distribution requirements.

## SDK distribution

The Android API coordinate exercised by the reference samples is:

```text
com.ldm:ldm-runtime-android:1.0.0-alpha1
```

The exact frozen SDK bundle was inspected and matched its immutable SHA256 identity. Its runtime AAR embeds the private frozen model/PTE resources and protocol lock, so that bundle is **not eligible for public export as-is** under the approved boundary. It also lacks the complete license/notice material required for a future public binary distribution.

This repository therefore does **not** publish an SDK ZIP, AAR, Maven repository, APK, AAB, native library, or model/PTE payload. Any future public SDK packaging is a separate workstream and must establish its own export, licensing, notice, and verification gates.

## Authoritative source provenance

Sanitized exports are derived only from the approved private source snapshot recorded in `EXPORT_MANIFEST.json`.

The authoritative private repository remains separate and is not mirrored here.

## Repository boundary

Do not add:

- model or `.pte` files;
- SDK/runtime AARs, APKs, AABs, native `.so` libraries, or Maven binary repositories;
- golden fixtures or internal conformance/parity harnesses;
- credentials, `.env` secrets, keystores, signing keys, signing passwords, or certificates;
- private validation evidence or private workflow history;
- content copied from the authoritative private repository unless it is explicitly inside the approved public export boundary.

The public CI enforces this source-only boundary.
