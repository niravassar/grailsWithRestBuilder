package logical.delete

import com.nanlabs.grails.plugin.logicaldelete.DeleteHibernateFilterConfigurator
import com.nanlabs.grails.plugin.logicaldelete.LogicalDeleteDomainClassEnhancer
import grails.plugins.Plugin
import org.hibernate.engine.spi.FilterDefinition

import static com.nanlabs.grails.plugin.logicaldelete.GLogicalDeleteASTTransformation.NOT_DELETED_COLUMN_VALUE

class LogicalDeleteGrailsPlugin extends Plugin {
    //Plugin version numbers are controlled by the plugin-versions.json file and Jenkins uses this. The following
    //	line is to make that obvious. Don't change it, it will be ignored
    def version = "0.0.0.1" // added by set-version

    def groupId = "com.accelrys.plugins.grails"
    def grailsVersion = "2.0 > *"
    def title = "Logical Delete Plugin"
    def description = 'Allows you to do a logical deletion of domain classes'
    def documentation = "http://grails.org/plugin/logical-delete"
    def loadAfter = ['hibernate']

    def license = "APACHE"
    def organization = [name: "NaN Labs", url: "http://www.nan-labs.com/"]
    def developers = [
            [name: "Ezequiel Parada", email: "ezequiel@nan-labs.com"]
    ]
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/nanlabs/logical-delete/issues']
    def scm = [url: 'https://github.com/nanlabs/logical-delete']

    def doWithSpring = {
        def filterName = 'logicDeleteHibernateFilter'
        def defaultCondition = "'$NOT_DELETED_COLUMN_VALUE' = deleted"
        def paramTypes = [:]

        logicDeleteHibernateFilter(FilterDefinition, filterName, defaultCondition, paramTypes)

        deleteHibernateFilterConfigurator(DeleteHibernateFilterConfigurator) {
            deleteFilterDefinition = ref('logicDeleteHibernateFilter')
        }
    }

    def doWithDynamicMethods = { ctx ->
        LogicalDeleteDomainClassEnhancer.enhance(application.domainClasses)

        application.controllerClasses.each {
            LogicalDeleteDomainClassEnhancer.addDisableFilterOption(it.clazz, ctx)
        }

        application.serviceClasses.each {
            LogicalDeleteDomainClassEnhancer.addDisableFilterOption(it.clazz, ctx)
        }
    }
}
