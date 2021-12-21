package envers

import grails.core.GrailsApplication
import grails.plugins.Plugin
import net.lucasward.grails.plugin.EnversPluginSupport
import net.lucasward.grails.plugin.RevisionsOfEntityQueryMethod
import org.hibernate.SessionFactory

class EnversGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "4.0.0 > *"
    
    // the other plugins this plugin depends on
    def observe = ['hibernate']
    def loadAfter = ['hibernate']

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        "grails-app/domain/**",
         "src/main/groovy/net/lucasward/grails/plugin/StubSpringSecurityService.groovy",
         "src/main/groovy/net/lucasward/grails/plugin/SpringSecurityRevisionListener.groovy",
         "src/main/groovy/net/lucasward/grails/plugin/SpringSecurityServiceHolder.groovy",
         "src/test/groovy/net/lucasward/grails/plugin/Book.java",
         "src/test/groovyjava/net/lucasward/grails/plugin/UserRevisionEntity.java",
         "grails-app/conf/hibernate/hibernate.cfg.xml",
         "web-app/**"
    ]

    def title = 'Grails Envers Plugin' // Headline display name of the plugin
    def author = 'Lucas Ward, Jay Hogan, Colin Harrington'
    def authorEmail = ""
    def description = '''\
Plugin to integrate grails with Hibernate Envers
'''

    // URL to the plugin's documentation
    def documentation = "https://github.com/mattmoss/grails-envers-plugin/blob/master/README.md"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
    def developers = [ [ name: "Matthew Moss", email: "mossm@objectcomputing.com" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "GITHUB", url: "https://github.com/mattmoss/grails-envers-plugin/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/mattmoss/grails-envers-plugin/" ]

    Closure doWithSpring() { {->
            // TODO Implement runtime spring config (optional)
        }
    }

    void doWithDynamicMethods() {
      for (entry in applicationContext.getBeansOfType(SessionFactory)) {
        SessionFactory sessionFactory = entry.value
        registerDomainMethods(grailsApplication, sessionFactory)
      }
    }
    
    private void registerDomainMethods(GrailsApplication application, SessionFactory sessionFactory) {
        application.mappingContext.persistentEntities.each { entity ->
            if (EnversPluginSupport.isAudited(entity.javaClass)) {

                MetaClass mc = entity.javaClass.getMetaClass()
                def getAllRevisions = new RevisionsOfEntityQueryMethod(sessionFactory, entity.javaClass)

                mc.static.findAllRevisions = {
                    getAllRevisions.query(null, null, [:])
                }

                mc.static.findAllRevisions = { Map parameters ->
                    getAllRevisions.query(null, null, parameters)
                }

                EnversPluginSupport.generateFindAllMethods(entity, sessionFactory)
                EnversPluginSupport.generateAuditReaderMethods(entity, sessionFactory)
            }
        }
    }

    void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
