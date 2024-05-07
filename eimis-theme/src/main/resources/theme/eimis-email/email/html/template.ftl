<#macro emailLayout>
<html>
    <head>
        <style type="text/css">html { height: 100%; }
            body { background: rgb(249, 250, 251); max-width: 680px; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol"; }
            .mx_Header { border-bottom-width: 3px; border-bottom-style: solid; border-bottom-color: rgb(221, 221, 221); margin-bottom: 1rem; padding-top: 1rem; padding-bottom: 1rem; text-align: center; }
            @media screen and (max-width: 1120px) {
            body { font-size: 20px; }
            h1 { font-size: 1rem; }
            h2 { font-size: 0.9rem; }
            h3 { font-size: 0.85rem; }
            h4 { font-size: 0.8rem; }
            }
            .error { color: red; }</style>
                
            <style>pre,code,address {
            margin: 0px;
            }
            h1,h2,h3,h4,h5,h6 {
            margin-top: 0.2em;
            margin-bottom: 0.2em;
            }
            ol,ul {
            margin-top: 0em;
            margin-bottom: 0em;
            }
            blockquote {
            margin-top: 0em;
            margin-bottom: 0em;
            }
        </style>
    </head>
    <body style="overflow-wrap: break-word; -webkit-nbsp-mode: space; line-break: after-white-space;">
        <header class="mx_Header"> 
            <img src="https://raw.githubusercontent.com/eimis-ans/.github/main/assets/logo-ANS.svg" width="180" height="76" alt="[Logo ANS]"> 
        </header>
        <p>
            <#nested>
        </p>
    </body>
</html>
</#macro>
