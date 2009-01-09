/***************************************************************************
* Copyright (C) 2008 Global Biodiversity Information Facility Secretariat.
* All Rights Reserved.
*
* The contents of this file are subject to the Mozilla Public
* License Version 1.1 (the "License"); you may not use this file
* except in compliance with the License. You may obtain a copy of
* the License at http://www.mozilla.org/MPL/
*
* Software distributed under the License is distributed on an "AS
* IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
* implied. See the License for the specific language governing
* rights and limitations under the License.

***************************************************************************/

package org.gbif.provider.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.gbif.provider.model.voc.Rank;
import org.gbif.provider.model.voc.RegionType;
import org.hibernate.validator.NotNull;


/**
 * The core class for taxon occurrence records with normalised properties used by the webapp.
 * The generated property values can be derived from different extensions like DarwinCore or ABCD
 * but the ones here are used for creating most of the webapp functionality
 * @author markus
 *
 */
//@Entity
@Table(name="dwcore"
	, uniqueConstraints = {@UniqueConstraint(columnNames={"localId", "resource_fk"})}
) 

@org.hibernate.annotations.Table(appliesTo = "dwcore2", indexes = { 	
		@org.hibernate.annotations.Index(name="latitude", columnNames = { "lat" }), 
		@org.hibernate.annotations.Index(name="longitude", columnNames = { "lon" }) 
		})
public class DarwinCore implements CoreRecord, Comparable<DarwinCore>{

	// for core record
	private Long id;
	private String localId;
	@NotNull
	private String guid;
	private String link;
	private boolean isDeleted;
	private Date modified;
	@NotNull
	private OccurrenceResource resource;
	@NotNull
	private DarwinCoreExtended ext;

	// DarinCore derived fields. calculated from raw Strings
	private Point location;
	private Taxon taxon;
	private Region region;
	private Date dateCollected;
	private Integer minimumElevationInMetersAsInteger;
	private Integer maximumElevationInMetersAsInteger;
	private Integer minimumDepthInMetersAsInteger;
	private Integer maximumDepthInMetersAsInteger;
	
	// DarwinCore 1.4 elements
	private String basisOfRecord;
	private String institutionCode;
	private String collectionCode;
	private String catalogNumber;
	// Identification Elements
	private String scientificName;
	private String identificationQualifer;
	// Collecting Event Elements	
	private String collector;
	private String earliestDateCollected;
	private String latestDateCollected;
	private String dayOfYear;
	private String collectingMethod;
	private String validDistributionFlag;
	// references elements
	private String imageURL;	
	private String relatedInformation;	
	private String informationWithheld;
	private String remarks;
	// Biological Elements
	private String sex;
	private String lifeStage;
	private String attributes;

	
	public static DarwinCore newInstance(OccurrenceResource resource){
		DarwinCore dwc = new DarwinCore();
		dwc.ext = new DarwinCoreExtended();
		dwc.ext.setDwc(dwc);
		dwc.location= new Point();
		dwc.resource=resource;
		dwc.ext.setResource(resource);		
		return dwc;
	}
	public static DarwinCore newMock(OccurrenceResource resource){
		Random rnd = new Random();
		DarwinCore dwc = DarwinCore.newInstance(resource);
		// populate instance
		// set unique localId to ensure we can save this record. Otherwise we might get a non unique constraint exception...
		String guid = UUID.randomUUID().toString();
		String localId = rnd.nextInt(99999999)+"";
		dwc.setLocalId(localId);
		dwc.setGuid(guid);
		dwc.setCatalogNumber("rbgk-"+localId+"-x");
		dwc.setBasisOfRecord("PreservedSpecimen");
		dwc.setInstitutionCode("RBGK");
		// location
		dwc.setCountry("PL");
		// taxonomy
		dwc.setScientificName("Abies alba L.");
		dwc.setGenus("Abies");
		dwc.setFamily("Pinaceae");
		return dwc;
	}
	
	

	// CORE RECORD
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Transient
	public Long getCoreId() {
		return id;
	}

