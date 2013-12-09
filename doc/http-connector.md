Apache example using HTTP
=====

Apache config:

    ProxyPass /redbox http://localhost:9000/redbox
    ProxyPassReverse /redbox http://localhost:9000/redbox

    <Location /redbox/default/sso/shibboleth>
        AuthType shibboleth
        ShibRequestSetting requireSession 1
        require valid-user
    </Location>


In `home/system-config.json` create the `Shibboleth` configuration section:

    "Shibboleth":{
        "use_headers": "true",
        "username_attribute":"eppn",
        "cn_attribute":"cn",
        "session_attribute":"Shib-Session-ID",
        "idp_attribute":"Shib-Identity-Provider",
        "attributes":["affiliation"],
        "delimiter":";",
        "rolePlugins":["SimpleShibbolethRoleManager"],
        .
        .
        .
     }
