<#import "template.ftl" as layout>
<@layout.emailLayout>
    ${kcSanitize(msg("emailCodeBody", code, ttl))?no_esc}
</@layout.emailLayout>