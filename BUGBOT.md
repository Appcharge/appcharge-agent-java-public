# BUGBOT — appcharge-agent-java-public
> Auto-generated from PR review analysis. Do not edit manually.
> Last updated: 2026-03-23

## Overview
The primary quality issues in this repo centre on incorrect naming of OTP-related classes (conflating authentication with deep-link generation), questions about infrastructure artefacts that don't belong in this repo, and residual dead code carried over from the mocker project. Review coverage is light — most PRs received rubber-stamp approvals with no substantive comments.

## Issue Categories

### Code Quality — Naming / Terminology
| Pattern | Affected Paths | Frequency |
|---------|----------------|-----------|
| OTP classes named as "auth" instead of "deeplink generation" — `OtpAuthResponse` / `OtpAuthenticationRequest` should be `OtpDeepLinkGenerationResponse` / `OtpDeepLinkGenerationRequest` | `src/main/java/com/appcharge/server/service/AuthService.java`, `src/main/java/com/appcharge/server/models/auth/OtpAuthenticationRequest.java` | ~3 comments |

**Example review comment:**
> "it's not otpAuthResponse, it's otpDeepLinkGenerationResponse — otp is not auth. please, change everywhere"

---

### Infrastructure / Deployment
| Pattern | Affected Paths | Frequency |
|---------|----------------|-----------|
| Dockerfile present in this public SDK repo despite CD being intended only for the mocker repo; raises questions about how publishers are expected to build and run the code | `Dockerfile` | ~2 comments |

**Example review comment:**
> "Why do we need a Dockerfile here? Didn't we say that CD will be only in the mocker repo? But then how will publishers build and run code from this repo?"

---

### Dead / Orphaned Code
| Pattern | Affected Paths | Frequency |
|---------|----------------|-----------|
| Code retained in this repo that was already removed from the mocker (the canonical reference implementation), causing divergence | `src/main/java/com/appcharge/server/service/impl/OfferServiceImpl.java` | ~1 comment |

**Example review comment:**
> "Why are we using this? I think we deleted it on the huge mocker."

---

## Most Reviewed Areas
| Directory | Comment Count | Main Theme |
|-----------|---------------|------------|
| `src/main/java/com/appcharge/server/service/` | 4 | Naming correctness, dead code |
| `Dockerfile` (repo root) | 2 | Deployment / infrastructure ownership |
| `src/main/java/com/appcharge/server/models/auth/` | 1 | Naming correctness |

## Action Items
- [ ] Rename all `OtpAuth*` classes/interfaces to `OtpDeepLinkGeneration*` across the entire codebase (service interfaces, models, implementations, and any DTOs)
- [ ] Decide the fate of the `Dockerfile` — either document clearly why it exists in this public repo and how publishers should use it, or remove it and update contributing docs accordingly
- [ ] Audit `OfferServiceImpl` (and other service impls) against the mocker repo to remove any code that has already been deleted upstream
