package grailswithrestbuilder

import demo.Book

class BootStrap {

    def init = { servletContext ->
        Book book = new Book(title: "Nirav")
        book.save()
    }
    def destroy = {
    }
}
