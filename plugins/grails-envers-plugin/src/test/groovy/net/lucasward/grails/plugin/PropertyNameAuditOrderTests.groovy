/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.lucasward.grails.plugin

import grails.testing.gorm.DataTest
import org.hibernate.envers.query.AuditQuery
import org.hibernate.envers.query.criteria.AuditProperty
import org.hibernate.envers.query.order.AuditOrder
import spock.lang.Specification

class PropertyNameAuditOrderTests extends Specification implements DataTest {

    PropertyNameAuditOrder auditOrder
    String propertyName = "name"

    @Override
    Class[] getDomainClassesToMock() {
        [Customer]
    }

    def setup() {
        auditOrder = new PropertyNameAuditOrder()
    }

    
    def testSortByPropertyNameDesc() {
      AuditOrder order = Mock(AuditOrder)
      AuditQuery query = Mock(AuditQuery)
      AuditProperty property = GroovyMock(AuditProperty, global:true)
      PropertyNameAuditOrder auditOrder1 = Spy(PropertyNameAuditOrder)
      
      when:
      assert auditOrder1
      auditOrder1.addOrder(query, [sort: propertyName, order: "desc"])
        
      then: 
      1 * auditOrder1.addOrder(*_);
      1 * property.desc();
      1 * query.addOrder(*_)
    }

    void testSortByPropertyNameAsc() {
      AuditOrder order = Mock(AuditOrder)
      AuditQuery query = Mock(AuditQuery)
      AuditProperty property = GroovyMock(AuditProperty, global:true)
      PropertyNameAuditOrder auditOrder1 = Spy(PropertyNameAuditOrder)
      
      when:
      assert auditOrder1
      auditOrder1.addOrder(query, [sort: propertyName, order: "asc"])
        
      then: 
      1 * auditOrder1.addOrder(*_);
      1 * property.asc();
      1 * query.addOrder(*_)
    }

    void testSortByAllCapsDirection() {
      AuditOrder order = Mock(AuditOrder)
      AuditQuery query = Mock(AuditQuery)
      AuditProperty property = GroovyMock(AuditProperty, global:true)
      PropertyNameAuditOrder auditOrder1 = Spy(PropertyNameAuditOrder)
      
      when:
      assert auditOrder1
      auditOrder1.addOrder(query, [sort: propertyName, order: "DESC"])
        
      then: 
      1 * auditOrder1.addOrder(*_);
      1 * property.desc();
      1 * query.addOrder(*_)
    }

    void testSortByPropertyNameDefaultOrdering() {
      AuditOrder order = Mock(AuditOrder)
      AuditQuery query = Mock(AuditQuery)
      AuditProperty property = GroovyMock(AuditProperty, global:true)
      PropertyNameAuditOrder auditOrder1 = Spy(PropertyNameAuditOrder)
      
      when:
      assert auditOrder1
      auditOrder1.addOrder(query, [sort: propertyName])
        
      then: 
      1 * auditOrder1.addOrder(*_);
      1 * property.asc();
      1 * query.addOrder(*_)
    }

    void testSortByPropertyNameWithNoParams() {
      AuditOrder order = Mock(AuditOrder)
      AuditQuery query = Mock(AuditQuery)
      AuditProperty property = GroovyMock(AuditProperty, global:true)
      PropertyNameAuditOrder auditOrder1 = Spy(PropertyNameAuditOrder)
      
      when:
      assert auditOrder1
      auditOrder1.addOrder(query, [:])
        
      then: 
      1 * auditOrder1.addOrder(*_);
      0 * property.asc();
      0 * query.addOrder(*_)
    }

    void testSortByRevisionNumberDesc() {
      AuditOrder order = Mock(AuditOrder)
      AuditQuery query = Mock(AuditQuery)
      AuditProperty property = GroovyMock(AuditProperty, global:true)
      PropertyNameAuditOrder auditOrder1 = Spy(PropertyNameAuditOrder)
      
      when:
      assert auditOrder1
      auditOrder1.addOrder(query, [sort: "revisionNumber", order: "desc"])
        
      then: 
      1 * auditOrder1.addOrder(*_);
      1 * property.desc();
      1 * query.addOrder(*_)
    }

    void testSortByRevisionType() {
      AuditOrder order = Mock(AuditOrder)
      AuditQuery query = Mock(AuditQuery)
      AuditProperty property = GroovyMock(AuditProperty, global:true)
      PropertyNameAuditOrder auditOrder1 = Spy(PropertyNameAuditOrder)
      
      when:
      assert auditOrder1
      auditOrder1.addOrder(query, [sort: "revisionType", order: "desc"])
        
      then: 
      1 * auditOrder1.addOrder(*_);
      1 * property.desc();
      1 * query.addOrder(*_)
    }

    void testSortByRevisionProperty() {
      AuditOrder order = Mock(AuditOrder)
      AuditQuery query = Mock(AuditQuery)
      AuditProperty property = GroovyMock(AuditProperty, global:true)
      PropertyNameAuditOrder auditOrder1 = Spy(PropertyNameAuditOrder)
      
      when:
      assert auditOrder1
      auditOrder1.addOrder(query, [sort: "revisionProperty.userId", order: "desc"])
        
      then: 
      1 * auditOrder1.addOrder(*_);
      1 * property.desc();
      1 * query.addOrder(*_)
    }

    /*private void withMock(Map options, Closure doIt) {
      
        AuditQuery queryMock = Mock(AuditQuery)
        AuditProperty propertyMock = Mock(AuditProperty)
        AuditOrder orderMock = Mock(AuditOrder)
        AuditEntity auditEntityMock = Mock(AuditEntity)

        propertyMock.demand.desc(0..100) {->
            return orderMock.createMock()
        }
        propertyMock.demand.asc(0..100) {->
            return orderMock.createMock()
        }
        auditEntityMock.demand.static.revisionProperty(0..100) {String prop ->
            return propertyMock.createMock()
        }
        auditEntityMock.demand.static.revisionNumber(0..100) {->
            return propertyMock.createMock()
        }
        auditEntityMock.demand.static.revisionType(0..100) {->
            return propertyMock.createMock()
        }
        auditEntityMock.demand.static.property(0..100) {String prop ->
            return propertyMock.createMock()
        }
        queryMock.demand.addOrder(0..100) {AuditOrder order -> }

        doIt(
                queryMock.createMock(),
                propertyMock.createMock(),
                orderMock.createMock(),
                auditEntityMock.createMock()
        )
    }*/
}
