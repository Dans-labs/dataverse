package edu.harvard.iq.dataverse.pidprovider.contract;

import java.sql.Timestamp;

/**
 * Collects methods to access the hosting application in a secure way. Defines a
 * contract for a dataset that is to be registered with the PID provider.
 * 
 * @author devel
 *
 */
public interface IDataset {

	/**
	 * Returns the identifier of the dataset.
	 * 
	 * @return The identier of the dataset
	 */
	String getIdentifier();

	/**
	 * Returns the global ID of the dataset.
	 * 
	 * @return The global ID of the dataset
	 */
	String getGlobalId();

	/**
	 * Returns the authority string of the PID. e.g 10.5072
	 * 
	 * @return The authority string of the PID
	 */
	String getAuthority();

	/**
	 * Returns the DOI separator, respectively any other PID separator, e.g.
	 * "/".
	 * 
	 * @return The DOI separator
	 */
	String getDoiSeparator();
        
        
        
        /**
         * Returns the name of the hosting dataverse.
         * @return The name
         */
        String getDataverseName();
        
        
        /**
         * Returns the name of the root dataverse.
         * @return The name
         */
        String getRootDataverseName();
 
        

	/**
	 * Returns the publication date as timestamp.
	 * 
	 * @return The publication date
	 */
	Timestamp getPublicationDate();
        
        
        /**
	 * Returns the creation date as timestamp.
	 * 
	 * @return The publication date
	 */
	Timestamp getCreationDate();
    

	/**
	 * Returns a string array of published(!) versions, e.g. {1.0","1.1","2.0"}
	 * 
	 * @return An array of versions
	 */
	String[] listVersions();

        
        
        /**
         * Returns the URL under that the dataset will be published.
         * @return 
         */
        String getDatasetURL();
        
        
	/**
	 * Returns the top-level field of a metadata block entry. The Fields contain
	 * subfield in a tree-fashion and can be traversed.
	 * 
	 * @param version The version of the dataset to be used, e.g. "1.0"
	 * @param metadatablock The metadata block to search the field in, e.g. "citation"
	 * @param topLevelFieldId The identifier of the top-level-field
	 * @return An array of top-level fields
	 */
	IMetadataField[] getFields(String version, String metadatablock, String topLevelFieldId);

        
        /**
         * Returns the file objects of the dataset.
         * @param version
         * @return 
         */
	public IFile[] getFiles(String version); // TODO: File are not subject to versioning.
}
