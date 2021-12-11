package grailswithrestbuilder

import demo.Book
import grails.gorm.transactions.Rollback
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.testing.mixin.integration.Integration
import spock.lang.Shared
import spock.lang.Specification

@Integration
@Rollback
class BookControllerIntSpec extends Specification {

    @Shared
    RestBuilder rest = new RestBuilder()

    void setup() {
        new Book(title: "NiravBook").save(failOnError: true, flush: true)
        new Book(title: "RobBook").save(failOnError: true, flush: true)
    }

    void "test http call with restbuilder"() {
        when:"books are called"
        RestResponse resp = rest.get("http://localhost:${serverPort}/books")

        then: "check the logging events"
        resp.status == 200
        resp.responseEntity.body.toString().contains("Nirav")
    }

    void "test logical-delete"() {
        when:
        Book niravBook = Book.findByTitle('NiravBook')
        niravBook.delete(something: 1)
        List<Book> books = Book.list()
        def whereQuery = Book.where {
            title == "NiravBook"
        }.list()

        then:
        // these values should fail bc the query is not considering logically deleted items.
        assert books.size() == 2 // SHOULD BE 1
        assert whereQuery.size() == 1 // SHOULD BE 0
    }

}
