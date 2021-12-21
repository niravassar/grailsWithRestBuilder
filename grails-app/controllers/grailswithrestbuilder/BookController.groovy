package grailswithrestbuilder

import demo.Book
import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus

@Transactional
class BookController {

    static responseFormats = ['json']

    def index() {
        respond Book.list()
    }

    def where() {
        respond Book.where { title == "NiravBook" || title == "RobBook"}.list()
    }

    def delete() {
        Book niravBook = Book.findByTitle('NiravBook')
        niravBook.delete(something: 1)
        respond null, status: HttpStatus.OK
    }

    def update() {
        Book niravBook = Book.findByTitle('NiravBook')
        niravBook.deleted = "deleted-nirav"
        niravBook.markDirty('deleted')
        respond null, status: HttpStatus.OK
    }
}
