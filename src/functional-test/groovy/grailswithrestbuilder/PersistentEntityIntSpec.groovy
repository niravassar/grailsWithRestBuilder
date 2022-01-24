package grailswithrestbuilder

import demo.Book
import grails.core.GrailsApplication
import grails.gorm.transactions.Rollback
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.testing.mixin.integration.Integration
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.orm.hibernate.cfg.GrailsDomainBinder
import org.grails.orm.hibernate.cfg.Mapping
import spock.lang.Shared
import spock.lang.Specification

@Integration
@Rollback
class PersistentEntityIntSpec extends Specification {

    GrailsApplication grailsApplication

    void "test grails GrailsDomainBinder"() {
        when:
        PersistentEntity bookEntity = grailsApplication.mappingContext.getPersistentEntity("demo.Book")
        Mapping mappingFromPersistenEntity = GrailsDomainBinder.getMapping(bookEntity)
        Mapping mappingFromClazz = GrailsDomainBinder.getMapping(Book)

        then:
        mappingFromClazz.getTableName() == "nirav_book"
        mappingFromPersistenEntity.getTableName() == "nirav_book"
    }

}
