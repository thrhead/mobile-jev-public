# Security Policy

This repository is the sanitized public **source/sample/documentation surface** for LDM mobile integration work. It intentionally excludes private model/runtime assets and signing material.

## Do not submit sensitive material

Do not commit or attach:

- credentials, tokens, passwords, private keys, keystores, certificates, or signing material;
- `.env` files containing secrets;
- model or `.pte` files;
- SDK/runtime AARs, APKs, AABs, native `.so` libraries, or private Maven binary repositories;
- golden validation fixtures;
- private evidence bundles, internal parity/conformance harnesses, or private workflow artifacts;
- content copied from the authoritative private repository unless it is explicitly within the approved export boundary.

If sensitive or private material is discovered, stop further publication of the affected content and treat the exposure as a security incident until it is assessed and remediated.

## Security reports

Do **not** put sensitive vulnerability details, credentials, private artifacts, or exploit material into a public issue.

Use GitHub's private vulnerability-reporting flow if it is available for this repository. If that option is unavailable, contact the repository owner through a private channel before disclosing sensitive details publicly.

## Scope note

The frozen private SDK binary is not distributed by this repository. Reports about private model/PTE contents or private validation infrastructure should not be published here as attachments or copied evidence.
