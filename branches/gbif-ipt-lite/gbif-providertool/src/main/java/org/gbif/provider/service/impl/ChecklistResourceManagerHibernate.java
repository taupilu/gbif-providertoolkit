package org.gbif.provider.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gbif.provider.model.ChecklistResource;
import org.gbif.provider.model.Taxon;
import org.gbif.provider.model.dto.StatsCount;
import org.gbif.provider.model.voc.Rank;
import org.gbif.provider.model.voc.StatusType;
import org.gbif.provider.service.ChecklistResourceManager;
import org.gbif.provider.service.ExtensionManager;
import org.gbif.provider.service.ExtensionPropertyManager;
import org.gbif.provider.util.StatsUtils;
import org.gbif.provider.util.ZipUtil;
import org.hibernate.ScrollableResults;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ChecklistResourceManagerHibernate extends DataResourceManagerHibernate<ChecklistResource> implements ChecklistResourceManager{
	private static final String TCS_ARCHIVE_FILENAME="tcsArchive.rdf";
	private static final String TCS_TEMPLATE="/WEB-INF/pages/tapir/model/tcsDataset.ftl";
	@Autowired
	private ExtensionManager extensionManager;
	@Autowired
	private ExtensionPropertyManager extensionPropertyManager;
	@Autowired
	private Configuration freemarkerCfg;
	
	public ChecklistResourceManagerHibernate() {
		super(ChecklistResource.class);
	}

	public List<StatsCount> taxByTaxon(Long resourceId, Rank rank) {
		String hql = "";
		List<Object[]> taxBySth;
		if (rank== null || rank.equals(Rank.TerminalTaxon)){
			// count all terminal taxa. No matter what rank. Higher, non terminal taxa have occ_count=0, so we can include them without problem
			hql = String.format("select t.id, t.label, 1   from Taxon t   where t.resource.id=:resourceId and t.type=:rank");		
	        taxBySth = getSession().createQuery(hql)
	        	.setParameter("resourceId", resourceId)
				.setParameter("rank", Rank.TerminalTaxon)
	        	.list();
		}else{
			// only select certain rank
			hql = String.format("select t.id, t.label, count(t2)   from Taxon t, Taxon t2   where t.resource.id=:resourceId and t2.resource.id=:resourceId  and t.type=:rank  and t2.lft>=t.lft and t2.rgt<=t.rgt  group by t");		
			taxBySth = getSession().createQuery(hql)
				.setParameter("resourceId", resourceId)
				.setParameter("rank", rank)
				.list();
		}
        return StatsUtils.getDataMap(taxBySth);
	}
	public String taxByTaxonPieUrl(Long resourceId, Rank rank, int width, int height, boolean title) {
		List<StatsCount> data = taxByTaxon(resourceId, rank);
		return taxByTaxonPieUrl(data, rank, width, height, title);
	}
	public String taxByTaxonPieUrl(List<StatsCount> data, Rank rank, int width, int height, boolean title) {
		assert(rank!=null);
		String titleText = null;
		if (title){
			titleText = "Terminal Taxa By "+rank.toString();
		}
        // get chart string
		data=limitDataForChart(data);
		return gpb.generatePieChartUrl(width, height, titleText, data);
	}

	
	


	public List<StatsCount> taxByRank(Long resourceId) {
		String hql = "";
		List<Object[]> taxBySth;
		// count all terminal taxa. No matter what rank. Higher, non terminal taxa have occ_count=0, so we can include them without problem
		hql = String.format("select t.taxonRank, count(t)   from Taxon t   where t.resource.id=:resourceId  group by t.taxonRank");		
        taxBySth = getSession().createQuery(hql)
        	.setParameter("resourceId", resourceId)
        	.list();
        return StatsUtils.getDataMap(taxBySth);
	}
	public String taxByRankPieUrl(Long resourceId, int width, int height, boolean title) {
		List<StatsCount> data = taxByRank(resourceId);
		return taxByRankPieUrl(data, width, height, title);
	}
	public String taxByRankPieUrl(List<StatsCount> data, int width, int height, boolean title) {
		String titleText = null;
		if (title){
			titleText = "Taxa By rank";
		}
        // get chart string
		data=limitDataForChart(data);
		return gpb.generatePieChartUrl(width, height, titleText, data);
	}

	
	
	public List<StatsCount> taxByStatus(Long resourceId, StatusType type) {
		List<Object[]> taxBySth;
		String hql = String.format("select t.%s, count(t)  from Taxon t  where t.resource.id=:resourceId and t.lft=t.rgt-1  group by t.%s", type.columnName, type.columnName);		
		taxBySth = getSession().createQuery(hql)
			.setParameter("resourceId", resourceId)
			.list();
        return StatsUtils.getDataMap(taxBySth);
	}
	public String taxByStatusPieUrl(Long resourceId, StatusType type, int width, int height, boolean title) {
		List<StatsCount> data = taxByStatus(resourceId, type);
		return taxByStatusPieUrl(data, type, width, height, title);
	}
	public String taxByStatusPieUrl(List<StatsCount> data, StatusType type, int width, int height, boolean title) {
		String titleText = null;
		if (title){
			titleText = "Terminal Taxa By "+type.toString();
		}
        // get chart string
		data=limitDataForChart(data);
		return gpb.generatePieChartUrl(width, height, titleText, data);
	}

	public ChecklistResource setResourceStats(ChecklistResource resource) {
		super.setResourceStats(resource);
		return resource;
	}

}