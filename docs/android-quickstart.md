# Android Sample Quickstart — Private Staging

The Android samples in this repository contain sanitized application-side source and public-safe Gradle shells. They do **not** bundle the private LDM SDK, model/PTE resources, signing material, or private CI artifacts.

## Prerequisites

- JDK 17
- Android SDK matching the sample compile SDK
- an explicitly approved staging Maven repository containing `com.ldm:ldm-runtime-android:1.0.0-alpha1`

Public SDK binary distribution is not yet approved. If you do not have an approved staging Maven repository, the build is expected to stop before dependency resolution.

## Task Priority sample

```bash
cd samples/task-priority-android
gradle -PldmSdkRepository=<approved-maven-repository-uri> :app:assembleDebug
```

Configuration:

- compileSdk 36
- targetSdk 36
- minSdk 26
- Java 17
- Room 2.6.1

The sample is a local Task Inbox. It collects semantic task state, rejects known out-of-range state before runtime invocation, supplies semantic candidate availability, calls the LDM public adapter, and keeps LDM decisions separate from `APPLICATION_FALLBACK`.

Canonical candidates:

`critical`, `high`, `low`, `medium`

Application fallback:

`medium`

## Notification Timing sample

```bash
cd samples/notification-timing-android
gradle -PldmSdkRepository=<approved-maven-repository-uri> :app:assembleDebug
```

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

## Repository URI input

Both sample projects accept the SDK repository through either:

```text
-PldmSdkRepository=<uri>
```

or environment variable:

```text
LDM_SDK_REPOSITORY=<uri>
```

No default private repository path or private GitHub Release URL is embedded in the staging build configuration.

## Expected staging failure without an SDK repository

Until the redistribution gate is closed, this is intentional:

```text
LDM SDK publication is pending redistribution clearance.
```

That failure demonstrates that the public-staging sample cannot silently reach into private distribution infrastructure.

## Contract guidance

Read:

- `public-contract.md` for semantic fields, candidate IDs, and ownership boundaries;
- `integration-guide.md` for the public API integration pattern;
- `distribution.md` for the current SDK publication block.
