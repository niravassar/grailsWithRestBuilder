package com.nanlabs.grails.plugin.logicaldelete

import grails.core.GrailsApplication
import grails.util.Holders
//import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration
import org.hibernate.MappingException

/**
 * This class is to be used in a Grails DataSource definition in order to add the logical delete Hibernate
 * Filter when Hibernate is starting up.<br/>
 * <br/>
 * Example DataSource.groovy config:
 * <pre>
 * // Don't forget the import
 * import com.nanlabs.grails.plugin.logicaldelete.HibernateFilterDomainConfiguration
 *
 * dataSource &#123;
 *   username = 'foo'
 *   password = 'bar'
 *   ...
 *   configClass = HibernateFilterDomainConfiguration
 * &#125;
 * </pre>
 * <br/>
 * This code was copied from the Grails Hibernate Filter plugin: https://github.com/burtbeckwith/grails-hibernate-filter/blob/master/src/groovy/org/grails/plugin/hibernate/filter/HibernateFilterDomainConfiguration.groovy
 */
//TODO RJD
/*
class HibernateFilterDomainConfiguration extends GrailsAnnotationConfiguration {
    private GrailsApplication grailsApp
    private boolean locked

    @Override
    void setGrailsApplication(GrailsApplication grailsApplication) {
        super.setGrailsApplication grailsApplication
        this.grailsApp = grailsApplication
    }

    @Override
    protected void secondPassCompile() throws MappingException {
        // Unlike the Hibernate Filter plugin, we call super() before checking the lock, otherwise errors seem to come about.
        super.secondPassCompile()

        if (locked) {
            return
        }

        DeleteHibernateFilterConfigurator deleteHibernateFilterConfigurator =
                (DeleteHibernateFilterConfigurator) Holders.grailsApplication.mainContext.getBean('deleteHibernateFilterConfigurator')

        deleteHibernateFilterConfigurator.doPostProcessing(this)

        locked = true
    }
}
*/