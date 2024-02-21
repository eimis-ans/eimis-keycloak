package com.mesutpiskin.keycloak.auth.email

import com.google.auto.service.AutoService
import org.keycloak.Config
import org.keycloak.authentication.Authenticator
import org.keycloak.authentication.AuthenticatorFactory
import org.keycloak.models.AuthenticationExecutionModel.Requirement
import org.keycloak.models.KeycloakSession
import org.keycloak.models.KeycloakSessionFactory
import org.keycloak.provider.ProviderConfigProperty

@AutoService(AuthenticatorFactory::class)
class EmailAuthenticatorFormFactory : AuthenticatorFactory {
    override fun getId(): String {
        return "email-authenticator"
    }

    override fun getDisplayType(): String {
        return "Email OTP"
    }

    override fun getReferenceCategory(): String {
        return "otp"
    }

    override fun isConfigurable(): Boolean {
        return true
    }

    override fun getRequirementChoices(): Array<Requirement> {
        return REQUIREMENT_CHOICES
    }

    override fun isUserSetupAllowed(): Boolean {
        return false
    }

    override fun getHelpText(): String {
        return "Email otp authenticator."
    }

    override fun getConfigProperties(): List<ProviderConfigProperty> {
        return java.util.List.of(
            ProviderConfigProperty(
                EmailConstants.CODE_LENGTH, "Code length",
                "The number of digits of the generated code.",
                ProviderConfigProperty.STRING_TYPE, EmailConstants.DEFAULT_LENGTH.toString()
            ),
            ProviderConfigProperty(
                EmailConstants.CODE_TTL,
                "Time-to-live",
                "The time to live in seconds for the code to be valid.",
                ProviderConfigProperty.STRING_TYPE,
                EmailConstants.DEFAULT_TTL.toString()
            )
        )
    }

    override fun close() {
        // NOOP
    }

    override fun create(session: KeycloakSession): Authenticator {
        return EmailAuthenticatorForm(session)
    }

    override fun init(config: Config.Scope) {
        // NOOP
    }

    override fun postInit(factory: KeycloakSessionFactory) {
        // NOOP
    }

    companion object {
        val REQUIREMENT_CHOICES = arrayOf(
            Requirement.REQUIRED, Requirement.ALTERNATIVE,
            Requirement.DISABLED
        )
    }
}
