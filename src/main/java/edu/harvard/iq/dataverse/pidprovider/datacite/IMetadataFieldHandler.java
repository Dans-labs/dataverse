package edu.harvard.iq.dataverse.pidprovider.datacite;

import edu.harvard.iq.dataverse.pidprovider.contract.IMetadataField;
import edu.harvard.iq.dataverse.pidprovider.contract.InsufficientMetadataException;


public interface IMetadataFieldHandler {

	public void handleField(IMetadataField field)throws InsufficientMetadataException;
	public void onNoField()throws InsufficientMetadataException;
}
