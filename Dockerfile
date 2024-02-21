ARG KEYCLOAK_VERSION=23.0.3

FROM gradle:8.6.0-jdk11-alpine AS java
COPY ./keycloak-2fa-email-authenticator /build
WORKDIR /build
RUN gradle build

FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION} as builder
ENV KC_DB=postgres

WORKDIR /opt/keycloak
COPY --from=java /build/build/libs/build-1.0.0.0-SNAPSHOT.jar /opt/keycloak/providers/keycloak-2fa-email-authenticator.jar
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION}
COPY --from=builder /opt/keycloak/ /opt/keycloak/
ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
