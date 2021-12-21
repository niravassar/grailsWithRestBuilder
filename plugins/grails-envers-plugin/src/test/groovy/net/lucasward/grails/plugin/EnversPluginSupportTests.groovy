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
import org.grails.datastore.mapping.model.PersistentEntity
import org.hibernate.SessionFactory
import org.hibernate.envers.DefaultRevisionEntity
import org.hibernate.envers.RevisionType
import spock.lang.Specification

class EnversPluginSupportTests extends Specification implements DataTest {

    @Override
    Class[] getDomainClassesToMock() {
        [Customer]
    }

    def testIsAudited() {
        expect:
        EnversPluginSupport.isAudited(Address)
    }

    def testIsNotAudited() {
        expect:
        !EnversPluginSupport.isAudited(State)
    }

    def testIsAuditedAtFieldLevelOnly() {
        expect:
        EnversPluginSupport.isAudited(Userr)
    }

    def testCollapseRevisions() {
        when:
        Customer user = new Customer(name: 'collapseTest');
        DefaultRevisionEntity revisionEntity = new DefaultRevisionEntity(id: 1)
        RevisionType revType = RevisionType.ADD
        def revision = [user, revisionEntity, revType]
        def collapsed = EnversPluginSupport.collapseRevision(revision)

        then:
        collapsed instanceof Customer
        collapsed.name == user.name
        collapsed.revisionEntity == revisionEntity
        collapsed.revisionType == RevisionType.ADD
    }

    def testCollapseRevisionsWithTooSmallArray() {
        when:
        EnversPluginSupport.collapseRevision([])

        then:
        thrown(IllegalArgumentException)
    }

    def testCollapseRevisionsWithTooLargeArray() {
        when:
        EnversPluginSupport.collapseRevision([1, 2, 3, 4])

        then:
        thrown(IllegalArgumentException)
    }

    def testGenerateFindAllMethods() {
        when:
        SessionFactory sessionFactory = [] as SessionFactory
        PersistentEntity entity = Customer.gormPersistentEntity
        EnversPluginSupport.generateFindAllMethods(entity, sessionFactory)//.createMock()

        then:
        Customer.metaClass.getStaticMetaMethod("findAllRevisionsByEmail", ["Email"]) != null
        Customer.metaClass.getStaticMetaMethod("findAllRevisionsByName", ["Email"]) != null
        Customer.metaClass.getStaticMetaMethod("findAllRevisionsByAddress", ["Email"]) != null
        Customer.metaClass.getStaticMetaMethod("findAllRevisionsById", ["Email"]) != null
    }

    def testGenerateAuditReaderMethods() {
        when:
        SessionFactory sessionFactory = [] as SessionFactory;
        PersistentEntity entity = Customer.gormPersistentEntity
        EnversPluginSupport.generateAuditReaderMethods(entity, sessionFactory)//sessionFactory.createMock()

        then:
        Customer.metaClass.getStaticMetaMethod("getCurrentRevision", []) != null
        Customer.metaClass.getMetaMethod("retrieveRevisions", []) != null
        Customer.metaClass.getMetaMethod("findAtRevision", [3]) != null
    }

}
