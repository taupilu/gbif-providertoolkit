/*
 * Copyright 2010 Global Biodiversity Informatics Facility.
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
 */
package org.gbif.provider.model;

import static org.apache.commons.lang.StringUtils.trimToNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.gbif.provider.model.eml.Eml;
import org.gbif.provider.model.eml.GeospatialCoverage;
import org.gbif.provider.model.eml.KeywordSet;
import org.gbif.provider.model.hibernate.Timestampable;
import org.gbif.provider.model.voc.PublicationStatus;
import org.gbif.provider.model.voc.ServiceType;
import org.gbif.provider.util.AppConfig;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.validator.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * A generic resource describing any digital, online and non digital available
 * biological resources. Only keeps the basic properties, but links to a far
 * more expressive EML file via the embedded ResourceMetadata type.
 * 
 */
@SuppressWarnings("serial")
@Entity
public class Resource implements BaseObject, Comparable<Resource>,
    Timestampable, Serializable {
  private Long id;
  @NotNull
  protected String guid = UUID.randomUUID().toString();
  // resource metadata
  protected ResourceMetadata meta = new ResourceMetadata();
  protected BBox geoCoverage;
  protected Set<String> keywords = new HashSet<String>();
  protected Map<String, String> services = new HashMap<String, String>();
  protected PublicationStatus status;
  protected String type;
  // resource meta-metadata
  protected User creator;
  protected Date created = new Date();
  protected User modifier;
  protected Date modified;
  protected String orgPassword;
  protected String orgTitle;
  protected String orgUuid;

  public int compareTo(Resource object) {
    return 0;
  }

  /**
   * @see java.lang.Object#equals(Object)
   */
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Resource)) {
      return false;
    }
    Resource rhs = (Resource) object;
    return new EqualsBuilder().append(this.modified, rhs.modified).append(
        this.created, rhs.created).append(this.creator, rhs.creator).append(
        this.getTitle(), rhs.getTitle()).append(this.modifier, rhs.modifier).append(
        this.getDescription(), rhs.getDescription()).append(this.guid, rhs.guid).append(
        this.getLink(), rhs.getLink()).append(this.id, rhs.id).isEquals();
  }

  @Transient
  public String getContactEmail() {
    return getMeta().getContactEmail();
  }

  @Transient
  public String getContactName() {
    return getMeta().getContactName();
  }

  public Date getCreated() {
    if (created == null) {
      return created;
    }
    return new Date(created.getTime());
  }

  @ManyToOne
  public User getCreator() {
    return creator;
  }

  @Transient
  public String getDescription() {
    return getMeta().getDescription();
  }

  public BBox getGeoCoverage() {
    return geoCoverage;
  }

  @Column(length = 128, unique = true)
  public String getGuid() {
    return guid;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId() {
    return id;
  }

  @CollectionOfElements(fetch = FetchType.EAGER)
  public Set<String> getKeywords() {
    return keywords;
  }

  // DELEGATE METHODS
  @Transient
  public String getLink() {
    return getMeta().getLink();
  }

  public ResourceMetadata getMeta() {
    if (meta == null) {
      meta = new ResourceMetadata();
    }
    return meta;
  }

  public Date getModified() {
    if (modified == null) {
      return modified;
    }
    return new Date(modified.getTime());
  }

  @ManyToOne
  public User getModifier() {
    return modifier;
  }

  @Column(length = 128)
  public String getOrgPassword() {
    return orgPassword;
  }

  @Column(length = 128)
  public String getOrgTitle() {
    return orgTitle;
  }

  @Column(length = 128)
  public String getOrgUuid() {
    return orgUuid;
  }

  @Transient
  public int getRecTotal() {
    return 0;
  }

  @Transient
  public String getRegistryUrl() {
    if (trimToNull(getUddiID()) != null) {
      return AppConfig.getRegistryResourceUrl() + "/" + getUddiID();
    }
    return null;
  }

  @CollectionOfElements(fetch = FetchType.LAZY)
  public Map<String, String> getServices() {
    return services;
  }

  @Transient
  public String getServiceUUID(ServiceType type) {
    return this.services.get(type.code);
  }

  public PublicationStatus getStatus() {
    return status;
  }

  @Transient
  public String getTitle() {
    return getMeta().getTitle();
  }

  @Column(length = 64)
  public String getType() {
    return type;
  }

  @Transient
  public String getUddiID() {
    return getMeta().getUddiID();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(1501230247, -1510855635).append(this.modified).append(
        this.created).append(this.creator).append(this.getTitle()).append(
        this.modifier).append(this.getDescription()).append(this.guid).append(
        this.getLink()).toHashCode();
  }

  @Transient
  public boolean isDataResource() {
    return false;
  }

  @Transient
  public boolean isDirty() {
    if (status == null) {
      return true;
    }
    return getStatus().compareTo(PublicationStatus.published) < 0;
  }

  @Transient
  public boolean isOrgRegistered() {
    return orgUuid != null && orgTitle != null && orgPassword != null;
  }

  @Transient
  public boolean isPublic() {
    if (status == null) {
      return false;
    }
    return getStatus().compareTo(PublicationStatus.modified) >= 0;
  }

  @Transient
  public boolean isRegistered() {
    return trimToNull(getUddiID()) != null;
  }

  @Deprecated
  // No references.
  public void putService(ServiceType type, String uuid) {
    if (trimToNull(uuid) != null) {
      this.services.put(type.code, trimToNull(uuid));
    } else {
      this.services.remove(type.code);
    }
  }

  public void setContactEmail(String contactEmail) {
    meta.setContactEmail(contactEmail);
  }

  public void setContactName(String contactName) {
    meta.setContactName(contactName);
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public void setDescription(String description) {
    meta.setDescription(description);
  }

  public void setDirty() {
    if (status != null && status.equals(PublicationStatus.published)) {
      this.status = PublicationStatus.modified;
    }
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setLink(String link) {
    meta.setLink(link);
  }

  public void setMeta(ResourceMetadata meta) {
    this.meta = meta;
  }

  public void setModified(Date modified) {
    this.modified = modified;
  }

  public void setModifier(User modifier) {
    this.modifier = modifier;
  }

  public void setOrgPassword(String orgPassword) {
    this.orgPassword = orgPassword;
  }

  public void setOrgTitle(String orgTitle) {
    this.orgTitle = orgTitle;
  }

  public void setOrgUuid(String orgUuid) {
    this.orgUuid = orgUuid;
  }

  @Deprecated
  // No references.
  public void setServices(Map<String, String> services) {
    this.services = services;
  }

  public void setStatus(PublicationStatus status) {
    // a not registered resource cant be uptodate
    if (status == null) {
      status = PublicationStatus.unpublished;
    } else if (status.equals(PublicationStatus.published) && !isRegistered()) {
      status = PublicationStatus.modified;
    }
    this.status = status;
  }

  public void setTitle(String title) {
    meta.setTitle(title);
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return new ToStringBuilder(this).append("created", this.created).append(
        "modified", this.modified).append("creator", this.creator).append(
        "description", this.getDescription()).append("id", this.id).append(
        "title", this.getTitle()).append("link", this.getLink()).append(
        "modifier", this.modifier).append("guid", this.guid).toString();
  }

  /**
   * updates persistent EML properties on resource based on EML values
   */
  public void updateWithMetadata(Eml eml) {
    // Contact
    String contactFirstName = eml.getContact().getFirstName();
    String contactLastName = eml.getContact().getLastName();
    if (contactFirstName == null) {
      contactFirstName = new String("");
    }
    if (contactLastName == null) {
      contactLastName = new String("");
    }
    meta.setContactName(contactFirstName.trim().concat(" ").concat(
        contactLastName.trim()));
    meta.setContactEmail(eml.getContact().getEmail());

    // keywords
    Set<String> keys = new HashSet<String>();

    for (KeywordSet kws : eml.getKeywords()) {
      keys.addAll(kws.getKeywords());
    }
    // for (TaxonomicCoverage tc : eml.getTaxonomicCoverages()) {
    // for (TaxonKeyword tk : tc.getKeywords()) {
    // keys.add(tk.getCommonName());
    // keys.add(tk.getScientificName());
    // }
    // }
    // this.keywords = keys;

    // GeoCovereage is the min min and max max of all coverages:
    List<GeospatialCoverage> gc = eml.getGeospatialCoverages();
    GeospatialCoverage min, max;
    if (gc != null && !gc.isEmpty()) {
      min = (GeospatialCoverage) Collections.min(gc,
          new Comparator<GeospatialCoverage>() {
            public int compare(GeospatialCoverage o1, GeospatialCoverage o2) {
              return o1.getBoundingCoordinates().getMin().getCoordinate().compareTo(
                  o2.getBoundingCoordinates().getMin().getCoordinate());
            }
          });
      max = (GeospatialCoverage) Collections.max(gc,
          new Comparator<GeospatialCoverage>() {
            public int compare(GeospatialCoverage o1, GeospatialCoverage o2) {
              return o1.getBoundingCoordinates().getMax().getCoordinate().compareTo(
                  o2.getBoundingCoordinates().getMax().getCoordinate());
            }
          });
      if (geoCoverage == null) {
        geoCoverage = new BBox();
      }
      geoCoverage.setMin(min.getBoundingCoordinates().getMin());
      geoCoverage.setMax(max.getBoundingCoordinates().getMax());
    }
  }

  /**
   * Persistent EML property. To change use eml.setGeoCoverage()
   * 
   * @param geoCoverage
   */
  private void setGeoCoverage(BBox geoCoverage) {
    this.geoCoverage = geoCoverage;
  }

  /**
   * Persistent EML property. To change use eml.setKeywords()
   * 
   * @param geoCoverage
   */
  private void setKeywords(Set<String> keywords) {
    this.keywords = keywords;
  }

}