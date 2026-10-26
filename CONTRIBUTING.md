# Contributing

This repository accepts changes only within the sanitized public **source/sample/documentation** boundary.

## Allowed contribution scope

Contributions may target:

- public-facing documentation;
- sanitized application-side reference sample source;
- public-safe build/configuration files that do not embed or acquire private artifacts;
- secret-independent static CI and repository hygiene.

## Prohibited content

Do not submit:

- frozen model/PTE payloads;
- SDK/runtime AARs, APKs, AABs, native `.so` libraries, or Maven binary repositories;
- golden fixtures or private validation datasets;
- internal STEP 12/13/14/15 evidence workflows or harnesses;
- credentials, tokens, keystores, signing keys, signing passwords, or certificates;
- historical private Actions artifacts/logs;
- private repository history or tags;
- content copied from the authoritative private repository unless it is explicitly inside the approved export boundary.

## Reference samples are source-only

The Android samples intentionally do not include a public LDM SDK binary. A contribution must not make them silently download, embed, or point at private distribution infrastructure.

Any future public SDK packaging is a separate workstream and is not accepted through ordinary source-surface contributions unless its export/licensing/notice design has first been explicitly approved.

## Review rule

If there is uncertainty about whether a file belongs in the public surface, treat it as **review required**, not implicitly allowed.
