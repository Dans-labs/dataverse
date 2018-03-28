/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.pidprovider.datacite;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author devel
 */
public class DataCiteRegisterService {

    private static final Logger logger = Logger.getLogger(DataCiteRegisterService.class.getCanonicalName());

    public boolean testDOIExists(String identifier) {
        boolean doiExists;
        try (DataCiteRESTfullClient client = openClient()) {
            doiExists = client.testDOIExists(identifier.substring(identifier.indexOf(":") + 1));
        } catch (Exception e) {
            logger.log(Level.INFO, identifier, e);
            return false;
        }
        return doiExists;
    }

    private DataCiteRESTfullClient openClient() throws IOException {
        return new DataCiteRESTfullClient(System.getProperty("doi.baseurlstring"), System.getProperty("doi.username"), System.getProperty("doi.password"));
    }

    public String alterIdentifier(String identifier,
            HashMap<String, String> metadata, List<Author> authorList,
            List<String[]> contactList, List<String[]> producerList) throws IOException {
        DataCiteMetadataTemplate metadataTemplate = new DataCiteMetadataTemplate();

        String tmp = identifier.substring(identifier.indexOf(':') + 1);
        metadataTemplate.setIdentifier(tmp);

        metadataTemplate.setCreators(Util.getListFromStr(metadata.get("datacite.creator")));
        metadataTemplate.setAuthors(authorList);
        metadataTemplate.setDescription(metadata.get("datacite.description"));
        // For debugging, set description to an unclosed tag, to make XML not well formed.
//        metadataTemplate.setDescription("<br>");
        metadataTemplate.setContacts(contactList);
        metadataTemplate.setProducers(producerList);
        metadataTemplate.setTitle(metadata.get("datacite.title"));
        metadataTemplate.setPublisher(metadata.get("datacite.publisher"));
        metadataTemplate.setPublisherYear(metadata.get("datacite.publicationyear"));
        String xmlMetadata = metadataTemplate.generateXML();
        logger.fine("XML to send to DataCite: " + xmlMetadata);

        String target = metadata.get("_target");
        String retString = null;

        String val = System.getProperty("org.gesis.test");
        if (val == null) {

            try (DataCiteRESTfullClient client = openClient()) { // Two steps: 1. Send metadata 2. Add URL
                retString = client.postMetadata(xmlMetadata);
                client.postUrl(identifier.substring(identifier.indexOf(":") + 1), target);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(DataCiteRegisterService.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        }
        return retString;
    }

    public void sendNewTargetURL(String identifier, String target) throws IOException {
        String val = System.getProperty("org.gesis.test");
        if (val == null) {
            try (DataCiteRESTfullClient client = openClient()) {
                client.postUrl(identifier.substring(identifier.indexOf(":") + 1), target);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(DataCiteRegisterService.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
        }
    }

    /**
     * Retrieves some data from datacite
     *
     * @param identifier
     * @return
     * @throws IOException
     */
    public HashMap<String, String> getMetadataFromService(String identifier) throws IOException {
        HashMap<String, String> metadata = new HashMap<>();
        try (DataCiteRESTfullClient client = openClient()) {
            String xmlMetadata = client.getMetadata(identifier.substring(identifier.indexOf(":") + 1));
            DataCiteMetadataTemplate template = new DataCiteMetadataTemplate(xmlMetadata);
            metadata.put("datacite.creator", Util.getStrFromList(template.getCreators()));
            metadata.put("datacite.title", template.getTitle());
            metadata.put("datacite.publisher", template.getPublisher());
            metadata.put("datacite.publicationyear", template.getPublisherYear());

        } catch (RuntimeException e) {
            logger.log(Level.INFO, identifier, e);
        }
        return metadata;
    }

    public void deleteIdentifier(String identifier) throws IOException {
        String val = System.getProperty("org.gesis.test");
        if (val == null) {
            try (DataCiteRESTfullClient client = openClient()) {
                client.inactiveDataset(identifier.substring(identifier.indexOf(":") + 1));

            } catch (IOException io) {
                throw io;
            }
        }
    }

}
