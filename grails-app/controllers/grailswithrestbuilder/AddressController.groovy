package grailswithrestbuilder

import grails.gorm.transactions.Transactional
import net.lucasward.grails.plugin.Address
import org.springframework.http.HttpStatus

@Transactional
class AddressController {

    static responseFormats = ['json']

    def index() {
        respond Address.list()
    }

    def update() {
        Address myAddress = Address.findByCity("New York")
        myAddress.city = "Grapevine"
        respond null, status: HttpStatus.OK
    }

    def getAudit() {
        Address myAddress = Address.findByCity("Grapevine")
        respond Address.findAllRevisions()
    }
}
