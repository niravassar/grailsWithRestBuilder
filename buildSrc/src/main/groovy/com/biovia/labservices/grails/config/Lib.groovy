package com.biovia.labservices.grails.config

import groovy.transform.CompileStatic

/**
 * Centralized dependency version management for Hub + Plugins
 */
@CompileStatic
class Lib {

    /**
     * Try to keep these organized.
     *
     * Use sensible naming that makes it clear what the dependency is and who the provider is.
     * If a using multiple dependencies of the same version, break the version out and keep that at top.
     *
     */

    //<editor-fold desc="Versions">
    static final String grailsVersion = '4.0.10'
    static final String gormVersion = '7.0.8.RELEASE'

    static final String assetPipelineVersion = '3.3.4'

    //TODO RJD These should probably implicitly (rather than explicitly) be what Grails is providing us.
    // There is probably a better way than defining the version here
    static final String springVersion = '5.1.20.RELEASE'
    static final String springIntegrationVersion = '5.1.13.RELEASE'
    static final String springSecurityVersion = '5.1.13.RELEASE'
    static final String springSecurityKerberosVersion = '1.0.1.RELEASE'

    static final String hibernateVersion = '5.4.18.Final'
    static final String jacocoGradlePluginVersion = '0.8.7'
    static final String jaxbVersion = '2.3.1'
    static final String jaxbVersion_Old = '2.2.7' //TODO RJD these deps are 10 years old...
    static final String seleniumVersion = '3.14.0'
    static final String slf4jVersion = '1.7.30'

    static final String chromeDriverBinaryVersion = '2.45.0'
    static final String geckoDriverBinaryVersion = '0.24.0'
    //</editor-fold>

    //<editor-fold desc="Dependencies">

    //Apache
    static final String apacheCommonsCodec = 'commons-codec:commons-codec:1.15'
    static final String apacheCommonsIo = 'commons-io:commons-io:2.10.0'
    static final String apacheCommonsLang = 'commons-lang:commons-lang:2.6'
    static final String apacheCommonsLang3 = 'org.apache.commons:commons-lang3:3.12.0'
    //TODO Apache http components should be replaced by the HTTP 5.x series of dependencies
    static final String apacheHttpAsyncClient = 'org.apache.httpcomponents:httpasyncclient:4.1.4'
    static final String apacheHttpClient = 'org.apache.httpcomponents:httpclient:4.5.13'
    static final String apacheHttpCoreNio = 'org.apache.httpcomponents:httpcore-nio:4.4.14'
    static final String apacheJohnzonMapper = 'org.apache.johnzon:johnzon-mapper:1.2.12'
    static final String apacheLog4j = 'log4j:log4j:1.2.17'
    static final String apacheTomcatJdbc = 'org.apache.tomcat:tomcat-jdbc'

    //Dassault/Biovia
    static final String dassultRegistryClient = 'com.dassault_systemes.registry.client:registry-client:1.0'

    //Google
    static final String googleDiffUtils = 'com.googlecode.java-diff-utils:diffutils:1.3.0'
    static final String googleGson = 'com.google.code.gson:gson:2.8.6'
    static final String googleGuava = 'com.google.guava:guava:30.1.1-jre'

    //Gradle
    static final String gradleWebdriverBinariesPlugin =
            'gradle.plugin.com.github.erdi.webdriver-binaries:webdriver-binaries-gradle-plugin:2.0'

    //Grails
    static final String grailsConsole = 'org.grails:grails-console'
    static final String grailsCore = 'org.grails:grails-core'
    static final String grailsDatastoreRestClient = 'org.grails:grails-datastore-rest-client:6.1.12.RELEASE'
    static final String grailsGormTestingSupport = 'org.grails:grails-gorm-testing-support'
    static final String grailsWebTestingSupport = 'org.grails:grails-web-testing-support'
    static final String grailsGradlePlugin = 'org.grails:grails-gradle-plugin:' + grailsVersion
    static final String grailsLogging = 'org.grails:grails-logging'
    static final String grailsPluginCodecs = 'org.grails:grails-plugin-codecs'
    static final String grailsPluginDatasource = 'org.grails:grails-plugin-datasource'
    static final String grailsPluginDatabinding = 'org.grails:grails-plugin-databinding'
    static final String grailsPluginI18n = 'org.grails:grails-plugin-i18n'
    static final String grailsPluginInterceptors = 'org.grails:grails-plugin-interceptors'
    static final String grailsPluginRest = 'org.grails:grails-plugin-rest'
    static final String grailsPluginServices = 'org.grails:grails-plugin-services'
    static final String grailsPluginUrlMappings = 'org.grails:grails-plugin-url-mappings'
    static final String grailsTestMixins = 'org.grails:grails-test-mixins:3.3.0'
    static final String grailsWebBoot = 'org.grails:grails-web-boot'
    static final String grailsViewsJsonTestingSupport = 'org.grails:views-json-testing-support'

