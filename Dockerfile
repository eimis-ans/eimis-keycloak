ARG KEYCLOAK_VERSION=24.0.3

FROM maven:3.9.4-eclipse-temurin-17 AS java
COPY ./keycloak-2fa-email-authenticator /build
WORKDIR /build
RUN --mount=type=cache,target=/root/.m2 mvn package

FROM maven:3.9.4-eclipse-temurin-17 AS java-theme
COPY ./eimis-theme /build
WORKDIR /build
RUN --mount=type=cache,target=/root/.m2 mvn package

FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION} as builder
ENV KC_DB=postgres
# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

WORKDIR /opt/keycloak
# Add the provider JAR files to the providers directory
COPY --chown=keycloak:keycloak --from=java /build/target/keycloak-2fa-email-authenticator-1.0.0.0-SNAPSHOT.jar /opt/keycloak/providers/keycloak-2fa-email-authenticator.jar
COPY --chown=keycloak:keycloak --from=java-theme /build/target/eimis-email-theme-1.0.0.0-SNAPSHOT.jar /opt/keycloak/providers/eimis-theme.jar
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:${KEYCLOAK_VERSION}
COPY --from=builder /opt/keycloak/ /opt/keycloak/
ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
