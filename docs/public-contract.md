# LDM Public Contract

This document describes the consumer-facing contract exercised by the sanitized Android samples in this private staging repository. It does not expose model internals, frozen PTE resources, internal encoders, conformance APIs, or validation evidence.

## Contract version

`1.0`

## Boundary

Applications send semantic state and semantic candidate availability to the LDM public runtime boundary. The runtime returns a decision outcome or a public error outcome.

The application owns:

- collection of raw application/OS state;
- semantic candidate availability;
- real application side effects;
- OS permission enforcement;
- deterministic safe fallback after ABSTAIN or ERROR.

The runtime owns:

- canonical feature encoding;
- canonical candidate encoding and masking;
- model inference;
- calibration;
- semantic out-of-distribution handling;
- abstention;
- final LDM candidate selection.

Applications must not construct the internal feature vector, normalize model inputs, recalibrate probabilities, override OOD/abstention, perform a local argmax, or substitute a different LDM candidate after a DECISION.

## Candidate semantics

Candidate identity belongs to the semantic candidate ID, not array position.

Unavailable candidates are supplied as unavailable to the public request. The application does not rebuild the runtime mask itself.

### `task_priority`

Canonical candidates:

- `critical`
- `high`
- `low`
- `medium`

Application fallback used by the sample after ABSTAIN, ERROR, or an application-side range failure: `medium`, with provenance `APPLICATION_FALLBACK`.

Semantic state fields exercised by the sample:

| Field | Presence | Meaning in the sample |
| --- | --- | --- |
| `deadline_minutes` | conditional | Minutes until due time; `0` when due now/overdue |
| `overdue_minutes` | always | Minutes overdue; `0` when not overdue |
| `importance` | always | Application importance score |
| `dependency_count` | optional | Number of dependencies |
| `blocking_others` | always | Whether the task blocks other work |
| `estimated_duration_min` | optional | Estimated duration in minutes |
| `business_impact` | always | Application business-impact score |
| `safety_impact` | always | Application safety-impact score |
| `previous_delays` | always | Number of previous delays |
| `required_resources_ready` | optional | Whether required resources are ready |

The sample guards these numeric ranges before the runtime call:

- `deadline_minutes`: `0..10080`
- `overdue_minutes`: `0..10080`
- `importance`: `0..1`
- `dependency_count`: `0..20`
- `estimated_duration_min`: `1..480`
- `business_impact`: `0..1`
- `safety_impact`: `0..1`
- `previous_delays`: `0..10`

### `notification_timing`

Canonical candidates:

- `delay`
- `show_now`
- `silent`

Application fallback used by the sample after ABSTAIN, ERROR, or an application-side range failure: `silent`, with provenance `APPLICATION_FALLBACK`.

Semantic state fields exercised by the sample:

| Field | Presence | Source |
| --- | --- | --- |
| `hour_of_day` | always | Local hour |
| `screen_on` | always | Android interactive state |
| `activity_type` | always | Application input |
| `notification_count_10m` | always | Local notification history |
| `notification_count_60m` | always | Local notification history |
| `task_urgency` | always | Application input |
| `task_age_minutes` | always | Derived from creation time |
| `previously_delayed` | always | Application input |
| `user_active` | optional | Omitted when unknown |
| `deadline_minutes` | optional | Application input |
| `device_in_dnd` | optional | Included only when Android policy access permits observation |
| `headphones_connected` | optional/unknown in this sample | Not guessed by the sample when unknown |

The sample explicitly guards the collected runtime-state ranges that can exceed the frozen input envelope during normal device use:

- `notification_count_10m`: `0..20`
- `notification_count_60m`: `0..100`
- `task_age_minutes`: `0..1440`

Input construction also constrains:

- `task_urgency`: `0..1`
- `deadline_minutes`: missing or `0..1440`
- `previously_delayed`: `0..10`
- `activity_type`: `stationary`, `walking`, `driving`, or `sleeping`

Unknown optional semantic values are omitted rather than guessed.

## Public outcome handling

Consumers distinguish these cases:

1. **DECISION** — use the runtime-selected available canonical candidate.
2. **ABSTAIN** — the application applies its own deterministic fallback and records application fallback provenance.
3. **ERROR** — the application applies its own deterministic fallback and keeps the runtime error separate from the application fallback.
4. **Application-side invalid state** — do not call the runtime with a state the application already knows is outside the accepted semantic range; use the application fallback boundary instead.

LDM and application fallback provenance must remain distinguishable.

## Distribution status

The public contract can be documented independently of SDK binary publication. The SDK binary remains blocked from public publication until redistribution-rights and third-party-notice gates are established. See `distribution.md`.
