import org.hibernate.engine.spi.FilterDefinition

/**
 * Enables the logical delete Hibernate Filter for the current Hibernate session. <br/>
 * <br/>
 * This code was copied from the Grails Hibernate Filter plugin: https://github.com/burtbeckwith/grails-hibernate-filter/blob/master/grails-app/conf/HibernateFilterFilters.groovy
 */
class LogicalDeleteHibernateFilters {

    def sessionFactory
    FilterDefinition logicDeleteHibernateFilter

    def filters = {
        enableHibernateFilter(controller: '*', action: '*') {
            before = {
                def session = sessionFactory.currentSession
                session.enableFilter logicDeleteHibernateFilter.filterName
            }
        }
    }
}
