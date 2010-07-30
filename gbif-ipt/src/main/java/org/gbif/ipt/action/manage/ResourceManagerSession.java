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

package org.gbif.ipt.action.manage;

import org.gbif.ipt.model.Resource;
import org.gbif.ipt.model.ResourceConfiguration;
import org.gbif.ipt.model.User;
import org.gbif.ipt.service.manage.ResourceManager;
import org.gbif.metadata.eml.Eml;

import com.google.inject.Inject;
import com.google.inject.servlet.SessionScoped;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * A wrapper kept in a users session when managing one resource.
 * It keeps the loaded EML and full mapping configuration in memory for quick access instead of loading them from files
 * in every request.
 * 
 * The ResourceManagerSession is initialised by the ResourceInterceptor who also does the resource authorisation.
 * 
 * @See ResourceInterceptor
 * @author markus
 * 
 */
@SessionScoped
public class ResourceManagerSession {
  private static Logger log = Logger.getLogger(ResourceManagerSession.class);
  @Inject
  private ResourceManager resourceManager;

  private Resource resource;
  private ResourceConfiguration config;
  private Eml eml;
  private User manager;

  public ResourceConfiguration getConfig() {
    return config;
  }

  public Eml getEml() {
    return eml;
  }

  public User getManager() {
    return manager;
  }

  public Resource getResource() {
    return resource;
  }

  public void load(User user, Resource resource) {
    this.manager = user;
    this.resource = resource;
    // TODO: load via resource/eml/config manager the appropiate instances
    this.eml = new Eml();
    this.config = new ResourceConfiguration();
    log.info("Loading new manager " + resource + " for user " + user.getEmail());
  }

  public void saveConfig() throws IOException {
    // TODO: implement
  }

  public void saveEml() throws IOException {
    // TODO: implement
  }

  public void saveResource() throws IOException {
    resourceManager.save(resource);
  }

}
