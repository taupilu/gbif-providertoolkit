/***************************************************************************
 * Copyright 2010 Global Biodiversity Information Facility Secretariat
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 ***************************************************************************/

package org.gbif.ipt.config;


import com.google.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author markus
 */
@RunWith(InjectingTestClassRunner.class)
public class AppConfigTest {
  @Inject
  AppConfig cfg;
  @Inject
  DataDir dd;

  @Test
  public void testTestConfig() {
    System.out.println(dd.configFile("").getAbsolutePath());
  }
}