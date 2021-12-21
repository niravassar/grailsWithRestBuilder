package grailswithrestbuilder

import demo.Address
import demo.Book

class BootStrap {

    def init = { servletContext ->
        Book.withTransaction {
            new Book(title: "NiravBook").save(failOnError: true, flush: true)
            new Book(title: "RobBook").save(failOnError: true, flush: true)
            new Address(city: "New York", zip: "76051").save(failOnError: true, flush: true)
        }

    }
    def destroy = {
    }
}
