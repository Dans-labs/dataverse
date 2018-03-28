package edu.harvard.iq.dataverse.pidprovider.contract;

import java.util.List;

public interface IMetadataField {

	boolean isLeaf();

	boolean isNode();

	String getIdentifier();

	String getValue();
	
	List<String> getValues();

	int childrenCount();

	List<IMetadataField> getChidren();

	IMetadataField getChild(int idx);

	IMetadataField getChild(String identifier);

	boolean childExists(String identifier);

}