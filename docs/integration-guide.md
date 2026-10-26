# LDM Android Integration Guide

This guide is for application developers reading the LDM public API integration surface. It intentionally excludes model/PTE access, runtime-internal encoding, conformance APIs, private validation infrastructure, signing/release evidence, and public SDK binary distribution.

## Current public repository scope

- Public contract: `1.0`
- Android runtime coordinate exercised by the reference samples: `com.ldm:ldm-runtime-android:1.0.0-alpha1`
- Minimum Android API exercised by the sample configuration: API 26
- Java toolchain: 17
- Repository publication model: **source + reference samples + documentation only**
- Public SDK binary distribution: **not provided by this repository**

The coordinate is documented for API compatibility and source readability. This repository does not publish or provide a public Maven repository for the binary. See `distribution.md`.

## Application architecture

A consumer application should keep these responsibilities separate:

```text
Application state collection
        ↓
Application semantic range guard
        ↓
Candidate availability policy
        ↓
Public DecisionRequest construction
        ↓
LDM public runtime adapter
        ↓
DECISION / ABSTAIN / ERROR routing
        ↓
Application side effect or application fallback
```

The application owns real side effects and fallback behavior. The runtime owns inference, encoding, masking, calibration, OOD handling, abstention, and LDM candidate selection.

## Runtime setup pattern

The sanitized reference samples show the public runtime pattern through `LdmRuntime` and `LdmPublicAdapter`:

```kotlin
val runtime = LdmRuntime(context.applicationContext)
runtime.load()
val adapter = LdmPublicAdapter(runtime)
```

Close the runtime when the owning component is destroyed:

```kotlin
runtime.close()
```

These snippets document the integration contract. They do not imply that the runtime binary is publicly distributed from this repository.

## Dependency coordinate

The reference samples declare:

```kotlin
dependencies {
    implementation("com.ldm:ldm-runtime-android:1.0.0-alpha1")
}
```

The sample settings require an explicitly supplied, separately authorized Maven repository URI:

```text
-PldmSdkRepository=<uri>
```

or:

```text
LDM_SDK_REPOSITORY=<uri>
```

The configuration fails intentionally when neither is supplied. This prevents the public source repository from silently depending on the authoritative private repository or private Release assets.

## Request construction

Use semantic domain and candidate IDs. Do not use array position as identity.

Example structure:

```kotlin
val request = LdmPublicDecisionRequest(
    "1.0",
    "task_priority",
    state,
    listOf(
        LdmPublicCandidate("critical", true),
        LdmPublicCandidate("high", true),
        LdmPublicCandidate("low", true),
        LdmPublicCandidate("medium", true),
    ),
)

val outcome = adapter.decide(request)
```

The `state` map contains semantic application fields. The application does not build an internal feature vector.

## Availability

Availability is semantic application state. For example, a notification application can mark `show_now` and `delay` unavailable when Android notification delivery is not currently permitted while leaving `silent` available.

The runtime translates semantic availability into its canonical mask. Consumers must not construct or mutate that internal mask.

## Outcome routing

### DECISION

Use the runtime-selected canonical candidate and preserve LDM provenance.

### ABSTAIN

Apply a deterministic application-owned fallback. Do not rewrite the runtime result to look like an LDM decision.

### ERROR

Keep the public runtime error intact for diagnostics, then apply the application-owned fallback separately.

The samples use:

- `task_priority` fallback: `medium`
- `notification_timing` fallback: `silent`

Both are labeled `APPLICATION_FALLBACK`.

## Application-side range protection

Applications may know that a collected semantic value is outside the supported input envelope before calling the runtime. In that case, intercept it at the application boundary and use the application fallback rather than clamping or normalizing the value.

The sample implementations demonstrate this pattern. See `public-contract.md` for their exercised fields and range guards.

## What consumers must not do

Do not:

- access `.pte` model files directly;
- invoke ExecuTorch directly as an application integration path;
- construct the internal feature vector;
- normalize or clamp semantic state to force acceptance;
- recalibrate probabilities;
- override OOD or abstention;
- run a local argmax over returned probabilities;
- substitute another candidate after a valid LDM DECISION;
- treat application fallback as an LDM decision.

## Reference samples

- `samples/task-priority-android/` — local Task Inbox with Room persistence and application fallback provenance.
- `samples/notification-timing-android/` — Android notification timing reference with `show_now`, delayed-alarm, and silent application actions.

These samples are source exports of application-side code only. They do not contain the private model bundle, SDK binaries, or internal validation history.
