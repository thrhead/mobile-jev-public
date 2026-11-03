# Distribution Boundary

This repository currently publishes **source, reference samples, and documentation**. It does not yet publish the model-free Android SDK binary through Maven Central or GitHub Releases.

## Legacy frozen SDK evidence

Frozen Android SDK identity:

- Maven coordinate: `com.ldm:ldm-runtime-android:1.0.0-alpha1`
- runtime AAR SHA256: `3bbb9cc6abc74c632702ac74f7dbce1c3218d4601ff61f11284a565250ad2ad2`
- vendored ExecuTorch coordinate: `com.ldm.vendor:executorch-android:1.5.1-ldm1`
- vendored ExecuTorch AAR SHA256: `cc5f0cc577793b4221587285e7a7f685c271c85a0b3bfd2a3a36457e4084af85`
- immutable private SDK bundle SHA256: `08adf8ced4fab0de66ac39f6f6a90543569305954bedf40621bf21409e4d2d80`

The exact immutable legacy bundle was inspected and matched that identity. Its runtime AAR embeds the frozen model/PTE resources and protocol lock as library assets, so the legacy `1.0.0-alpha1` package is **not eligible for public export as-is**.

That conclusion is historical and remains valid. It does not reopen or modify the frozen runtime, model, PTE, protocol, or STEP11A contracts.

## Model-free public SDK candidate

A separate public-packaging workstream has prepared and verified these model-free candidate coordinates:

```text
io.github.thrhead:ldm-runtime-android:2.0.0-alpha1
io.github.thrhead:ldm-runtime-testkit-android:2.0.0-alpha1
```

Current publication state:

```text
PUBLIC_SDK_CANDIDATE = VERIFIED_PRE_PUBLICATION
MAVEN_CENTRAL_PUBLICATION = NOT_PERFORMED
GITHUB_RELEASE_PUBLICATION = NOT_PERFORMED
PRIVATE_MODEL_PACK_INCLUDED = FALSE
PRODUCTION_READINESS = NOT_ESTABLISHED
```

These coordinates are not yet claimed to be publicly resolvable. Publication requires a separate release/signing workstream and explicit approval.

## Public repository state

In C1-B1:

- no SDK ZIP/AAR is committed here;
- no APK/AAB/native `.so` artifact is committed here;
- no model/PTE resource is committed here;
- no Maven binary repository is committed here;
- the existing reference samples remain distribution-gated until C1-B2 activation.

## Provenance separation

`EXPORT_MANIFEST.json` remains the source/sample/docs export provenance record.

`SDK_RELEASE_MANIFEST_2.0.0-alpha1.json` separately records the model-free SDK candidate's **PRE_PUBLICATION** state.

## Next activation boundary

C1-B2 begins only after this coordinate is verified as publicly resolvable from Maven Central:

```text
io.github.thrhead:ldm-runtime-android:2.0.0-alpha1
```

Only then may the public samples switch to the public Maven coordinate and add real public sample-build CI.
