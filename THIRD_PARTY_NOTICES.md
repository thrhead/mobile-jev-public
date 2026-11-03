# Third-Party Notices

This file preserves the source-only notice boundary and documents the dependency families permitted for the model-free Android SDK pre-publication candidate.

## Source-only public surface

For the current repository source/sample/docs surface, third-party binary artifacts are not copied into this repository.

```text
PUBLIC_SOURCE_ONLY_NOTICE_HYGIENE = PASS_NO_THIRD_PARTY_BINARY_REDISTRIBUTION
```

The legacy frozen SDK bundle is not redistributed here.

## Model-free public SDK candidate dependency families

The verified public release graph permits these dependency families:

- `org.pytorch:executorch-android` — ExecuTorch — BSD 3-Clause basis.
- `com.facebook.fbjni` — fbjni — Apache License 2.0.
- `com.facebook.soloader` — SoLoader / NativeLoader — Apache License 2.0.
- `com.google.guava` — Guava / ListenableFuture — Apache License 2.0.
- `AndroidX` (`androidx.*`) — Apache License 2.0.
- `kotlinx.coroutines` (`org.jetbrains.kotlinx:kotlinx-coroutines-*`) — Apache License 2.0.
- `Kotlin` (`org.jetbrains.kotlin:*`) — Apache License 2.0.
- `JetBrains annotations` (`org.jetbrains:annotations`) — Apache License 2.0.

No third-party binary is vendored into the model-free LDM public runtime AAR. ExecuTorch and its transitive dependencies are intended to resolve from public repositories in the eventual published distribution.

## Pre-publication state

This notice inventory documents the permitted dependency families for the prepared candidate. It does not mean the candidate is already published.

```text
MAVEN_CENTRAL_PUBLICATION = NOT_PERFORMED
GITHUB_RELEASE_PUBLICATION = NOT_PERFORMED
```

Exact final release-graph and artifact checksums remain publication-stage evidence and are not asserted by this source repository before publication.
