package org.gbif.provider.service;

import java.util.Set;

import org.gbif.provider.datasource.ImportRecord;
import org.gbif.provider.model.Annotation;
import org.gbif.provider.model.ChecklistResource;
import org.gbif.provider.model.CoreRecord;
import org.gbif.provider.model.DarwinCore;
import org.gbif.provider.model.DataResource;
import org.gbif.provider.model.OccurrenceResource;
import org.gbif.provider.model.Taxon;

public interface CoreRecordFactory {
	CoreRecord build(DataResource resource, ImportRecord iRec, Set<Annotation> annotations);
	DarwinCore build(OccurrenceResource resource, ImportRecord iRec, Set<Annotation> annotations);
	Taxon build(ChecklistResource resource, ImportRecord iRec, Set<Annotation> annotations);
	CoreRecord copyPersistentProperties(CoreRecord target, CoreRecord source);
}