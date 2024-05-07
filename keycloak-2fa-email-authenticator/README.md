# 🔒 Keycloak 2FA Email Authenticator

Originally forked from [mesutpiskin/keycloak-2fa-email-authenticator](https://github.com/mesutpiskin/keycloak-2fa-email-authenticator). Thanks ❤

Keycloak Authentication Provider implementation to get a two factor authentication with an OTP (One-time-password) send via Email (through SMTP).

This is a [keycloak SPI](https://www.keycloak.org/docs/latest/server_development/index.html).

## Usage

When logging in with this provider, you can send a verification code (OTP) to the user's e-mail address.
If you are using a different Keycloak version, don't forget to change the version in pom.xml file.

## Provider

`mvn package` will create a jar file. Or in a contained way:

```bash
docker run -it --rm --name maven-kc-2fa-email-build -v "$(pwd)":/usr/src/build -w /usr/src/build maven:3.9.4-eclipse-temurin-17 mvn clean package
```

`keycloak-2fa-email-authenticator.jar` needs to go to `/opt/keycloak/providers/` directory. Then rebuild keycloak with `./kc.sh build`

### Theme Resources

- OTP screen is configured in this SPI
- ⚠️ Email however is configured in the eimis theme SPI

## Keycloak configuration

- Don't forget to configure your realm's SMTP settings, otherwise no email will be send:
- Create new browser login authentication flow (copy the default one)
- add Email OTP flow before Username Password Form.
- back to authentication, bind the new workflow to browser flow.
