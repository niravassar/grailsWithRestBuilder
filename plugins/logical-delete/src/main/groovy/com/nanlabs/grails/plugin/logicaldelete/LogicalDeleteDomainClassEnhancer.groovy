package com.nanlabs.grails.plugin.logicaldelete

import org.hibernate.Session
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogicalDeleteDomainClassEnhancer {

    private static final Logger log = LoggerFactory.getLogger(this)

    private static final String PHYSICAL_PARAM = 'physical'
    private static String ON_DELETE_METHOD = 'onDelete'

    static void enhance(domainClasses) {
        for (domainClass in domainClasses) {
            Class clazz = domainClass.clazz

            // Classes that have been marked for Logical Delete will have had an AST transform applied to override
            // their delete() methods. AST transforms are preferred since Hibernate proxies can only be affected
            // using those means. For all other domain classes we will rely on the MetaClass changes being done here.
            if (!markedForLogicalDelete(clazz)) {
                enhanceWithOnDeleteSupport(clazz)
            }
        }
    }

    static addDisableFilterOption(Class cls, ctx) {
        cls.metaClass.static.withDeleted = { Closure closure ->
            Session session = ctx.sessionFactory.currentSession
            def filterName = "logicDeleteHibernateFilter"

            def filterModified = false
            if (session.getEnabledFilter(filterName)) {
                log.debug "Temporarily disabling Hibernate filter '$filterName'"
                session.disableFilter(filterName)
                filterModified = true
            }

            try {
                closure()
            } finally {
                if (filterModified) {
                    session.enableFilter(filterName)
                }
            }
        }
    }

    static boolean markedForLogicalDelete(clazz) {
        LogicalDeleteDomainClass.isAssignableFrom(clazz)
    }

    private static void changeDeleteMethod(clazz) {
        log.debug "Adding logic delete support to $clazz"
        def gormSaveMethod = clazz.metaClass.getMetaMethod('save')
        def gormDeleteMethod = clazz.metaClass.getMetaMethod('delete')
        def gormDeleteWithArgsMethod = clazz.metaClass.getMetaMethod('delete', Map)
        def curriedDelete = deleteAction.curry(gormSaveMethod)

        clazz.metaClass.delete = { ->
            curriedDelete(delegate)
        }

        clazz.metaClass.delete = { Map m ->
            if (m[PHYSICAL_PARAM]) {
                if (m.count { true } > 1) {
                    def args = m.dropWhile { it.key == PHYSICAL_PARAM }
                    gormDeleteWithArgsMethod.invoke(delegate, args)
                } else {
                    gormDeleteMethod.invoke(delegate)
                }
            } else {
                curriedDelete(delegate, m)
            }
        }
    }

    private static deleteAction = { aSave, aDelegate, args = null ->
        log.debug "Applying logical delete to domain class ${aDelegate.class}"
        aDelegate.deleted = true
        if (args) aSave.invoke(aDelegate) else aSave.invoke(aDelegate, args)
    }

    /**
     * Overrides the specified {@code clazz}'s delete method in order to first call its onDelete() method, if it exists,
     * and then proceed with GORM's delete process.
     *
     * @param clazz class to enhance
     */
    private static void enhanceWithOnDeleteSupport(Class clazz) {
        Closure newDelete = { Map args = null ->
            // First look for trigger to invoke (in a way "depth first").
            if (delegate.respondsTo(ON_DELETE_METHOD)) {
                delegate."$ON_DELETE_METHOD"()
            }

            currentGormInstanceApi().delete(delegate)
        }

        clazz.metaClass.delete = newDelete
    }
}
