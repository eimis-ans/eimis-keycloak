package com.mesutpiskin.keycloak.auth.email

import jakarta.ws.rs.core.Response
import org.jboss.logging.Logger
import org.keycloak.authentication.AuthenticationFlowContext
import org.keycloak.authentication.AuthenticationFlowError
import org.keycloak.authentication.AuthenticationFlowException
import org.keycloak.authentication.authenticators.browser.AbstractUsernameFormAuthenticator
import org.keycloak.common.util.SecretGenerator
import org.keycloak.email.EmailException
import org.keycloak.email.EmailTemplateProvider
import org.keycloak.events.Errors
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel
import org.keycloak.models.utils.FormMessage
import org.keycloak.services.messages.Messages

class EmailAuthenticatorForm(private val session: KeycloakSession) : AbstractUsernameFormAuthenticator() {

    private val log: Logger = Logger.getLogger(EmailAuthenticatorForm::class.java)

    override fun authenticate(context: AuthenticationFlowContext) {
        challenge(context, null)
    }

    override fun challenge(context: AuthenticationFlowContext, error: String, field: String): Response {
        generateAndSendEmailCode(context)
        val form = context.form().setExecution(context.execution.id)
        form.addError(FormMessage(field, error))
        val response = form.createForm("email-code-form.ftl")
        context.challenge(response)
        return response
    }

    private fun generateAndSendEmailCode(context: AuthenticationFlowContext) {
        val config = context.authenticatorConfig
        val session = context.authenticationSession
        if (session.getAuthNote(EmailConstants.CODE) != null) {
            // skip sending email code
            return
        }
        var length = EmailConstants.DEFAULT_LENGTH
        var ttl = EmailConstants.DEFAULT_TTL
        if (config != null) {
            // get config values
            length = config.config[EmailConstants.CODE_LENGTH]!!.toInt()
            ttl = config.config[EmailConstants.CODE_TTL]!!.toInt()
        }
        val code = SecretGenerator.getInstance().randomString(length, SecretGenerator.DIGITS)
        sendEmailWithCode(context.realm, context.user, code, ttl)
        session.setAuthNote(EmailConstants.CODE, code)
        session.setAuthNote(EmailConstants.CODE_TTL, java.lang.Long.toString(System.currentTimeMillis() + ttl * 1000L))
    }

    override fun action(context: AuthenticationFlowContext) {
        val userModel = context.user
        if (!enabledUser(context, userModel)) {
            // error in context is set in enabledUser/isDisabledByBruteForce
            return
        }
        val formData = context.httpRequest.decodedFormParameters
        if (formData.containsKey("resend")) {
            resetEmailCode(context)
            challenge(context, null)
            return
        }
        if (formData.containsKey("cancel")) {
            resetEmailCode(context)
            context.resetFlow()
            return
        }
        val session = context.authenticationSession
        val code = session.getAuthNote(EmailConstants.CODE)
        val ttl = session.getAuthNote(EmailConstants.CODE_TTL)
        val enteredCode = formData.getFirst(EmailConstants.CODE)
        if (enteredCode == code) {
            if (ttl.toLong() < System.currentTimeMillis()) {
                // expired
                context.event.user(userModel).error(Errors.EXPIRED_CODE)
                val challengeResponse = challenge(context, Messages.EXPIRED_ACTION_TOKEN_SESSION_EXISTS)
                context.failureChallenge(AuthenticationFlowError.EXPIRED_CODE, challengeResponse)
            } else {
                // valid
                resetEmailCode(context)
                context.success()
            }
        } else {
            // invalid
            val execution = context.execution
            if (execution.isRequired) {
                context.event.user(userModel).error(Errors.INVALID_USER_CREDENTIALS)
                val challengeResponse = challenge(context, Messages.INVALID_ACCESS_CODE)
                context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challengeResponse)
            } else if (execution.isConditional || execution.isAlternative) {
                context.attempted()
            }
        }
    }

    override fun disabledByBruteForceError(): String {
        return Messages.INVALID_ACCESS_CODE
    }

    private fun resetEmailCode(context: AuthenticationFlowContext) {
        context.authenticationSession.removeAuthNote(EmailConstants.CODE)
    }

    override fun requiresUser(): Boolean {
        return true
    }

    override fun configuredFor(session: KeycloakSession, realm: RealmModel, user: UserModel): Boolean {
        return true
    }

    override fun setRequiredActions(session: KeycloakSession, realm: RealmModel, user: UserModel) {
        // NOOP
    }

    override fun close() {
        // NOOP
    }

    private fun sendEmailWithCode(realm: RealmModel, user: UserModel, code: String, ttl: Int) {
        if (user.email == null) {
            log.warnf(
                "Could not send access code email due to missing email. realm=%s user=%s",
                realm.id,
                user.username
            )
            throw AuthenticationFlowException(AuthenticationFlowError.INVALID_USER)
        }
        val mailBodyAttributes: MutableMap<String, Any> = HashMap()
        mailBodyAttributes["username"] = user.username
        mailBodyAttributes["code"] = code
        mailBodyAttributes["ttl"] = ttl
        val realmName = if (realm.displayName != null) realm.displayName else realm.name
        val subjectParams = listOf<String>(realmName)
        try {
            val emailProvider = session.getProvider(EmailTemplateProvider::class.java)
            emailProvider.setRealm(realm)
            emailProvider.setUser(user)
            // Don't forget to add the welcome-email.ftl (html and text) template to your theme.
            emailProvider.send("emailCodeSubject", subjectParams, "code-email.ftl", mailBodyAttributes)
        } catch (eex: EmailException) {
            log.errorf(
                eex,
                "Failed to send access code email. realm=%s user=%s",
                realm.id,
                user.username
            )
        }
    }
}
