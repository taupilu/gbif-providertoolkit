/***************************************************************************
 * Copyright 2010 Global Biodiversity Information Facility Secretariat
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ***************************************************************************/

package org.gbif.ipt.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author markus
 * 
 */
public class EmlValidatorTest {
  @Test
  public void testPhone() {
    assertTrue(EmlValidator.isValidPhoneNumber("4916213056"));
    assertTrue(EmlValidator.isValidPhoneNumber("49 162 130 5624 - 0"));
    assertTrue(EmlValidator.isValidPhoneNumber("0049 (162) 130 5624 - 0"));
    assertTrue(EmlValidator.isValidPhoneNumber("+49 (162) 130 5624 - 0"));
    assertTrue(EmlValidator.isValidPhoneNumber("001/432/4342321233"));
    assertFalse(EmlValidator.isValidPhoneNumber("+49 (30) alpha"));
    assertFalse(EmlValidator.isValidPhoneNumber("3210049,33"));
    assertFalse(EmlValidator.isValidPhoneNumber("32134214."));
  }
}