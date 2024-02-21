package com.mesutpiskin.keycloak.auth.email

import lombok.experimental.UtilityClass

@UtilityClass
class EmailConstants {
    companion object {
        var CODE = "emailCode"
        var CODE_LENGTH = "length"
        var CODE_TTL = "ttl"
        var DEFAULT_LENGTH = 6
        var DEFAULT_TTL = 300
    }
}
