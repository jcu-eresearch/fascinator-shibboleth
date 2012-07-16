The Fascinator Shibboleth SSO Plugin
====

This project is a plugin for The Fascinator project: https://code.google.com/p/the-fascinator
though, typically, it would be used in an institutional build of RedBoX: http://code.google.com/p/redbox-mint/ .
You will need to run your servlet container behind a shibboleth enabled web server like Apache.
See:
* https://wiki.shibboleth.net/confluence/display/SHIB2/Installation+and+Configuration
* http://wiki.aaf.edu.au/tech-info/sp-install-guide

Apache example using mod_proxy_ajp:

    ProxyPass /redbox  ajp://localhost:8009/redbox
    ProxyPassReverse /redbox  ajp://localhost:8009/redbox

    <Location /redbox/default/sso/shibboleth>
        AuthType shibboleth
        ShibRequestSetting requireSession 1
        require valid-user
    </Location>


Add the following to the config/server/jetty/etc/server.xml file of your institutional build:

    <Call name="addConnector">
      <Arg>
        <New class="org.mortbay.jetty.ajp.Ajp13SocketConnector">
          <Set name="port">8009</Set>
        </New>
      </Arg>
    </Call>

In /etc/shibboleth/shobboleth2.xml add attributePrefix="AJP_" to the ApplicationDefaults element:

    <ApplicationDefaults ...
                          attributePrefix="AJP_">


To compile the fascinator-shibboleth plugin:

	#> mvn install

To enable Shibboleth ib your institutional build (when using ReDBox for example)
add the following dependency to your pom.xml:

        <dependency>
            <groupId>fascinator-shibboleth</groupId>
            <artifactId>fascinator-shibboleth</artifactId>
            <version>${shib.version}</version>
        </dependency>

You will need to add the unpack-shib-conf execution to the maven-dependency-plugin:

           <!-- 1st - Unpack Generic Mint setup -->
            <plugin>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>2.1</version>
                <executions>
                    <execution>
			.
			.
			.
                    </execution>
                    <!-- Shibboleth Resources -->
                    <execution>
                        <id>unpack-shib-conf</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>unpack</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.home}</outputDirectory>
                            <artifactItems>
                                <artifactItem>
                                    <groupId>fascinator-shibboleth</groupId>
                                    <artifactId>fascinator-shibboleth</artifactId>
                                    <classifier>redbox-config</classifier>
                                    <type>zip</type>
                                </artifactItem>
                            </artifactItems>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

And then, in the sso section of home/config/system-config.json, enable the Shibboleth plugin:

	...
	"sso": {
        	"plugins": ["Shibboleth"],
	...

