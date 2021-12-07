package demo

import gorm.logical.delete.typetrait.StringLogicalDelete

class Person3 implements StringLogicalDelete<Person3> {
    String userName


    static mapping = {
        // the deleted property may be configured
        // like any other persistent property...
        deleted column:"delFlag"
    }
    static constraints = {
        deleted nullable: true
    }
}
