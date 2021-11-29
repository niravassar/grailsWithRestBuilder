package grailswithrestbuilder

import grails.gorm.transactions.Rollback
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.testing.mixin.integration.Integration
import spock.lang.Shared
import spock.lang.Specification

@Integration
@Rollback
class BookControllerIntSpec extends Specification {

    @Shared
    RestBuilder rest = new RestBuilder()

    void "test http call with restbuilder"() {
        when:"books are called"
        RestResponse resp = rest.get("http://localhost:${serverPort}/books")

        then: "check the logging events"
        resp.status == 200
        resp.responseEntity.body.toString().contains("Nirav")
    }

}
