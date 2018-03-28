package edu.harvard.iq.dataverse.pidprovider.contract;

import java.io.IOException;
import java.util.Optional;

/**
 * This is the interface for the PID providers to implement in order to be
 * accessed by the host application.
 *
 * @author devel
 *
 */
public interface IPIDProvider {

    // --- Basic administrative functionality ---
    /**
     * Getter for the name of the plugin. Shall be used by DV to detect the
     * desired plugin in the plugin container.
     *
     * @return Name of the plugin, e.g. "Dara v1.0"
     */
    public String getPluginName();

    /**
     * Getter for the protocol used for PIDs e.g. "doi, "hdl",...
     *
     * @return "doi, "hdl",...
     */
    public String getProtocol();

    /**
     * To be called when the plugin is loaded.
     *
     * @throws Exception
     */
    public void start() throws Exception;

    /**
     * Checks the metadata of dataset for sufficiency of registration. Can be
     * called at any time, e.g. before saving a dataset.
     *
     * @param ds The dataset that allows for access to the metadata of a
     * document.
     * @throws InsufficientMetadataException Signals an error when reading
     * metadata from the dataset.
     */
    public void testMetadata(IDataset ds) throws InsufficientMetadataException;

    // --- Getter for general information about the provider --- 
    /**
     * Getter for the name of the provider addressed by the plugin.
     *
     * @return
     */
    public String getProviderName();

    // --- Methods to be called during the lifecycle of a document ---
    // Methods here are tied to the lifecycle of a document so implementers can
    // choose how to deal with the events on their own.
    /**
     * To be called when a new dataset (draft version) is created.
     *
     * @param ds The dataset that allows for access to the metadata of a
     * document.
     * @return The PID under that the document(s) was actually registrated.
     * @throws NotSupportedException Signals nothing was done by this method.
     * @throws IOException Signals a network error.
     * @throws InsufficientMetadataException Signals an error when reading
     * metadata from the dataset.
     * @throws PIDProviderException Signals an error of the provider
     * registration service.
     * @throws PIDInUseException Signals a PID is already in use and needs to be
     * recreated.
     */
    public Optional<String> onCreation(IDataset ds) throws NotSupportedException,
            IOException, InsufficientMetadataException, PIDProviderException,
            PIDInUseException;

    /**
     * To be called when a dataset (unregistered, registered or reserved) is
     * updated.
     *
     * @param ds The dataset that allows for access to the metadata of a
     * document.
     * @throws NotSupportedException Signals nothing was done by this method.
     * @throws IOException IOException Signals a network error.
     * @throws InsufficientMetadataException Signals an error when reading
     * metadata from the dataset.
     * @throws PIDProviderException Signals an error of the provider
     * registration service.
     * @throws UnknownPIDException Signals the (failed) update of metadata to an
     * unknown PID.
     * @throws PIDInUseException Signals the operation failed due to an already
     * used PID.
     */
    public Optional<String> onUpdate(IDataset ds) throws NotSupportedException, IOException,
            InsufficientMetadataException, PIDProviderException,
            UnknownPIDException, PIDInUseException;

    /**
     * Called when a dataset is published.
     *
     * @param ds The dataset that allows for access to the metadata of a
     * document.
     * @param version The version number of the release.
     * @return The PID under that the document(s) was actually registrated.
     * @throws NotSupportedException Signals nothing was done by this method.
     * @throws IOException IOException Signals a network error.
     * @throws InsufficientMetadataException Signals an error when reading
     * metadata from the dataset.
     * @throws PIDProviderException Signals an error of the provider
     * registration service.
     * @throws PIDInUseException
     */
    public Optional<String> onPublish(IDataset ds, String version) throws
            IOException, InsufficientMetadataException, PIDProviderException,
            PIDInUseException;

    /**
     * Called when a dataset is depublicized.
     *
     * @param ds The dataset that allows for access to the metadata of a
     * document.
     * @throws NotSupportedException Signals nothing was done by this method.
     * @throws IOException IOException Signals a network error.
     * @throws InsufficientMetadataException Signals an error when reading
     * metadata from the dataset.
     * @throws PIDProviderException Signals an error of the provider
     * registration service.
     * @throws UnknownPIDException Signals the (failed) operation due to an
     * unknown PID.
     */
    public void onDeaccession(IDataset ds, String version) throws NotSupportedException, IOException,
            InsufficientMetadataException, PIDProviderException,
            UnknownPIDException;

    /**
     * Called when a dataset is destroyed.
     *
     * @param ds The dataset that allows for access to the metadata of a
     * document.
     * @throws NotSupportedException Signals nothing was done by this method.
     * @throws IOException IOException Signals a network error.
     * @throws InsufficientMetadataException Signals an error when reading
     * metadata from the dataset.
     * @throws PIDProviderException Signals an error of the provider
     * registration service.
     * @throws UnknownPIDException Signals the (failed) operation due to an
     * unknown PID.
     */
    public void onDestroy(IDataset ds) throws NotSupportedException, IOException,
            InsufficientMetadataException, PIDProviderException,
            UnknownPIDException;

    /**
     * Is called when the target URL for a dataset is to be changed.
     *
     * @param ds The dataset that allows for access to the metadata of a
     * document. The new URL is already set.
     * @throws NotSupportedException Signals nothing was done by this method.
     * @throws IOException IOException Signals a network error.
     * @throws InsufficientMetadataException Signals an error when reading
     * metadata from the dataset.
     * @throws PIDProviderException Signals an error of the provider
     * registration service.
     * @throws UnknownPIDException Signals the (failed) operation due to an
     * unknown PID.
     */
    public void onUpdateDatasetURL(IDataset ds) throws NotSupportedException, IOException,
            InsufficientMetadataException, PIDProviderException,
            UnknownPIDException;

    /**
     * Checks whether a PID is already known by the provider.
     *
     * @param pid The PID to be checked.
     * @return True if the PID exists, false otherwise.
     */
    public boolean pidExists(String pid) throws NotSupportedException, IOException, PIDProviderException;

    /**
     * To be called when a file is added to a dataset.
     *
     * @param file The file to be added.
     * @return
     */
    public Optional<String> onCreateFile(IDataset ds, IFile file) throws NotSupportedException,
            IOException, InsufficientMetadataException, PIDProviderException,
            PIDInUseException;

    /**
     * To be called when a file is removed from a dataset.
     *
     * @param file The file to be removed.
     */
    public void onDeleteFile(IDataset ds, IFile file) throws NotSupportedException, IOException,
            InsufficientMetadataException, PIDProviderException,
            UnknownPIDException;

    public Optional<String> onPublishFile(IDataset ds, String version, IFile file) throws
            IOException, InsufficientMetadataException, PIDProviderException,
            PIDInUseException;

    public void onDeaccessionFile(IDataset ds, IFile file, String version) throws NotSupportedException, IOException,
            InsufficientMetadataException, PIDProviderException,
            UnknownPIDException;

    public void onDestroyFile(IDataset ds, IFile file) throws NotSupportedException, IOException,
            InsufficientMetadataException, PIDProviderException,
            UnknownPIDException;

    public void onUpdateFileURL(IDataset ds, IFile file) throws NotSupportedException, IOException, 
            InsufficientMetadataException, PIDProviderException, 
            UnknownPIDException;

}
