package demo

import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus

@Transactional
class BookController {

    static responseFormats = ['json']

    def index() {
        respond Book.list()
    }

    def delete() {
        Book niravBook = Book.findByTitle('NiravBook')
        niravBook.delete(something: 1)
        respond null, status: HttpStatus.OK
    }
}
