# Mobile JEV / LDM — Public Source & Samples

This repository is the sanitized **source/sample/documentation surface** for the Local Decision Model (LDM) mobile integration work.

## Publication scope

- Public source/sample/docs surface: **available**
- LDM public source/sample license: **Apache-2.0**
- Legacy frozen SDK `com.ldm:ldm-runtime-android:1.0.0-alpha1`: **not publicly distributable as-is**
- Model-free public SDK `io.github.thrhead:ldm-runtime-android:2.0.0-alpha1`: **verified pre-publication candidate**
- Maven Central publication: **not performed**
- GitHub Release publication: **not performed**
- Production readiness: **not established**

Current machine-readable SDK state:

```text
MAVEN_CENTRAL_PUBLICATION = NOT_PERFORMED
GITHUB_RELEASE_PUBLICATION = NOT_PERFORMED
```

This repository was created with fresh Git history. It does **not** import the private authoritative repository history, frozen model/PTE bundles, golden fixtures, internal validation evidence, signing material, historical release artifacts, or the frozen SDK binaries.

## Legacy frozen SDK vs. model-free public candidate

The legacy frozen Android SDK remains:

```text
com.ldm:ldm-runtime-android:1.0.0-alpha1
```

That frozen package embeds private model/PTE resources and the protocol lock, so it is not eligible for public binary export as-is.

A separate packaging workstream has prepared and verified these model-free public candidate coordinates:

```text
io.github.thrhead:ldm-runtime-android:2.0.0-alpha1
io.github.thrhead:ldm-runtime-testkit-android:2.0.0-alpha1
```

They are documented here as **pre-publication candidates only**. This repository does not claim that those coordinates are currently resolvable from Maven Central. Publication requires a separate release/signing workstream and explicit publication approval.

## Reference samples

Two sanitized Android application-side samples are exported from the approved authoritative source snapshot:

- `samples/task-priority-android/` — local Task Inbox demonstrating semantic task-state collection, Room persistence, LDM public-boundary usage, and explicit `APPLICATION_FALLBACK` provenance.
- `samples/notification-timing-android/` — notification timing sample demonstrating semantic availability and application-owned `delay`, `show_now`, and `silent` actions.

These are **reference integration samples**. During this pre-publication phase they intentionally keep the legacy/gated SDK configuration rather than pretending the new Maven Central coordinate is already available.

## License scope

LDM-authored source, sample code, and documentation in this repository are licensed under the Apache License 2.0; see [`LICENSE`](LICENSE).

This license decision does **not** by itself license or authorize publication of the authoritative private repository, frozen model/PTE resources, the private SDK ZIP/AARs, vendored third-party binaries, private validation evidence, or signing material. Those remain outside this repository's public scope.

## Documentation

- [`docs/public-contract.md`](docs/public-contract.md) — public ownership boundary, canonical candidates, semantic fields, and outcome handling.
- [`docs/integration-guide.md`](docs/integration-guide.md) — Android public API integration pattern and pre-publication coordinate state.
- [`docs/android-quickstart.md`](docs/android-quickstart.md) — how to read and, where separately authorized, exercise the reference samples before public Maven activation.
- [`docs/distribution.md`](docs/distribution.md) — legacy frozen SDK inspection plus the separate model-free `2.0.0-alpha1` pre-publication state.
- [`THIRD_PARTY_NOTICES.md`](THIRD_PARTY_NOTICES.md) — source-only historical notice scope plus the model-free candidate dependency families.

## SDK release provenance

`EXPORT_MANIFEST.json` remains the provenance record for the original sanitized source/sample/docs export.

`SDK_RELEASE_MANIFEST_2.0.0-alpha1.json` is a separate pre-publication record for the model-free Android SDK candidate. It does not authorize publication.

## Repository boundary

Do not add:

- model or `.pte` files;
- SDK/runtime AARs, APKs, AABs, native `.so` libraries, or Maven binary repositories;
- golden fixtures or internal conformance/parity harnesses;
- credentials, `.env` secrets, keystores, signing keys, signing passwords, or certificates;
- private validation evidence or private workflow history;
- content copied from the authoritative private repository unless it is explicitly inside the approved public export boundary.

The public CI enforces this boundary. C1-B2 sample activation is deferred until the model-free runtime is actually verified as publicly resolvable from Maven Central.
