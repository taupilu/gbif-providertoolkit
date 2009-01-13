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

package org.gbif.provider.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.gbif.provider.model.CoreRecord;
import org.gbif.provider.model.DataResource;
import org.gbif.provider.model.ExtensionProperty;
import org.gbif.provider.model.dto.ValueListCount;
import org.gbif.provider.service.CoreRecordManager;
import org.gbif.provider.tapir.Filter;
import org.gbif.provider.util.H2Utils;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic manager for all datasource based resources that need to be registered with the routing datasource.
 * Overriden methods keep the datasource targetsource map of the active datasource registry in sync with the db.
 * @author markus
 *
 * @param <T>
 */
@Transactional(readOnly=true)
public class CoreRecordManagerHibernate<T extends CoreRecord> extends GenericResourceRelatedManagerHibernate<T> implements CoreRecordManager<T> {
	public CoreRecordManagerHibernate(Class<T> persistentClass) {
		super(persistentClass);
	}

    public T findByLocalId(final String localId, final Long resourceId) {
    	T result = null;
    	try{
    		Query query = getSession().createQuery(String.format("select core FROM %s core WHERE core.resource.id = :resourceId and core.localId = :localId", persistentClass.getName()))
						.setLong("resourceId", resourceId)
						.setString("localId", localId);
        	result = (T) query.uniqueResult();
    	} catch (NonUniqueResultException e){
    		log.debug("local ID is not unique within the resource. Corrupted cache!");
    	}
		return result;
	}

	public T get(final String guid) {
    	T result = null;
    	try{
    		Query query = getSession().createQuery(String.format("select core FROM %s core WHERE core.guid = :guid", persistentClass.getName()))
						.setParameter("guid", guid);
        	result = (T) query.uniqueResult();
    	} catch (NonUniqueResultException e){
    		log.debug("GUID is not unique. Corrupted cache!");
    	}
		return result;
	}

	@Transactional(readOnly=false)
	public void flagAllAsDeleted(DataResource resource) {
		// use DML-style HQL batch updates
		// http://www.hibernate.org/hib_docs/reference/en/html/batch.html
		Session session = getSession();
		String hqlUpdate = String.format("update %s core set core.deleted = true WHERE core.resource = :resource", persistentClass.getName());
		int count = session.createQuery( hqlUpdate )
		        .setEntity("resource", resource)
		        .executeUpdate();
		
//		ScrollableResults coreRecords = session.createQuery(String.format("select core FROM %s core WHERE core.resource.id = :resourceId", persistentClass.getName()))
//							    		.setCacheMode(CacheMode.IGNORE)
//							    		.scroll(ScrollMode.FORWARD_ONLY);
//		int count=0;
//		while ( coreRecords.next() ) {
//		    T core = (T) coreRecords.get(0);
//		    core.setDeleted(true);
//		    // no explicit save call needed???
//		    if ( ++count % 100 == 0 ) {
//		        //flush a batch of updates and release memory:
//		        session.flush();
//		        session.clear();
//		    }
//		}
		log.info(String.format("%s %s records of resource were flagged as deleted.", count, persistentClass.getName(), resource.getId()));
	}


	@Override
	public List<T> getAll(final Long resourceId) {
        return query(String.format("from %s e WHERE deleted=false and e.resource.id = :resourceId", persistentClass.getSimpleName()))
		        .setLong("resourceId", resourceId)
        		.list();
	}

	@Override
    public int count(Long resourceId) {
        return ( (Long) query(String.format("select count(e) from %s e WHERE deleted=false and e.resource.id = :resourceId", persistentClass.getSimpleName()))
        .setLong("resourceId", resourceId)
        .iterate().next() ).intValue();
	}


	public List<T> search(final Long resourceId, final String q) {
	     return null;
	}

	public List<T> getLatest(Long resourceId, int startPage, int pageSize) {
        return query(String.format("from %s e WHERE deleted=false and e.resource.id = :resourceId ORDER BY e.modified, e.id", persistentClass.getSimpleName()))
        .setLong("resourceId", resourceId)
        .setFirstResult(H2Utils.offset(startPage, pageSize))
        .setMaxResults(pageSize)
		.list();
	}	
	
    public ScrollableResults scrollResource(final Long resourceId) {
        Query query = getSession().createQuery(String.format("select core FROM %s core WHERE deleted=false and core.resource.id = :resourceId", persistentClass.getName()))
						.setParameter("resourceId", resourceId);
        return query.scroll(ScrollMode.FORWARD_ONLY);
    }      

    

    // TAPIR related inventory
    public List<ValueListCount> inventory(Long resourceId, List<ExtensionProperty> properties, Filter filter, int start, int limit) {
    	String selectHQL = buildSelect(properties);
    	String filterHQL = "";
    	if (filter !=null){
    		filterHQL = filter.toHQL();
    	}
    	String hql = String.format("select new org.gbif.provider.model.dto.ValueListCount(count(*), %s) from %s e WHERE deleted=false and e.resource.id = :resourceId %s group by %s  ORDER BY %s", selectHQL, persistentClass.getSimpleName(), filterHQL, selectHQL, selectHQL);
        return query(hql)
        .setLong("resourceId", resourceId)
        .setFirstResult(start)
        .setMaxResults(limit)
		.list();
	}
    private String buildSelect(List<ExtensionProperty> properties){
    	List<String> props = new ArrayList<String>();
    	for (ExtensionProperty prop : properties){
    		if (!prop.getExtension().isCore()){
    			throw new IllegalArgumentException("Only core properties are accepted");
    		}
    		props.add(prop.getName());
    	}
    	return StringUtils.join(props, ",");
    }
    
    // TAPIR related search
	public List<T> search(Long resourceId, Filter filter, int start, int limit) {
    	String filterHQL = "";
        return query(String.format("from %s e WHERE deleted=false and e.resource.id = :resourceId %s ORDER BY e.id", persistentClass.getSimpleName(), filterHQL))
        .setLong("resourceId", resourceId)
        .setFirstResult(start)
        .setMaxResults(limit)
		.list();
	}

}
