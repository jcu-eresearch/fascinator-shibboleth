[Using this connection method will result is security breaches.](https://wiki.shibboleth.net/confluence/display/SHIB2/NativeSPSpoofChecking#NativeSPSpoofChecking-Apache)
====
Apache
-----
The Apache modules support request headers for backward compatibility, but environment variables are used by default in 2.x and can be turned on in 1.3.x.

Under no circumstances should you rely on the request header option other than as a temporary measure while adjusting applications to use the environment option. There are no known scenarios in which environment variables can't be used, including with Java containers, though sometimes extra effort or Apache settings may be needed. Do NOT take shortcuts with this. Do the work and use them.

If for some inexplicable reason you choose not to do this, then you may need to manually add a random spoofKey setting to the configuration yourself to avoid false alarms from the spoof detection feature. Because Apache is a multi-process web server, automatically generating a key to use isn't currently supported. Ideally, I suggest running without it for a while and only adding the setting if you have problems.

Config
----
See [NativeSPApacheConfig](https://wiki.shibboleth.net/confluence/display/SHIB2/NativeSPApacheConfig) specifically you need to add:

	ShibUseHeaders On

to youir apache config.

Apache example using HTTP Headers
=====

Apache config:

    ProxyPass /redbox http://localhost:9000/redbox
    ProxyPassReverse /redbox http://localhost:9000/redbox

    <Location /redbox/default/sso/shibboleth>
        AuthType shibboleth
        ShibRequestSetting requireSession 1
        require valid-user
        ShibUseHeaders On
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
