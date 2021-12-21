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
import spock.lang.Specification

class PaginationHandlerTests extends Specification {

    PaginationHandler handler = new PaginationHandler()

    void testAddMax() {
        given:
        AuditQuery query = Mock(AuditQuery)

        when:
        handler.addPagination(query, [max: 10])

        then:
        1 * query.setMaxResults(10)
    }

    void testCallWithoutMax() {
        given:
        AuditQuery query = Mock(AuditQuery)

        when:
        handler.addPagination(query, [:])

        then:
        0 * query.setMaxResults(_)
    }

    void testAddOffset() {
        given:
        AuditQuery query = Mock(AuditQuery)

        when:
        handler.addPagination(query, [offset: 10])

        then:
        1 * query.setFirstResult(10)
    }

    void testCallWithoutOffset() {
        given:
        AuditQuery query = Mock(AuditQuery)

        when:
        handler.addPagination(query, [:])

        then:
        0 * query.setFirstResult(_)
    }
}
