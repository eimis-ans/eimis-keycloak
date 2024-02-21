# EIMIS Keycloak

![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/eimis-ans/eimis-keycloak/lint.yml?label=lint&logo=github)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/eimis-ans/eimis-keycloak/publish.yml?label=publish&logo=github&branch=main)
![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)
![Gitmoji](https://img.shields.io/badge/gitmoji-%20%F0%9F%98%9C%20%F0%9F%98%8D-FFDD67.svg)

This repo is meant to build and publish a customized version of [Keycloak](https://www.keycloak.org/). An ID provider used to connect to a [Matrix](https://matrix.org/) network through [Synapse](https://github.com/element-hq/synapse) server and [Element](https://element.io/) app.

The docker image is published to [Docker Hub](https://hub.docker.com/r/eimisans/eimis-keycloak). The version will be :
`v[KC_VERSION]-[EIMIS_INCREMENTAL_NUMBER]`

Included SPIs projects are present in sub directories:

- keycloak-2fa-email-authenticator which is a fork of [mesutpiskin/keycloak-2fa-email-authenticator](https://github.com/mesutpiskin/keycloak-2fa-email-authenticator)

## Try it out locally

add the following to your `/etc/hosts` file:

```text
127.0.0.1       matrix.local
127.0.0.1       idp.local
```

```bash
docker compose up -d
```

> [!TIP]
> If started with `docker compose watch`, keycloak image will be rebuilt restarted everytime a change is detected in SPIs folders

You can then access:

- Keycloak at [http://localhost:8080](http://localhost:8080)
  - login with `admin`/`admin`
  - You can then go to eimis-realm and create a user
- Element at [http://localhost:1983](http://localhost:1983)
  - Click on `EIMIS Connect` and login with the user you created in Keycloak
- Mailhog at [http://localhost:8025](http://localhost:8025)
  - You should see a e-mail sent with a code
  - Paste it in the login screen
- Synapse at [http://localhost:8008](http://localhost:8008)

So far Keycloak version is set in the Dockerfile and in SPIs dependencies and they should match.

## TODO

- [ ] Synapse db in PG

### SPI Dev

- [ ] kotlin
- [ ] tests
