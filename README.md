The Fascinator Shibboleth SSO Plugin
====

This project is a plugin for The Fascinator project: https://code.google.com/p/the-fascinator
though, typically, it would be used in an institutional build of RedBoX: http://code.google.com/p/redbox-mint/ .

Shibboleth Installation
====

These instructions explain how to install the plugin and how to configure it, however, additional work is
needed in order actually use shibboleth. Please see the following documentation for more information:
* https://wiki.shibboleth.net/confluence/display/SHIB2/Installation+and+Configuration
* http://wiki.aaf.edu.au/tech-info/sp-install-guide

For windows users you may need to incorperate elements (and get downloads) from:
* https://wiki.shibboleth.net/confluence/display/SHIB2/NativeSPWindowsApacheInstaller



Compiling
====

To compile the fascinator-shibboleth plugin:

	#> mvn install



Instutional Build
====

To enable Shibboleth in your institutional build (when using ReDBox for example)
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


Connector Configuration
====

There are two ways to configure the fascinator-shibboleth plugin:

* [AJP Connector](doc/ajp-connector.md) (recommended by shibboleth)
* [HTTP Connector](doc/http-connector.md)


Enabling the Shibboleth plugin in the ReDBoX Configuration
====

In the sso section of home/config/system-config.json, enable the Shibboleth plugin:

	.
	.
	.
	"sso": {
        	"plugins": ["Shibboleth"],
	.
	.
	.

Shibboleth
----

Add the Shibboleth configuration section:

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


Shibboleth Configuration Parameters
===

### use_headers
The `use_headers` element enables the Shibboleth plugin to process the request `HEADERS`
along with the request `ATTRIBUTES` for Shibboleth attributes. This is disabled by
default because it posses a security issue as clients can spoof the request headers.

### username_attribute
The `username_attribute` element indicates which Shibboleth attribute contains the
user's username.

### cn_attribute
The `cn_attribute` element indicates which Shibboleth attribute contains the user's
common name.

### session_attribute
The `session_attribute` element indicates which Shibboleth attribute contains the
Shibboleth session ID.

### idp_attribute
The `idp_attribute` element indicates which Shibboleth attribute contains the
Shibboleth IDP value.

### attributes
The `attributes` element contains a list of Shibboleth attributes that will be extracted
from the request and attached to the session for processing by role managers etc.

### delimiter
The `delimiter` element contains the character or string that will be used to split
attributes that have multiple values delimited by the delimiter. eg: the affiliation
attribute often contains more than on value:

        affiliation: member@edu.au;student@edu.au

###rolePlugins
the `rolePlugins` element is a list of SimpleShibbolethRoleManager IDs that will
be enabled by this configuration. eg. the config above enables the
SimpleShibbolethRoleManager with:

    "rolePlugins":["SimpleShibbolethRoleManager"]


SimpleShibbolethRoleManager
----
When the SimpleShibbolethRoleManager is enabled it will need to be configured. It expects
to find its configuration in the existing Shibboleth block, for example:

    "Shibboleth":{
        .
        .
        .
        "SimpleShibbolethRoleManager":{
            "reviewer":[
                [
                    ["affiliation","is","member@edu.au"]
                ],
                [
                    ["auEduPersonSharedToken","is","ddsdsf678hgH878786G67F7Fg"]
                ]
            ],
            "ourInstitution":[
                [
                    ["Shib-Identity-Provider","is","https://idp.example.com:8443/idp/shibboleth"]
                ]
            ]
        }
    },

The format of the SimpleShibbolethRoleManager block is:

    role:[
        [                                                       //
            [attribute, operation, rule_value],    //Rule1      // Rule Group  1
            [attribute, operation, rule_value]     //Rule2      //
        ]                                                       //
        ,
        [                                                       //
           [attribute, operation, rule_value],    //Rule3       // Rule Group 2
           [attribute, operation, rule_value]     //Rule4       //
        ]                                                       //
    ],
    role2:[
    .
    .
    .

Where

* `role` is the role that will be applied.
* `attribute` is the Shibboleth attribute attached to the session.
* `operation` is the ID of the ShibSimpleRoleOperator plugin to use.
* `rule_value` is a value passed to the operation along with the value of the `attribute` retrieved from the session.

Within a `role`'s rule groups, the results of each operation are logically `AND`ed together.
Between a `role`'s rule groups, the rule group's results are logically `OR`ed together.
The first Role above would thus be evaluated as:

    (Rule1 && Rule2) || (Rule3 && Rul4)

Development
====

There are currently two types of plugins that can be implemented to extend the functionality
of the fascinator-shibboleth plugin:

*   au.edu.jcu.fascinator.portal.sso.shibboleth.ShibbolethRoleManager
*   au.edu.jcu.fascinator.portal.sso.shibboleth.roles.simple.ShibSimpleRoleOperator

The ShibbolethRoleManager plugins allow developers to implement there own plugin for
assigning roles to users.
The ShibSimpleRoleOperator plugins allow developers to extent the functionality of the
SimpleShibbolethRoleManager by implementing new operations.
Both of these plugins use the standard Fascinator plugin mechanisms.

Logging
----
You can add the following to your `server/jetty/resources/logback.groovy` file to enable the output of the Shibboleth SSO plugin's debug messages to `${logHome}/logs/shibboleth.log`
```gorovy
appender("Shibboleth", RollingFileAppender) {
  file = "${logHome}/logs/shibboleth.log"
  rollingPolicy(TimeBasedRollingPolicy) {
    fileNamePattern = "${logHome}/logs/archives/%d{yyyy-MM}_shibboleth.zip"
    maxHistory = 30
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%d %-8X{name} %-6p %-20.20c{0} %m%n"
  }
}
```

```groovy
logger("au.edu.jcu", TRACE, ["Shibboleth"])
```

Credits
=======

This project is supported by the [Australian National Data Service (ANDS)](http://www.ands.org.au) through the National Collaborative Research Infrastructure Strategy Program and the Education Investment Fund (EIF) Super Science Initiative, as well as through the [Queensland Cyber Infrastructure Foundation (QCIF)](http://www.qcif.edu.au).

Licence
=======
All code licensed under GPLv2 - see [`LICENSE`](./LICENSE) for details.
