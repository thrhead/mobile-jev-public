# Android Reference Samples

The Android samples in this repository are sanitized **application-side reference integrations**. They do **not** bundle or publish the private LDM SDK, frozen model/PTE resources, signing material, private CI artifacts, or a public Maven repository.

Current C1-B1 state:

```text
PUBLIC SDK CANDIDATE = PREPARED
PUBLIC DISTRIBUTION = NOT YET ACTIVATED
```

Prepared public candidate identity:

```text
io.github.thrhead:ldm-runtime-android:2.0.0-alpha1
```

That coordinate is not yet claimed to be publicly resolvable from Maven Central.

## What you can use publicly

You can inspect the samples to understand:

- semantic state collection;
- application-side range guards;
- semantic candidate availability;
- request construction through the LDM public boundary;
- DECISION / ABSTAIN / ERROR routing;
- explicit `APPLICATION_FALLBACK` provenance;
- real application side effects owned by the app.

The samples are intentionally separated from model/runtime internals.

## Build status

These samples are **not standalone public builds** during C1-B1. The legacy frozen SDK bundle cannot be published as-is because its runtime AAR embeds private frozen model/PTE resources.

Without a separately authorized SDK repository, dependency resolution is expected to stop with:

```text
LDM SDK publication is pending redistribution clearance.
```

That failure is intentional: the public source repository must not silently reach into private distribution infrastructure.

## Optional authorized build path

For maintainers or environments that already have separately authorized access to an approved SDK Maven repository, the existing Gradle shells accept either:

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

Canonical candidates:

`critical`, `high`, `low`, `medium`

Application fallback:

`medium`

The sample is a local Task Inbox. It collects semantic task state, rejects known out-of-range state before runtime invocation, supplies semantic candidate availability, calls the public LDM boundary, and keeps LDM decisions separate from `APPLICATION_FALLBACK`.

### Notification Timing sample

Configuration:

- compileSdk 35
- targetSdk 35
- minSdk 26
- Java 17

Canonical candidates:

`delay`, `show_now`, `silent`

Application fallback:

`silent`

Application actions demonstrated by the sample:

- `show_now` — post a notification when Android permission/settings permit it;
- `delay` — schedule an application broadcast using an inexact idle-aware alarm;
- `silent` — do not post an interruptive notification.

The sample does not promise exact wall-clock delivery for the delayed path.

## Contract guidance

Read:

- `public-contract.md` for semantic fields, candidate IDs, and ownership boundaries;
- `integration-guide.md` for the public API integration pattern;
- `distribution.md` for the legacy frozen SDK inspection and current pre-publication candidate state.

## C1-B2 activation

Only after the model-free runtime is independently verified as publicly resolvable from Maven Central will the samples switch to:

```text
io.github.thrhead:ldm-runtime-android:2.0.0-alpha1
```

At that point the custom staging-repository gate can be removed and real public sample-build CI can be enabled.

Nothing in this C1-B1 repository state authorizes publication of the frozen private SDK bundle or private model assets.
