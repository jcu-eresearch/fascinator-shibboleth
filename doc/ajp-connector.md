Apache example using mod_proxy_ajp
=====

Apache config:

    ProxyPass /redbox  ajp://localhost:8009/redbox
    ProxyPassReverse /redbox  ajp://localhost:8009/redbox

    <Location /redbox/default/sso/shibboleth>
        AuthType shibboleth
        ShibRequestSetting requireSession 1
        require valid-user
    </Location>


Add the following to the config/server/jetty/etc/jetty.xml file of your institutional build:

    <Call name="addConnector">
      <Arg>
        <New class="org.mortbay.jetty.ajp.Ajp13SocketConnector">
          <Set name="port">8009</Set>
        </New>
      </Arg>
    </Call>

In /etc/shibboleth/shibboleth2.xml add attributePrefix="AJP_" to the ApplicationDefaults element:

    <ApplicationDefaults ...
                          attributePrefix="AJP_">

In home/config/system-config.json create the `Shibboleth` configuration section:

    "Shibboleth":{
        "use_headers": "false",
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
