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

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * A mapping between a single extension property (concept) and a resource represented via the viewMapping.
 * A mapping can either be a fixed value (String) or the column index of the resultset generated by the viewMapping sql statement.
 * @author markus
 *
 */
@Entity
public class PropertyMapping implements BaseObject , Comparable<PropertyMapping> {
	private Long id;	
	private ExtensionMapping viewMapping;
	private ExtensionProperty property;
	private String column;
	private Transformation termTransformation;
	private String value;
	
	public static PropertyMapping newInstance(){
		PropertyMapping pm = new PropertyMapping();
		return pm;
	}
	public static PropertyMapping newInstance(ExtensionProperty property){
		PropertyMapping pm = newInstance();
		pm.setProperty(property);
		return pm;
	}
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(optional=false)
	//@JoinColumn(name="view_mapping_fk", insertable=false, updatable=false, nullable=false)
	public ExtensionMapping getViewMapping() {
		return viewMapping;
	}
	public void setViewMapping(ExtensionMapping viewMapping) {
		this.viewMapping = viewMapping;
	}
	
	@ManyToOne
	public ExtensionProperty getProperty() {
		return property;
	}
	public void setProperty(ExtensionProperty property) {
		this.property = property;
	}
	
	@ManyToOne
    public Transformation getTermTransformation() {
		return termTransformation;
	}
	public void setTermTransformation(Transformation termTransformation) {
		this.termTransformation = termTransformation;
	}
	@Transient
    public Long getTermTransformationId() {
		if (termTransformation==null){
			return null;
		}
		return termTransformation.getId();
	}
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = StringUtils.trimToNull(column);
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		if (value!=null){
			value=value.trim();
			if (value.length()<1){
				value = null;
			}
		}
		this.value=value;
	}

	
	/**
	 * Indicate wether this mapping has some true mapping content
	 * @return
	 */
	@Transient
	public boolean isEmpty(){
		if (StringUtils.trimToNull(column) == null && getValue()==null){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Natural sort order is by viewMapping, then extension property
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(PropertyMapping propMap) {
		int viewCmp = viewMapping.compareTo(propMap.viewMapping); 
		return (viewCmp != 0 ? viewCmp : property.compareTo(propMap.property));
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof PropertyMapping)) {
			return false;
		}
		PropertyMapping rhs = (PropertyMapping) object;
		return new EqualsBuilder().append(this.column, rhs.column).append(
				this.property, rhs.property).append(this.viewMapping,
				rhs.viewMapping).append(this.id, rhs.id).isEquals();
	}
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
	        int result = 17;
	        result = 31 * (property != null ? property.hashCode() : 0);
	        result = 31 * (column != null ? column.hashCode() : 0);
	        result = 31 * (value != null ? value.hashCode() : 0);
	        result = 31 * (viewMapping != null ? viewMapping.getExtension().hashCode() : 0);
	        return result;
	}
		
		
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("%s=>%s", property==null ? "null" : property.getName(), value==null ? (column==null ? "null" : column) : "#"+value);
	}


	
	
	
}