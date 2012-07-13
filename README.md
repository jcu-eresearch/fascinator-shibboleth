The Fascinator Shibboleth SSO Plugin
====

This project is a plguin for The Fasconator prooject: https://code.google.com/p/the-fascinator
though, typically, it would be used in an institional build of RedBoX: http://code.google.com/p/redbox-mint/
To use it, you firstly need to run mvn install:

	#> mvn install

Then in your institional build (when using ReDBox for example)

        <dependency>
            <groupId>fascinator-shibboleth</groupId>
            <artifactId>fascinator-shibboleth</artifactId>
            <version>${shib.version}</version>
        </dependency>

Then add the unpack-shib-conf execution to the maven-dependency-plugin:

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

Then in the sso section of home/config/system-config.json enable the Shibboleth plugin:

	...
	"sso": {
        	"plugins": ["Shibboleth"],
	...