	@Column(length=64)
	@org.hibernate.annotations.Index(name="source_local_id")
	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}	

	@Column(length=128, unique=true)
	@org.hibernate.annotations.Index(name="guid")
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	@org.hibernate.annotations.Index(name="deleted")
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@ManyToOne
	public OccurrenceResource getResource() {
		return resource;
	}
	public void setResource(OccurrenceResource resource) {
		this.resource = resource;
	}
	@Transient
	public Long getResourceId() {
		return resource.getId();
	}
	

	// OTHER	
	// optional = false breaks hibernate IdGeneration somehow... buuh
    @OneToOne(mappedBy="dwc", fetch = FetchType.LAZY, cascade=CascadeType.ALL) 
	public DarwinCoreExtended getExt() {
    	// shouldnt be the case, but to prevend NPE create assure there is always the ext component
    	if (ext==null){
    		ext = new DarwinCoreExtended();
    		//ext.setDwc(this);    		
    	}
		return ext;
	}
	public void setExt(DarwinCoreExtended ext) {
		this.ext = ext;
	}
	
	
	@ManyToOne(optional = true)
	public Taxon getTaxon() {
		return taxon;
	}
	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	@ManyToOne(optional = true)
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	
	@org.hibernate.annotations.Index(name="date_collected")
    public Date getDateCollected() {
		return dateCollected;
	}
	public void setDateCollected(Date dateCollected) {
		this.dateCollected = dateCollected;
	}
	
	@AttributeOverrides({
        @AttributeOverride(name="latitude", column = @Column(name="lat")), 
        @AttributeOverride(name="longitude", column = @Column(name="lon")) 
    })
	public Point getLocation() {
    	// to prevent NPE create new empty point in case it doesnt exist yet
    	if (location==null){
    		location=new Point();
    	}
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	
	@Transient
	public Double getLatitude() {
		return location.getLatitude();
	}
	@Transient
	public Double getLongitude() {
		return location.getLongitude();
	}
	
	public Integer getMinimumElevationInMetersAsInteger() {
		return minimumElevationInMetersAsInteger;
	}
	public void setMinimumElevationInMetersAsInteger(Integer minimumElevationInMetersAsInteger) {
		this.minimumElevationInMetersAsInteger = minimumElevationInMetersAsInteger;
	}
	public Integer getMaximumElevationInMetersAsInteger() {
		return maximumElevationInMetersAsInteger;
	}
	public void setMaximumElevationInMetersAsInteger(Integer maximumElevationInMetersAsInteger) {
		this.maximumElevationInMetersAsInteger = maximumElevationInMetersAsInteger;
	}
	public Integer getMinimumDepthInMetersAsInteger() {
		return minimumDepthInMetersAsInteger;
	}
	public void setMinimumDepthInMetersAsInteger(Integer minimumDepthInMetersAsInteger) {
		this.minimumDepthInMetersAsInteger = minimumDepthInMetersAsInteger;
	}
	public Integer getMaximumDepthInMetersAsInteger() {
		return maximumDepthInMetersAsInteger;
	}
	public void setMaximumDepthInMetersAsInteger(Integer maximumDepthInMetersAsInteger) {
		this.maximumDepthInMetersAsInteger = maximumDepthInMetersAsInteger;
	}
	
	
	
	/**
	 * Aliases for set/getGuid() which are part of any core record, not only darwin core
	 * @return
	 */
	@Transient
	public String getGlobalUniqueIdentifier() {
		return guid;
	}
	public void setGlobalUniqueIdentifier(String globalUniqueIdentifier) {
		this.guid = globalUniqueIdentifier;
	}
	/**no need for extra date last modified. Forward to CoreRecord property
	 * @return
	 */
	@Transient
	public Date getDateLastModified() {
		return this.getModified();
	}
	@Column(length=64)
	@org.hibernate.annotations.Index(name="record_basis")
	public String getBasisOfRecord() {
		return basisOfRecord;
	}
	public void setBasisOfRecord(String basisOfRecord) {
		this.basisOfRecord = basisOfRecord;
	}
	@Column(length=128)
	@org.hibernate.annotations.Index(name="inst_code")
	public String getInstitutionCode() {
		return institutionCode;
	}
	public void setInstitutionCode(String institutionCode) {
		this.institutionCode = institutionCode;
	}
	@Column(length=128)
	@org.hibernate.annotations.Index(name="coll_code")
	public String getCollectionCode() {
		return collectionCode;
	}
	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}
	@Column(length=128)
	@org.hibernate.annotations.Index(name="cat_num")
	public String getCatalogNumber() {
		return catalogNumber;
	}
	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}
	@Lob
	public String getInformationWithheld() {
		return informationWithheld;
	}
	public void setInformationWithheld(String informationWithheld) {
		this.informationWithheld = informationWithheld;
	}
	@Lob
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(length=64)
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@Column(length=128)
	public String getLifeStage() {
		return lifeStage;
	}
	public void setLifeStage(String lifeStage) {
		this.lifeStage = lifeStage;
	}
	@Lob
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	@Lob
	public String getRelatedInformation() {
		return relatedInformation;
	}
	public void setRelatedInformation(String relatedInformation) {
		this.relatedInformation = relatedInformation;
	}

	@Lob
	public String getCollectingMethod() {
		return collectingMethod;
	}
	public void setCollectingMethod(String collectingMethod) {
		this.collectingMethod = collectingMethod;
	}
	@Column(length = 16)
	public String getValidDistributionFlag() {
		return validDistributionFlag;
	}
	public void setValidDistributionFlag(String validDistributionFlag) {
		this.validDistributionFlag = validDistributionFlag;
	}
	
	@Column(length = 64)
	public String getEarliestDateCollected() {
		return earliestDateCollected;
	}
	public void setEarliestDateCollected(String earliestDateCollected) {
		this.earliestDateCollected = earliestDateCollected;
	}
	@Column(length = 64)
	public String getLatestDateCollected() {
		return latestDateCollected;
	}
	public void setLatestDateCollected(String latestDateCollected) {
		this.latestDateCollected = latestDateCollected;
	}
	
	@Column(length=32)
	public String getDayOfYear() {
		return dayOfYear;
	}
	public void setDayOfYear(String dayOfYear) {
		this.dayOfYear = dayOfYear;
	}
	
	@Column(length = 128)
	public String getCollector() {
		return collector;
	}
	public void setCollector(String collector) {
		this.collector = collector;
	}

	@org.hibernate.annotations.Index(name="sci_name")
	public String getScientificName() {
		return scientificName;
	}
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}
	
	@Column(length=64)
	public String getIdentificationQualifer() {
		return identificationQualifer;
	}
	public void setIdentificationQualifer(String identificationQualifer) {
		this.identificationQualifer = identificationQualifer;
	}	

	
	
	//
	// Forwarding properties
	//
	
	// LOCATION FORWARDING
	@Transient
	public String getHigherGeography() {
		return getExt().getHigherGeography();
	}
	public void setHigherGeography(String higherGeography) {
		getExt().setHigherGeography(higherGeography);
	}
	@Transient
	public String getContinent() {
		return getExt().getContinent();
	}
	public void setContinent(String continent) {
		getExt().setContinent(continent);
	}
	@Transient
	public String getWaterBody() {
		return getExt().getWaterBody();
	}
	public void setWaterBody(String waterBody) {
		getExt().setWaterBody(waterBody);
	}
	@Transient
	public String getIslandGroup() {
		return getExt().getIslandGroup();
	}
	public void setIslandGroup(String islandGroup) {
		getExt().setIslandGroup(islandGroup);
	}
	@Transient
	public String getIsland() {
		return getExt().getIsland();
	}
	public void setIsland(String island) {
		getExt().setIsland(island);
	}
	@Transient
	public String getCountry() {
		return getExt().getCountry();
	}
	public void setCountry(String country) {
		getExt().setCountry(country);
	}
	@Transient
	public String getStateProvince() {
		return getExt().getStateProvince();
	}
	public void setStateProvince(String stateProvince) {
		getExt().setStateProvince(stateProvince);
	}
	@Transient
	public String getCounty() {
		return getExt().getCounty();
	}
	public void setCounty(String county) {
		getExt().setCounty(county);
	}
	@Transient
	public String getLocality() {
		return ext.getLocality();
	}
	public void setLocality(String locality) {
		getExt().setLocality(locality);
	}
	@Transient
	public String getMinimumElevationInMeters() {
		return getExt().getMinimumElevationInMeters();
	}
	public void setMinimumElevationInMeters(String minimumElevationInMeters) {
		getExt().setMinimumElevationInMeters(minimumElevationInMeters);
	}
	@Transient
	public String getMaximumElevationInMeters() {
		return getExt().getMaximumElevationInMeters();
	}
	public void setMaximumElevationInMeters(String maximumElevationInMeters) {
		getExt().setMaximumElevationInMeters(maximumElevationInMeters);
	}
	@Transient
	public String getMinimumDepthInMeters() {
		return getExt().getMinimumDepthInMeters();
	}
	public void setMinimumDepthInMeters(String minimumDepthInMeters) {
		getExt().setMinimumDepthInMeters(minimumDepthInMeters);
	}
	@Transient
	public String getMaximumDepthInMeters() {
		return getExt().getMaximumDepthInMeters();
	}
	public void setMaximumDepthInMeters(String maximumDepthInMeters) {
		getExt().setMaximumDepthInMeters(maximumDepthInMeters);
	}


	// TAXONOMY FORWARDING
	
	@Transient
	public String getHigherTaxon() {
		return getExt().getHigherTaxon();
	}
	public void setHigherTaxon(String higherTaxon) {
		getExt().setHigherTaxon(higherTaxon);
	}
	@Transient
	public String getKingdom() {
		return getExt().getKingdom();
	}
	public void setKingdom(String kingdom) {
		getExt().setKingdom(kingdom);
	}
	@Transient
	public String getPhylum() {
		return getExt().getPhylum();
	}
	public void setPhylum(String phylum) {
		getExt().setPhylum(phylum);
	}
	@Transient
	public String getClasss() {
		return getExt().getClasss();
	}
	public void setClasss(String classs) {
		getExt().setClasss(classs);
	}
	@Transient
	public String getOrder() {
		return getExt().getOrder();
	}
	public void setOrder(String order) {
		getExt().setOrder(order);
	}
	@Transient
	public String getFamily() {
		return getExt().getFamily();
	}
	public void setFamily(String family) {
		getExt().setFamily(family);
	}
	@Transient
	public String getGenus() {
		return getExt().getGenus();
	}
	public void setGenus(String genus) {
		getExt().setGenus(genus);
	}
	@Transient
	public String getSpecificEpithet() {
		return getExt().getSpecificEpithet();
	}
	public void setSpecificEpithet(String specificEpithet) {
		getExt().setSpecificEpithet(specificEpithet);
	}
	@Transient
	public String getInfraspecificRank() {
		return getExt().getInfraspecificRank();
	}
	public void setInfraspecificRank(String infraspecificRank) {
		getExt().setInfraspecificRank(infraspecificRank);
	}
	@Transient
	public String getInfraspecificEpithet() {
		return getExt().getInfraspecificEpithet();
	}
	public void setInfraspecificEpithet(String infraspecificEpithet) {
		getExt().setInfraspecificEpithet(infraspecificEpithet);
	}
	@Transient
	public String getAuthorYearOfScientificName() {
		return getExt().getAuthorYearOfScientificName();
	}
	public void setAuthorYearOfScientificName(String authorYearOfScientificName) {
		getExt().setAuthorYearOfScientificName(authorYearOfScientificName);
	}
	@Transient
	public String getNomenclaturalCode() {
		return getExt().getNomenclaturalCode();
	}
	public void setNomenclaturalCode(String nomenclaturalCode) {
		getExt().setNomenclaturalCode(nomenclaturalCode);
	}

	
	@Transient
	public String getPropertyValue(ExtensionProperty property){
		return getPropertyValue(property.getName());
	}
	@Transient
	private String getPropertyValue(String propName){
		if (propName.equals("Class")){
			propName = "Classs";
		}
		String getter = String.format("get%s", propName);
		String value = null;
		try {
			Method m = this.getClass().getMethod(getter);
			Object obj = m.invoke(this);
			if (obj!=null){
				value=obj.toString();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return value;
	}
	public boolean setPropertyValue(ExtensionProperty property, String value){
		try {
			Method m = this.getClass().getMethod(String.format("set%s", property.getName()), String.class);
			m.invoke(this, value);
		} catch (SecurityException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", this.getId())
			.append("basisOfRecord", this.basisOfRecord)
			.append("scientificName",this.getScientificName())
			.append("localID", this.getLocalId())
			.append("deleted", this.isDeleted())
			.append("institutionCode", this.institutionCode)
			.append("collectionCode",this.collectionCode)
			.append("catalogNumber",this.catalogNumber)
			.append("country",this.getCountry())
			.append("guid", this.guid)
			.toString();
	}
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof DarwinCore)) {
			return false;
		}
		DarwinCore dwc = (DarwinCore) object;
        return this.hashCode() == dwc.hashCode();		
	}
	
	/**
	 * Works on the raw imported properties and Ignores all secondary derived properties.
	 * Therefore id, deleted, lat/longAsFloat, modified,created, region & taxon are ignored in the hashing
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
        int result = 17;
        // core record
        result = 31 * result + (guid != null ? guid.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (localId != null ? localId.hashCode() : 0);
        // this dwc class
        result = 31 * result + (basisOfRecord != null ? basisOfRecord.hashCode() : 0);
        result = 31 * result + (institutionCode != null ? institutionCode.hashCode() : 0);
        result = 31 * result + (collectionCode != null ? collectionCode.hashCode() : 0);
        result = 31 * result + (catalogNumber != null ? catalogNumber.hashCode() : 0);
        result = 31 * result + (informationWithheld != null ? informationWithheld.hashCode() : 0);
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (lifeStage != null ? lifeStage.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (imageURL != null ? imageURL.hashCode() : 0);
        result = 31 * result + (relatedInformation != null ? relatedInformation.hashCode() : 0);
        // dwc location
        result = 31 * result + (getHigherGeography() != null ? getHigherGeography().hashCode() : 0);
        result = 31 * result + (getContinent() != null ? getContinent().hashCode() : 0);
        result = 31 * result + (getWaterBody() != null ? getWaterBody().hashCode() : 0);
        result = 31 * result + (getIslandGroup() != null ? getIslandGroup().hashCode() : 0);
        result = 31 * result + (getIsland() != null ? getIsland().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + (getStateProvince() != null ? getStateProvince().hashCode() : 0);
        result = 31 * result + (getCounty() != null ? getCounty().hashCode() : 0);
        result = 31 * result + (getLocality() != null ? getLocality().hashCode() : 0);
        result = 31 * result + (getMinimumElevationInMeters() != null ? getMinimumElevationInMeters().hashCode() : 0);
        result = 31 * result + (getMaximumElevationInMeters() != null ? getMaximumElevationInMeters().hashCode() : 0);
        result = 31 * result + (getMinimumDepthInMeters() != null ? getMinimumDepthInMeters().hashCode() : 0);
        result = 31 * result + (getMaximumDepthInMeters() != null ? getMaximumDepthInMeters().hashCode() : 0);
        result = 31 * result + (getCollectingMethod() != null ? getCollectingMethod().hashCode() : 0);
        result = 31 * result + (getValidDistributionFlag() != null ? getValidDistributionFlag().hashCode() : 0);
        result = 31 * result + (getEarliestDateCollected() != null ? getEarliestDateCollected().hashCode() : 0);
        result = 31 * result + (getLatestDateCollected() != null ? getLatestDateCollected().hashCode() : 0);
        result = 31 * result + (getDayOfYear() != null ? getDayOfYear().hashCode() : 0);
        result = 31 * result + (getCollector() != null ? getCollector().hashCode() : 0);
        // dwc taxonomy
        result = 31 * result + (getScientificName() != null ? getScientificName().hashCode() : 0);
        result = 31 * result + (getHigherTaxon() != null ? getHigherTaxon().hashCode() : 0);
        result = 31 * result + (getKingdom() != null ? getKingdom().hashCode() : 0);
        result = 31 * result + (getPhylum() != null ? getPhylum().hashCode() : 0);
        result = 31 * result + (getClasss() != null ? getClasss().hashCode() : 0);
        result = 31 * result + (getOrder() != null ? getOrder().hashCode() : 0);
        result = 31 * result + (getFamily() != null ? getFamily().hashCode() : 0);
        result = 31 * result + (getGenus() != null ? getGenus().hashCode() : 0);
        result = 31 * result + (getSpecificEpithet() != null ? getSpecificEpithet().hashCode() : 0);
        result = 31 * result + (getInfraspecificRank() != null ? getInfraspecificRank().hashCode() : 0);
        result = 31 * result + (getInfraspecificEpithet() != null ? getInfraspecificEpithet().hashCode() : 0);
        result = 31 * result + (getAuthorYearOfScientificName() != null ? getAuthorYearOfScientificName().hashCode() : 0);
        result = 31 * result + (getNomenclaturalCode() != null ? getNomenclaturalCode().hashCode() : 0);
        result = 31 * result + (getIdentificationQualifer() != null ? getIdentificationQualifer().hashCode() : 0);
        
        return result;
	}
	
	public int compareTo(DarwinCore myClass) {
		return new CompareToBuilder()
				.append(this.institutionCode, myClass.institutionCode)
				.append(this.collectionCode, myClass.collectionCode)
				.append(this.catalogNumber, myClass.catalogNumber)
				.append(this.getScientificName(),	myClass.getScientificName())
				.append(this.localId, myClass.localId)
				.append(this.guid,myClass.guid)
				.toComparison();
	}
	
	@Transient
	public String getHigherTaxonName(Rank rank){
		if (rank==Rank.Species){
			if (getSpecificEpithet()!=null){
				return StringUtils.trimToNull(String.format("%s %s", getGenus(), getSpecificEpithet()));
			}else{
				return null;
			}
		}else if (rank==Rank.InfraSpecies){
			if (getInfraspecificEpithet()!=null){
				return StringUtils.trimToNull(String.format("%s %s %s %s", getGenus(), getSpecificEpithet(), getInfraspecificRank(), getInfraspecificEpithet()));				
			}else{
				return null;
			}
		}else{
			return StringUtils.trimToNull(getPropertyValue(StringUtils.capitalise(rank.columnName)));
		}
	}
	/** 9 rank strings joined by | pipe symbol, starting with kingdom, ending in Genus, SpeciesEpi, InfraSpeciesEpi and finally ScientificName
	 * @return
	 */
	@Transient
	public String getTaxonomyPath() {
		return getTaxonomyPath(Rank.InfraSpecies)+"|"+getScientificName();
	}
	@Transient
	public String getTaxonomyPath(Rank rank) {
		String path = StringUtils.trimToEmpty(getKingdom());
		if (rank.compareTo(Rank.Phylum) >= 0){
			path += "|"+StringUtils.trimToEmpty(getPhylum());
		}
		if (rank.compareTo(Rank.Class) >= 0){
			path += "|"+StringUtils.trimToEmpty(getClasss());
		}
		if (rank.compareTo(Rank.Order) >= 0){
			path += "|"+StringUtils.trimToEmpty(getOrder());
		}
		if (rank.compareTo(Rank.Family) >= 0){
			path += "|"+StringUtils.trimToEmpty(getFamily());
		}
		if (rank.compareTo(Rank.Genus) >= 0){
			path += "|"+StringUtils.trimToEmpty(getGenus());
		}
		if (rank.compareTo(Rank.Species) >= 0){
			path += "|"+StringUtils.trimToEmpty(getSpecificEpithet());
		}
		if (rank.compareTo(Rank.InfraSpecies) >= 0){
			path += "|"+StringUtils.trimToEmpty(getInfraspecificEpithet());
		}
		return path;
	}
	
	@Transient
	public String getHigherGeographyName(RegionType regionType){
		return StringUtils.trimToNull(getPropertyValue(StringUtils.capitalise(regionType.columnName)));
	}
	@Transient
	public String getGeographyPath() {
		return getGeographyPath(RegionType.Locality);
	}
	@Transient
	public String getGeographyPath(RegionType regionType) {
		String path = StringUtils.trimToEmpty(getKingdom());
		if (regionType.compareTo(RegionType.Continent) >= 0){
			path += "|"+StringUtils.trimToEmpty(getContinent());
		}
		if (regionType.compareTo(RegionType.Waterbody) >= 0){
			path += "|"+StringUtils.trimToEmpty(getWaterBody());
		}
		if (regionType.compareTo(RegionType.IslandGroup) >= 0){
			path += "|"+StringUtils.trimToEmpty(getIslandGroup());
		}
		if (regionType.compareTo(RegionType.Island) >= 0){
			path += "|"+StringUtils.trimToEmpty(getIsland());
		}
		if (regionType.compareTo(RegionType.Country) >= 0){
			path += "|"+StringUtils.trimToEmpty(getCountry());
		}
		if (regionType.compareTo(RegionType.State) >= 0){
			path += "|"+StringUtils.trimToEmpty(getStateProvince());
		}
		if (regionType.compareTo(RegionType.County) >= 0){
			path += "|"+StringUtils.trimToEmpty(getCounty());
		}
		if (regionType.compareTo(RegionType.Locality) >= 0){
			path += "|"+StringUtils.trimToEmpty(getLocality());
		}		
		return path;
	}
}
