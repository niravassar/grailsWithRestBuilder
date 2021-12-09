package com.nanlabs.grails.plugin.logicaldelete;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.mapping.Filterable;
import org.hibernate.mapping.PersistentClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO RJD
public class DeleteHibernateFilterConfigurator {

    private static final Logger log = LoggerFactory.getLogger(DeleteHibernateFilterConfigurator.class);

    private FilterDefinition deleteFilterDefinition;

    public void doPostProcessing(Configuration configuration) throws HibernateException {
        log.debug("-------------------Configuring logic-delete Hibernate filter----------------");
        /*
        addDeleteFilterDefinition(configuration);
        enrichLogicalDeleteClasses(configuration);
        enrichLogicalDeleteCollections(configuration);
        */
    }

    /*
    private void enrichLogicalDeleteClasses(Configuration configuration) {
        Iterator<PersistentClass> mappingIterator = configuration.getClassMappings();
        while (mappingIterator.hasNext()) {
            PersistentClass persistentClass = mappingIterator.next();
            if (mustBeProcessed(persistentClass.getMappedClass())) {
                enrichLogicalDeleteClass(persistentClass);
            }
        }
    }
     */

    /*
    private void enrichLogicalDeleteCollections(Configuration configuration) {
        Iterator<?> mappings = configuration.getCollectionMappings();
        while (mappings.hasNext()) {
            Collection collection = (Collection) mappings.next();

            // TODO: We are not handling the case of collection.isManyToOne()==true. This code is likely handling only oneToMany because in such cases Grails does not use a third joinTable to relate the two entities but uses only 2 tables for relating the two entities (with the 'many' side having a foreign key).
            // Example when this can cause a problem:  parent is NOT a @LogicalDelete domain class, children are logically deleted, retrieving the parent still shows the (logically deleted) children. Bug?
            // See also: http://www.grails.org/Many-to-Many+Mapping+without+Hibernate+XML

            if (collection.isOneToMany()) {
                Class referencedEntity = null;
                Value element = collection.getElement();
                if (element instanceof OneToMany) {
                    try {
                        referencedEntity = Class.forName(((OneToMany) element).getReferencedEntityName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new RuntimeException("Collection references an entity of unsupported type: " + element);
                }

                if (referencedEntity != null && mustBeProcessed(referencedEntity)) {
                    log.debug("Enabling delete filter for collection class {}", collection.getRole());
                    addFilter(collection);
                }
            }
        }
    }
     */

    private void enrichLogicalDeleteClass(PersistentClass persistentClass) {
        log.debug("Enabling delete filter for domain class {}", persistentClass.getClassName());
        addFilter(persistentClass);
    }

    private void addFilter(Filterable filterable) {
        String filterName = deleteFilterDefinition.getFilterName();
        String condition = deleteFilterDefinition.getDefaultFilterCondition();
        filterable.addFilter(filterName, condition, true, null, null);
    }

    /*
    private void addDeleteFilterDefinition(Configuration configuration) {
        log.debug("Defining Delete Hibernate filer ---- ");
        configuration.addFilterDefinition(deleteFilterDefinition);
    }
     */

    private boolean mustBeProcessed(Class<?> mappedClass) {
        return LogicalDeleteDomainClass.class.isAssignableFrom(mappedClass);
    }

    public void setDeleteFilterDefinition(FilterDefinition deleteFilterDefinition) {
        this.deleteFilterDefinition = deleteFilterDefinition;
    }
}
