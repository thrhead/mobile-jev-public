# Third-Party Notices — Private Staging Draft

**Status: incomplete; not publication clearance.**

This file records third-party components already established by LDM build/SBOM evidence. It must not be interpreted as a complete legal notice inventory until the exact distributable SDK artifacts are inspected.

## ExecuTorch

- Component: ExecuTorch
- LDM vendored Maven coordinate: `com.ldm.vendor:executorch-android:1.5.1-ldm1`
- Version basis: upstream ExecuTorch `v1.5.1`
- Upstream project: `pytorch/executorch`
- License identified from the upstream `v1.5.1` source tree: BSD-3-Clause
- Upstream copyright holder stated in the license: Meta Platforms, Inc. and affiliates

Binary redistribution under the upstream BSD-3-Clause terms requires preservation of the required copyright, license conditions, and disclaimer in the distribution documentation and/or other materials.

### Unresolved before publication

The vendored AAR and exact private SDK Maven bundle have not yet been inspected for their complete packaged third-party dependency/notice set. ExecuTorch contains third-party components, so the top-level BSD-3-Clause identification alone is not sufficient to mark notice compliance PASS.

## AndroidX / Android build dependencies

The public samples reference AndroidX components such as Room. Build-time and runtime dependency notices must be derived from the final public dependency graph and the artifacts actually distributed. No claim of a complete AndroidX notice inventory is made by this draft.

## Gate

```text
THIRD_PARTY_NOTICE_COMPLIANCE = NOT_ESTABLISHED
```

Before public visibility or binary publication, replace this staging draft with a complete notice inventory supported by the exact distributable artifacts and final public dependency graph.
