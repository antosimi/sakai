/******************************************************************************
 * EntityPropertyTest.java - created by aaronz on Jul 24, 2007
 * 
 * Copyright 2007, 2008 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 *****************************************************************************/

package org.sakaiproject.entitybroker.impl.dao;

import org.sakaiproject.entitybroker.dao.EntityProperty;

import junit.framework.TestCase;

/**
 * Note really sure this test is needed
 * 
 * @author Aaron Zeckoski (aaronz@vt.edu)
 */
public class EntityPropertyTest extends TestCase {

   private final Long ID = Long.valueOf(1);
   private final String REF = "ref";
   private final String PREFIX = "prefix";
   private final String NAME = "name";
   private final String VALUE = "value";

   /**
    * Test method for
    * {@link org.sakaiproject.entitybroker.impl.EntityProperty#EntityProperty()}.
    */
   public void testEntityProperty() {
      EntityProperty ep = new EntityProperty();
      assertNotNull(ep);
      assertNull(ep.getId());
      // check that none of the setters fail
      ep.setId(ID);
      ep.setEntityRef(REF);
      ep.setEntityPrefix(PREFIX);
      ep.setPropertyName(NAME);
      ep.setPropertyValue(VALUE);
   }

   /**
    * Test method for
    * {@link org.sakaiproject.entitybroker.impl.EntityProperty#EntityProperty(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}.
    */
   public void testEntityPropertyStringStringStringStringArray() {
      EntityProperty ep = new EntityProperty(REF, PREFIX, NAME, VALUE);
      assertNotNull(ep);
      // check that none of the getters fail
      assertNull(ep.getId());
      assertEquals(REF, ep.getEntityRef());
      assertEquals(PREFIX, ep.getEntityPrefix());
      assertEquals(NAME, ep.getPropertyName());
      assertEquals(VALUE, ep.getPropertyValue());
   }

}
