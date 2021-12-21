package grailswithrestbuilder

import demo.Book
import grails.gorm.transactions.Transactional

class BootStrap {

    def init = { servletContext ->
        Book.withTransaction {
            new Book(title: "NiravBook").save(failOnError: true, flush: true)
            new Book(title: "RobBook").save(failOnError: true, flush: true)
        }

    }
    def destroy = {
    }
}