    //Grails Profiles
    static final String grailsProfilesWeb = 'org.grails.profiles:web'
    static final String grailsProfilesRestApiPlugin = 'org.grails.profiles:rest-api-plugin'
    static final String grailsProfilesWebPlugin = 'org.grails.profiles:web-plugin'

    //Grails Plugins
    static final String grailsPluginsAsync = 'org.grails.plugins:async'
    static final String grailsPluginsBrowserDetection = 'org.grails.plugins:browser-detection:3.4.0'
    static final String grailsPluginsCache = 'org.grails.plugins:cache'
    static final String grailsPluginsConverters = 'org.grails.plugins:converters:4.0.1'
    static final String grailsPluginsDatabaseMigration = 'org.grails.plugins:database-migration:3.1.0'
    static final String grailsPluginsEvents = 'org.grails.plugins:events'
    static final String grailsPluginsGeb = 'org.grails.plugins:geb'
    static final String grailsPluginsGormLogicalDelete = 'org.grails.plugins:gorm-logical-delete:2.0.0.M2' //TODO RJD Grails 3.0 plugin
    static final String grailsPluginsGsp = 'org.grails.plugins:gsp'
    static final String grailsPluginsHibernate5 = 'org.grails.plugins:hibernate5:7.1.0.M5'
    static final String grailsPluginsScaffolding = 'org.grails.plugins:scaffolding'
    static final String grailsPluginsSpringSecurityAcl = 'org.grails.plugins:spring-security-acl:4.0.0.M2'
    static final String grailsPluginsSpringSecurityCore = 'org.grails.plugins:spring-security-core:4.0.3'
    static final String grailsPluginsSpringSecurityOauth2Provider = 'org.grails.plugins:spring-security-oauth2-provider:4.0.0-RC1'
    static final String grailsPluginsViewsGradle = 'org.grails.plugins:views-gradle:2.0.4'
    static final String grailsPluginsViewsJson = 'org.grails.plugins:views-json'
    static final String grailsPluginsViewsJsonTemplates = 'org.grails.plugins:views-json-templates'

    //Hibernate
    static final String hibernateCore = 'org.hibernate:hibernate-core:' + hibernateVersion
    static final String hibernateEnvers = 'org.hibernate:hibernate-envers:' + hibernateVersion
    static final String hibernateValidator = 'org.hibernate.validator:hibernate-validator:6.0.20.Final'

    //Jakarta
    static final String jakartaMailApi = 'jakarta.mail:jakarta.mail-api:2.0.1'
    static final String jakartaXmlWsApi = 'jakarta.xml.ws:jakarta.xml.ws-api:3.0.0'

    //JavaX
    static final String javaxActivationApi = 'javax.activation:javax.activation-api:1.2.0'
    static final String javaxJaxbApi = 'javax.xml.bind:jaxb-api:' + jaxbVersion
    static final String javaxJsonApi = 'javax.json:javax.json-api:1.1.4'
    static final String javaxServletApi = 'javax.servlet:javax.servlet-api:3.0.1'
    //TODO platform-security-spring - should be 4.0.1!

    //JaxB (Old)
    static final String jaxbApi_Old = 'javax.xml.bind:jaxb-api:' + jaxbVersion_Old
    static final String jaxbCore_Old = 'com.sun.xml.bind:jaxb-core:' + jaxbVersion_Old
    static final String jaxbImpl_Old = 'com.sun.xml.bind:jaxb-impl:' + jaxbVersion_Old
    static final String jaxbXjc_Old = 'com.sun.xml.bind:jaxb-xjc:' + jaxbVersion_Old

    static final String jaxB2CommonsBasicsAnt = 'org.jvnet.jaxb2_commons:jaxb2-basics-ant:0.6.3'
    static final String jaxB2CommonsNamespacePrefix = 'org.jvnet.jaxb2_commons:jaxb2-namespace-prefix:1.1'

    //Selenium
    static final String seleniumRemoteDriver = 'org.seleniumhq.selenium:selenium-remote-driver:' + seleniumVersion
    static final String seleniumApi = 'org.seleniumhq.selenium:selenium-api:' + seleniumVersion
    static final String seleniumSupport = 'org.seleniumhq.selenium:selenium-support:' + seleniumVersion
    static final String seleniumChromeDriver = 'org.seleniumhq.selenium:selenium-chrome-driver:' + seleniumVersion
    static final String seleniumFirefoxDriver = 'org.seleniumhq.selenium:selenium-firefox-driver:' + seleniumVersion

    //Slf4j
    static final String slf4jApi = 'org.slf4j:slf4j-api:' + slf4jVersion
    static final String slf4jLog4j12 = 'org.slf4j:slf4j-log4j12:' + slf4jVersion
    static final String jclToSlf4j = 'org.slf4j:jcl-over-slf4j:' + slf4jVersion
    static final String julToSlf4j = 'org.slf4j:jul-to-slf4j:' + slf4jVersion

