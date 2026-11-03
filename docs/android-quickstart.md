# Android Reference Samples

The Android samples in this repository are sanitized **application-side reference integrations**. They do not bundle or publish the private LDM SDK, frozen model/PTE resources, signing material, private CI artifacts, or a public Maven repository.

Current C1-B1 state:

```text
PUBLIC SDK CANDIDATE = PREPARED
PUBLIC DISTRIBUTION = NOT YET ACTIVATED
```

The prepared public candidate identity is:

```text
io.github.thrhead:ldm-runtime-android:2.0.0-alpha1
```

It is not yet claimed to be publicly resolvable from Maven Central.

## What you can use publicly

You can inspect the samples to understand:

- semantic state collection;
- application-side range guards;
- semantic candidate availability;
- request construction through the LDM public boundary;
- DECISION / ABSTAIN / ERROR routing;
- explicit `APPLICATION_FALLBACK` provenance;
- real application side effects owned by the app.

## Build status before Maven activation

The reference samples intentionally remain on the existing legacy/gated dependency path during C1-B1.

Without a separately authorized SDK repository, dependency resolution is expected to stop with:

```text
LDM SDK publication is pending redistribution clearance.
```

That failure remains intentional until C1-B2.

## Optional authorized staging build path

For maintainers or environments that already have separately authorized access to an approved SDK Maven repository, the Gradle shells continue to accept either:

```text
-PldmSdkRepository=<approved-maven-repository-uri>
```

or:

```text
LDM_SDK_REPOSITORY=<approved-maven-repository-uri>
```

No private repository path or private GitHub Release URL is embedded in the sample configuration.

### Task Priority sample

Configuration:

- compileSdk 36
- targetSdk 36
- minSdk 26
- Java 17
- Room 2.6.1

Canonical candidates: `critical`, `high`, `low`, `medium`

Application fallback: `medium`

### Notification Timing sample

Configuration:

- compileSdk 35
- targetSdk 35
- minSdk 26
- Java 17

Canonical candidates: `delay`, `show_now`, `silent`

Application fallback: `silent`

The delayed path does not promise exact wall-clock delivery.

## C1-B2 activation

Only after the model-free runtime is independently verified as publicly resolvable from Maven Central will the samples switch to:

```text
io.github.thrhead:ldm-runtime-android:2.0.0-alpha1
```

At that point the custom staging-repository gate can be removed and real public sample-build CI can be enabled.

Nothing in this pre-publication repository state authorizes publication of the legacy frozen SDK bundle or private model assets.
