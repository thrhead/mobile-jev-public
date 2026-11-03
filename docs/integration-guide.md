# LDM Android Integration Guide

This guide describes the public application integration boundary. It excludes model/PTE access, runtime-internal encoding, conformance APIs, private validation infrastructure, signing/release evidence, and any assumption that an unpublished binary is already available.

## Current public repository scope

- Public contract: `1.0`
- Current reference-sample dependency: `com.ldm:ldm-runtime-android:1.0.0-alpha1`
- Planned public Maven runtime coordinate: `io.github.thrhead:ldm-runtime-android:2.0.0-alpha1`
- Minimum Android API exercised by the sample configuration: API 26
- Java toolchain: 17
- Repository publication model: source + reference samples + documentation, with a verified SDK pre-publication candidate

The planned public Maven coordinate is **not yet publicly resolvable**. Do not substitute it into the reference samples until Maven Central publication has been verified in C1-B2.

## Application architecture

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

The sanitized reference samples show the runtime integration pattern through `LdmRuntime` and `LdmPublicAdapter`:

```kotlin
val runtime = LdmRuntime(context.applicationContext)
runtime.load()
val adapter = LdmPublicAdapter(runtime)
```

Close the runtime when the owning component is destroyed:

```kotlin
runtime.close()
```

## Current reference-sample dependency

The current sample Gradle files intentionally remain on the legacy/gated coordinate:

```kotlin
dependencies {
    implementation("com.ldm:ldm-runtime-android:1.0.0-alpha1")
}
```

They also require an explicitly supplied, separately authorized Maven repository URI via:

```text
-PldmSdkRepository=<uri>
```

or:

```text
LDM_SDK_REPOSITORY=<uri>
```

This remains intentional during C1-B1.

## Planned public dependency after activation

After Maven Central publication is independently verified, C1-B2 will migrate the samples to:

```kotlin
dependencies {
    implementation("io.github.thrhead:ldm-runtime-android:2.0.0-alpha1")
}
```

Until then, the new coordinate is documentation of the verified candidate identity, not an availability claim.

## Request construction

Use semantic domain and candidate IDs. Do not use array position as identity.

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

The application does not build an internal feature vector.

## Availability and outcomes

Availability is semantic application state. Consumers must not construct or mutate the runtime's internal mask.

- **DECISION:** use the runtime-selected canonical candidate and preserve LDM provenance.
- **ABSTAIN:** apply a deterministic application-owned fallback.
- **ERROR:** preserve the runtime error for diagnostics, then apply fallback separately.

The samples use:

- task priority fallback: `medium`
- notification timing fallback: `silent`

Both are labeled `APPLICATION_FALLBACK`.

## Consumer restrictions

Do not access `.pte` model files directly, invoke ExecuTorch as an application integration path, construct internal feature vectors, recalibrate probabilities, override OOD/abstention, perform local argmax substitution, or relabel application fallback as an LDM decision.
