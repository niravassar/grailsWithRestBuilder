package demo

import com.nanlabs.grails.plugin.logicaldelete.LogicalDelete
import grails.rest.*

@LogicalDelete
class Book {

    String title

    static constraints = {
        title blank:false, unique: true
    }
}
