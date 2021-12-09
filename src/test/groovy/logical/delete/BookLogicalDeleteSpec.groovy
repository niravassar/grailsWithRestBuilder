package logical.delete

import demo.Book
import grails.gorm.DetachedCriteria
import grails.gorm.transactions.Rollback
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class BookLogicalDeleteSpec extends Specification implements DomainUnitTest<Book> {

    void setup() {
        new Book(title: "NiravBook").save(failOnError: true, flush: true)
        new Book(title: "RobBook").save(failOnError: true, flush: true)
    }

    @Rollback
    void 'test book logical delete - count'() {
        when:
        assert Book.count() == 2
        Book niravBook = Book.findByTitle('NiravBook')
        niravBook.delete()

        then:
        Book.count() == 1
    }

    @Rollback
    void 'test book logical delete - dynamicFinders'() {
        when:
        assert Book.count() == 2
        Book.findByTitle('NiravBook').delete()

        List<Book> booksFromFindAll = Book.findAll()
        List<Book> booksFromList = Book.list()
        Book bookFromFindByTitle = Book.findByTitle('NiravBook')

        then:
        booksFromFindAll.size() == 1
        booksFromList.size() == 1
        bookFromFindByTitle == null
    }

    @Rollback
    void 'test book logical delete - where'() {
        when:
        assert Book.count() == 2
        Book.findByTitle('NiravBook').delete()

        DetachedCriteria<Book> booksFromWhere = Book.where { title == "RobBook" || title == "NiravBook" }

        then:
        booksFromWhere.size() == 1
    }
}
