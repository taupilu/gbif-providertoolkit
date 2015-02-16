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

package org.gbif.provider.webapp.action;

import org.gbif.provider.model.OccurrenceResource;

/**
 * Sets the resource type group  
 */
public class BaseOccurrenceResourceAction extends BaseResourceAction<OccurrenceResource>{
    /**
	 * Generated 
	 */
	private static final long serialVersionUID = -8397969082862865969L;

	public BaseOccurrenceResourceAction(){
    	super();
    	resourceType=OCCURRENCE;
    }

	@Override
	public void prepare() {
		resourceManager=occResourceManager;
		super.prepare();
	}
}