# EIMIS Keycloak

- Fork 2FA SPI
- Dockerfile builds a keycloak image

-> test

- build kc image
- a docker compose to test
- a script to import a test realm

-> Image of a ready-to-use unconfigured keycloak

## CI

- [x] lint
- [x] build SPI
- [ ] version management
- [x] build and push KC image

## TODO

- [x] Dockerfile
- [ ] Docker compose
  - [x] build
  - [x] health checks and watch
  - [x] kc
  - [x] mailhog
  - [x] pg
  - [x] synapse
  - [x] element
  - [ ] use `watch`
- [ ] Fork project
  - [ ] clean up
  - [ ] Check license
  - [ ] readme : how to test email 2fa, fixes, badges
  - [ ] kotlin
  - [ ] tests
- [ ] import/version test realm
