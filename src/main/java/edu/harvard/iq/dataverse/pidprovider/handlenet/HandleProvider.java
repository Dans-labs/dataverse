/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.pidprovider.handlenet;

import com.google.auto.service.AutoService;
import edu.harvard.iq.dataverse.pidprovider.contract.*;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author devel
 */
@AutoService(IPIDProvider.class)
public class HandleProvider implements IPIDProvider {

    @Override
    public String getPluginName() {
        return "Handle";
    }

    @Override
    public String getProtocol() {
        return "hdl";
    }

    @Override
    public void start() throws Exception {
        // Do nothing
    }

    @Override
    public void testMetadata(IDataset ds) throws InsufficientMetadataException {
        // Do nothing
    }

    @Override
    public String getProviderName() {
        return "N/A";
    }

    @Override
    public Optional<String> onCreation(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, PIDInUseException {
        Handlenet h = new Handlenet();
        String handle = null;
        try {
            handle = h.createIdentifier(ds);
        } catch (Throwable ex) {
            Logger.getLogger(HandleProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.ofNullable(handle);
    }

    @Override
    public Optional<String> onUpdate(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException, PIDInUseException {
        Handlenet h = new Handlenet();
        String handle = null;
        try {
            handle = h.modifyIdentifier(ds);
        } catch (Throwable ex) {
            Logger.getLogger(HandleProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Failed to modify identifier.",ex);
        }
        return Optional.ofNullable(handle);
    }

    @Override
    public Optional<String> onPublish(IDataset ds, String version) throws IOException, InsufficientMetadataException, PIDProviderException, PIDInUseException {
        Handlenet h = new Handlenet();
        try {
            h.publicizeIdentifier(ds);
        } catch (Throwable ex) {
            Logger.getLogger(HandleProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Failed to modify identifier.",ex);
        }
        return Optional.empty();
    }

    @Override
    public void onDeaccession(IDataset ds, String version) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        Handlenet h = new Handlenet();
        String handle = null;
        try {
            h.deleteIdentifier(ds);
        } catch (Throwable ex) {
            Logger.getLogger(HandleProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Failed to modify identifier.",ex);
        }
    }

    @Override
    public void onDestroy(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        throw new NotSupportedException(""); 
    }
    
    
     @Override
    public void onUpdateDatasetURL(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        Handlenet h = new Handlenet();
        String handle = null;
        try {
            h.modifyIdentifier(ds);
        } catch (Throwable ex) {
            Logger.getLogger(HandleProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Failed to modify identifier.",ex);
        }
    }

    @Override
    public boolean pidExists(String pid) throws NotSupportedException, IOException, PIDProviderException {
        Handlenet h = new Handlenet();
        String handle = null;
        try {
            return h.alreadyExists(handle);
        } catch (Throwable ex) {
            Logger.getLogger(HandleProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Failed to modify identifier.",ex);
        }
    }

    @Override
    public Optional<String> onCreateFile(IDataset ds, IFile file) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, PIDInUseException {
        Handlenet h = new Handlenet();
        String handle = null;
        try {
            handle = h.createIdentifier(file,ds);
        } catch (Throwable ex) {
            Logger.getLogger(HandleProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.ofNullable(handle);
    }

    @Override
    public void onDeleteFile(IDataset ds, IFile file) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