    //Spring Framework
    static final String springIntegrationCore = 'org.springframework.integration:spring-integration-core:' + springIntegrationVersion
    static final String springIntegrationWebsocket = 'org.springframework.integration:spring-integration-websocket:' +
            springIntegrationVersion
    static final String springLdapCore = 'org.springframework.ldap:spring-ldap-core:2.3.4.RELEASE'
    static final String springMessaging = 'org.springframework:spring-messaging:' + springVersion
    static final String springSecurityCas = 'org.springframework.security:spring-security-cas:' + springSecurityVersion
    static final String springSecurityConfig = 'org.springframework.security:spring-security-config:' + springSecurityVersion
    static final String springSecurityKerberosCore = 'org.springframework.security.kerberos:spring-security-kerberos-core:' + springSecurityKerberosVersion
    static final String springSecurityKerberosWeb = 'org.springframework.security.kerberos:spring-security-kerberos-web:' + springSecurityKerberosVersion
    static final String springSecurityLdap = 'org.springframework.security:spring-security-ldap:' + springSecurityVersion
    static final String springSecurityOauth2 = 'org.springframework.security.oauth:spring-security-oauth2:2.0.18.RELEASE'
    static final String springSecurityWeb = 'org.springframework.security:spring-security-web:' + springSecurityVersion
    static final String springTest = 'org.springframework:spring-test:' + springVersion
    static final String springWeb = 'org.springframework:spring-web:' + springVersion
    static final String springWebsocket = 'org.springframework:spring-websocket:' + springVersion
    static final String springBootDevTools = 'org.springframework.boot:spring-boot-devtools'
    static final String sprintBootStarterLogging = 'org.springframework.boot:spring-boot-starter-logging'
    static final String springBootAutoconfigure = 'org.springframework.boot:spring-boot-autoconfigure'
    static final String springBootStarterActuator = 'org.springframework.boot:spring-boot-starter-actuator'
    static final String springBootStarterTomcat = 'org.springframework.boot:spring-boot-starter-tomcat'
    static final String springBootStarterWebsocket = 'org.springframework.boot:spring-boot-starter-websocket'

    //UI
    static final String assetPipelineGradle = 'com.bertramlabs.plugins:asset-pipeline-gradle:' + assetPipelineVersion
    static final String assetPipelineGrails = 'com.bertramlabs.plugins:asset-pipeline-grails:' + assetPipelineVersion
    static final String angularTemplateAssetPipeline = 'com.craigburke.angular:angular-template-asset-pipeline:2.4.0'

    //Etc
    static final String amazonS3 = 'com.amazonaws:aws-java-sdk-s3:1.11.426'
    static final String easyMock = 'org.easymock:easymock:4.3'
    static final String jacksonDatabind = 'com.fasterxml.jackson.core:jackson-databind:2.12.3'
    static final String glassfishJson = 'org.glassfish:javax.json:1.1.4'
    static final String glassfishElImpl = 'org.glassfish.web:el-impl:2.1.2-b03'
    static final String h2Database = 'com.h2database:h2'
    static final String hamcrest = 'org.hamcrest:hamcrest:2.2'
    static final String hazelcast = 'com.hazelcast:hazelcast:4.2.1'
    static final String hazelcastHibernate = 'com.hazelcast:hazelcast-hibernate53:2.2.1'
    static final String jaxb2BasicsAnt = 'org.jvnet.jaxb2_commons:jaxb2-basics-ant:0.6.3'
    static final String jaxb2NamespacePrefix = 'org.jvnet.jaxb2_commons:jaxb2-namespace-prefix:1.1'
    static final String javaMelody = 'net.bull.javamelody:javamelody-core:1.88.0'
    static final String jettyWebApp = 'org.eclipse.jetty.aggregate:jetty-webapp:8.2.0.v20160908'
    static final String jodaTime = 'joda-time:joda-time:2.10.1' //DO NOT USE IN NEW CODE
    static final String jsonPath = 'com.jayway.jsonpath:json-path:2.5.0'
    static final String jUnit = 'junit:junit:4.12' //TODO RJD provided by Grails
    static final String liquibaseCore = 'org.liquibase:liquibase-core:3.6.1'  //TODO Stale
    static final String mariaDBJDBCDriver = 'org.mariadb.jdbc:mariadb-java-client:2.7.4'
    static final String micronautHttpClient = 'io.micronaut:micronaut-http-client:1.0.3'
    static final String micronautInjectGroovy = 'io.micronaut:micronaut-inject-groovy'
    static final String mockitoAll = 'org.mockito:mockito-all:1.10.19'
    static final String mockitoCore = 'org.mockito:mockito-core'
    static final String modeshapeCommon = 'org.modeshape:modeshape-common:5.4.1.Final'
    static final String oracleJDBC11DriverPackage = 'com.oracle.database.jdbc:ojdbc11-production:21.3.0.0'
    static final String openCsv = 'net.sf.opencsv:opencsv:2.3'
    static final String owaspDependencyCheck = 'org.owasp:dependency-check-gradle:6.3.1'
    static final String xmlUnit = 'xmlunit:xmlunit:1.6' //Update to 2.x requires code changes

    //</editor-fold>
}
