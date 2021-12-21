package grailswithrestbuilder

import demo.Book
import net.lucasward.grails.plugin.Address

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
