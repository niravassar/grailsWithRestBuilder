package demo

import groovy.transform.CompileStatic
import myplugin.OCI

@CompileStatic
class RandomPersonService {

    String randomOciPersonName() {
        List<String> people = OCI.PEOPLE
        Collections.shuffle(people)
        people.first()
    }
}

