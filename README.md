# Mobile JEV / LDM — Public Staging

This repository is the **private staging area** for a future sanitized public-facing surface of the Local Decision Model (LDM) mobile integration work.

## Status

- Visibility: **private staging**
- Production readiness: **not established**
- Public release readiness: **not established**
- SDK binary redistribution: **not yet approved**

This repository is intentionally created with a fresh Git history. It does **not** import the private authoritative repository history, frozen model/PTE bundles, golden fixtures, internal validation evidence, signing material, or historical release artifacts.

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

Public-facing documentation, sample source, build configuration, and CI will be added only after review against the approved export boundary.
