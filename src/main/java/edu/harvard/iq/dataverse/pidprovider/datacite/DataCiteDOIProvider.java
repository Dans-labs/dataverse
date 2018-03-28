/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.pidprovider.datacite;

import com.google.auto.service.AutoService;
import edu.harvard.iq.dataverse.pidprovider.contract.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author devel
 */
@AutoService(IPIDProvider.class)
public class DataCiteDOIProvider implements IPIDProvider {

    private static final Logger logger = Logger.getLogger(DataCiteDOIProvider.class.getCanonicalName());

    @PersistenceContext(unitName = "VDCNet-ejbPU")
    private EntityManager em;

    DataCiteRegisterService dataCiteRegisterService;

    @Override
    public String getPluginName() {
        return "DataCite";
    }

    @Override
    public String getProtocol() {
        return "doi";
    }

    @Override
    public void start() throws Exception {
        this.dataCiteRegisterService = new DataCiteRegisterService();
    }

    @Override
    public void testMetadata(IDataset ds) throws InsufficientMetadataException {
        // Do nothing
    }

    @Override
    public String getProviderName() {
        return "DataCite";
    }

    @Override
    public Optional<String> onCreation(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, PIDInUseException {
        throw new NotSupportedException("There is no action connected to the creation of an identifier.");
    }

    @Override
    public Optional<String> onUpdate(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException, PIDInUseException {
        throw new NotSupportedException("There is no action connected to the creation of an identifier.");
    }

    @Override
    public Optional<String> onPublish(IDataset ds, String version) throws IOException, InsufficientMetadataException, PIDProviderException, PIDInUseException {
        String identifier = ds.getGlobalId();
        //HashMap<String, String> metadata = getUpdateMetadataFromDataset(dataset);// <- Means: creator, title, publisher/producer
        HashMap<String, String> metadata = new HashMap();
        metadata.put("datacite.creator", MetadataExtractor.readAuthorString(ds, null));
        metadata.put("datacite.title", MetadataExtractor.readTitle(ds, null));
        metadata.put("datacite.publisher", ds.getRootDataverseName());
        metadata.put("datacite.publicationyear", String.valueOf(MetadataExtractor.readPublicationYear(ds, null)));
        metadata.put("datacite.description", MetadataExtractor.readDescription(ds, null));
        metadata.put("_target", MetadataExtractor.readTargetURL(ds));
        List<Author> authorList = MetadataExtractor.readAuthors(ds, null);
        List<String[]> contactList = MetadataExtractor.readContacts(ds, null);
        List<String[]> producerList = MetadataExtractor.readProducers(ds, null);
        try {
            dataCiteRegisterService.alterIdentifier(identifier, metadata, authorList, contactList, producerList);
            return Optional.empty();
        } catch (IOException e) {
            logger.log(Level.WARNING, "modifyMetadata failed");
            logger.log(Level.WARNING, "String {0}", e.toString());
            logger.log(Level.WARNING, "localized message {0}", e.getLocalizedMessage());
            logger.log(Level.WARNING, "cause", e.getCause());
            logger.log(Level.WARNING, "message {0}", e.getMessage());
            throw e;
        }

    }

    @Override
    public void onDeaccession(IDataset ds, String version) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        // Retrieve metadata from service
        String identifier = ds.getGlobalId();
        try {
            dataCiteRegisterService.deleteIdentifier(identifier);
        } catch (IOException e) {
            logger.log(Level.WARNING, "get matadata failed cannot delete");
            logger.log(Level.WARNING, "String {0}", e.toString());
            logger.log(Level.WARNING, "localized message {0}", e.getLocalizedMessage());
            logger.log(Level.WARNING, "cause ", e.getCause());
            logger.log(Level.WARNING, "message {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void onDestroy(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        throw new NotSupportedException("Not supported.");
    }
    
    
     @Override
    public void onUpdateDatasetURL(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        dataCiteRegisterService.sendNewTargetURL(ds.getGlobalId(), ds.getDatasetURL());
    }
    

    @Override
    public boolean pidExists(String pid) throws NotSupportedException, IOException, PIDProviderException {
        logger.log(Level.FINE, "alreadyExists");
        boolean alreadyExists;
        try {
            alreadyExists = dataCiteRegisterService.testDOIExists(pid);
        } catch (Exception e) {
            logger.log(Level.WARNING, "alreadyExists failed");
            return false;
        }
        return alreadyExists;
    }

    @Override
    public Optional<String> onCreateFile(IDataset ds, IFile file) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, PIDInUseException {
         String fileIdentifier = file.getPID();
        //HashMap<String, String> metadata = getUpdateMetadataFromDataset(dataset);// <- Means: creator, title, publisher/producer
        HashMap<String, String> metadata = new HashMap();
        metadata.put("datacite.creator", MetadataExtractor.readAuthorString(ds,null));
        metadata.put("datacite.title", MetadataExtractor.readTitle(ds, null));
        metadata.put("datacite.publisher", ds.getRootDataverseName());
        metadata.put("datacite.publicationyear", String.valueOf(MetadataExtractor.readPublicationYear(ds, null)));
        metadata.put("datacite.description", MetadataExtractor.readDescription(ds, null));
        metadata.put("_target", MetadataExtractor.readTargetURL(ds));
        List<Author> authorList = MetadataExtractor.readAuthors(ds, null);
        List<String[]> contactList = MetadataExtractor.readContacts(ds, null);
        List<String[]> producerList = MetadataExtractor.readProducers(ds, null);
        try {
            dataCiteRegisterService.alterIdentifier(fileIdentifier, metadata, authorList, contactList, producerList);
            return Optional.empty();
        } catch (IOException e) {
            logger.log(Level.WARNING, "modifyMetadata failed");
            logger.log(Level.WARNING, "String {0}", e.toString());
            logger.log(Level.WARNING, "localized message {0}", e.getLocalizedMessage());
            logger.log(Level.WARNING, "cause", e.getCause());
            logger.log(Level.WARNING, "message {0}", e.getMessage());
            throw e;
        }
    }

   

    @Override
    public void onDeleteFile(IDataset ds, IFile file) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        // Retrieve metadata from service
        String identifier = file.getPID();
        try {
            dataCiteRegisterService.deleteIdentifier(identifier);
        } catch (IOException e) {
            logger.log(Level.WARNING, "get matadata failed cannot delete");
            logger.log(Level.WARNING, "String {0}", e.toString());
            logger.log(Level.WARNING, "localized message {0}", e.getLocalizedMessage());
            logger.log(Level.WARNING, "cause ", e.getCause());
            logger.log(Level.WARNING, "message {0}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<String> onPublishFile(IDataset ds, String version, IFile file) throws IOException, InsufficientMetadataException, PIDProviderException, PIDInUseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDeaccessionFile(IDataset ds, IFile file, String version) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDestroyFile(IDataset ds, IFile file) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onUpdateFileURL(IDataset ds, IFile file) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  
   

}
