# BUGBOT — appcharge-agent-java-public

> Watches for naming correctness, infrastructure ownership, and dead code drift in this Java SDK repo.

## Code Quality
- **OTP class misnaming**: Rename `OtpAuth*` → `OtpDeepLinkGeneration*` — OTP is not authentication.
- **Dead code from mocker**: Remove service code deleted upstream in the mocker repo to prevent divergence.

## Infrastructure
- **Dockerfile ownership**: Dockerfile should not live in this public SDK repo — clarify or remove.

## Checklist
- [ ] All `OtpAuth*` classes renamed to `OtpDeepLinkGeneration*` (models, services, DTOs, interfaces)
- [ ] Dockerfile removed or documented with clear publisher instructions
- [ ] `OfferServiceImpl` and other impls audited against mocker repo for deleted code
