ARG KEYCLOAK_VERSION=23.0.3

FROM maven:3.9.4-eclipse-temurin-17 AS java
COPY ./keycloak-2fa-email-authenticator /build
WORKDIR /build
RUN mvn package

FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION} as builder
ENV KC_DB=postgres
# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

WORKDIR /opt/keycloak
# Add the provider JAR file to the providers directory
COPY --chown=keycloak:keycloak --from=java /build/target/keycloak-2fa-email-authenticator-1.0.0.0-SNAPSHOT.jar /opt/keycloak/providers/keycloak-2fa-email-authenticator.jar
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION}
COPY --from=builder /opt/keycloak/ /opt/keycloak/
ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
