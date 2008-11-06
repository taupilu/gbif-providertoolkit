package org.gbif.provider.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gbif.provider.model.BaseObject;
import org.gbif.provider.model.Extension;
import org.gbif.provider.model.ExtensionProperty;
import org.gbif.provider.model.OccurrenceResource;
import org.gbif.provider.model.Region;
import org.gbif.provider.model.Resource;
import org.gbif.provider.model.Taxon;
import org.gbif.provider.model.TreeNode;
import org.gbif.provider.model.hibernate.IptNamingStrategy;
import org.gbif.provider.model.voc.ExtensionType;
import org.gbif.provider.service.ExtensionManager;
import org.gbif.provider.service.GenericManager;
import org.gbif.provider.service.RegionManager;
import org.gbif.provider.service.TaxonManager;
import org.gbif.provider.service.TreeNodeManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.NamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.transaction.annotation.Transactional;

public class ExtensionManagerHibernate extends GenericManagerHibernate<Extension> implements ExtensionManager {
    protected static final Log log = LogFactory.getLog(ExtensionManagerHibernate.class);
    @Autowired
	private SessionFactory sessionFactory;		
	@Autowired
	private IptNamingStrategy namingStrategy;

	public ExtensionManagerHibernate() {
	        super(Extension.class);
    }

	private Connection getConnection() throws SQLException {
		Session s = SessionFactoryUtils.getSession(sessionFactory, false);
		Connection cn = s.connection();
		return cn;
	}
	
	@Transactional(readOnly=false)
	public void installExtension(Extension extension){
		if (extension==null || extension.getName()==null){
			throw new IllegalArgumentException("Extension needs to have a name");
		}
		String table = namingStrategy.extensionTableName(extension);
		if (extension.getProperties().size()==0){
			throw new IllegalArgumentException("Extension needs to define properties");
		}

		Connection cn;
		try {
			cn = getConnection();
			// create table basics
			String ddl = String.format("CREATE TABLE IF NOT EXISTS %s (coreid bigint NOT NULL, resource_fk bigint NOT NULL)", table);
			PreparedStatement ps = cn.prepareStatement(ddl);
			try {
				ps.execute();
			}finally{
				ps.close();
			}
			// create indices
			String[] indexedColumns = {"coreid","resource_fk"};
			for (String col : indexedColumns){
				ddl = String.format("CREATE INDEX IDX%s_%s ON %s(%s)", table, col, table, col);
				ps = cn.prepareStatement(ddl);
				try {
					ps.execute();
				}finally{
					ps.close();
				}
			}
			// add columns
			for (ExtensionProperty prop : extension.getProperties()){
				if (prop!=null && prop.getName()!=null && prop.getColumnLength()>0){
					ddl = String.format("ALTER TABLE %s ADD %s VARCHAR(%s)",table, namingStrategy.propertyToColumnName(prop.getName()), prop.getColumnLength());
					ps = cn.prepareStatement(ddl);
					try {
						ps.execute();
					}finally{
						ps.close();
					}
				}else{
					log.warn("Extension property doesnt contain valid column description");
				}
			}
			// persist extension in case it isnt yet
			extension.setInstalled(true);
			save(extension);
		} catch (SQLException e) {
			extension.setInstalled(false);
			save(extension);
			e.printStackTrace();
		}
	}
	
	@Transactional(readOnly=false)
	public void removeExtension(Extension extension){
		if (extension==null || extension.getName()==null){
			throw new IllegalArgumentException("Extension needs to have a name");
		}
		String table = namingStrategy.extensionTableName(extension);

		Connection cn;
		PreparedStatement ps=null;
		try {
			cn = getConnection();
			String ddl = String.format("DROP TABLE IF EXISTS %s", table);
			ps = cn.prepareStatement(ddl);
			ps.execute();
			extension.setInstalled(false);
			save(extension);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	@Transactional(readOnly=false)
	private void removeExtensionCompletely(Extension extension) throws SQLException{
		removeExtension(extension);
		// remove extension, its properties and potentially existing property mappings
		Session session = getSession();
		for (ExtensionProperty prop : extension.getProperties()){
			// delete all existing property mappings
			String hqlUpdate = "delete PropertyMapping WHERE property = :property";
			int count = session.createQuery( hqlUpdate ).setEntity("property", prop).executeUpdate();
			log.info(String.format("Removed %s property mappings bound to extension property %s", count, prop.getQualName()));
			// remove property itself
			universalRemove(prop);
		}
		// finally remove extension
		remove(extension);
	}

	@SuppressWarnings("unchecked")
	public List<Extension> getAllInstalled(ExtensionType type) {
        return getSession().createQuery(String.format("from Extension where installed=true and type=:type"))
        	.setParameter("type", type)
        	.list();
	}
	
}
