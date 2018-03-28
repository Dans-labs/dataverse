/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.pidprovider;

import com.google.auto.service.AutoService;
import edu.emory.mathcs.backport.java.util.Arrays;
import edu.harvard.iq.dataverse.pidprovider.contract.*;
import edu.harvard.iq.dataverse.pidprovider.datacite.Author;
import edu.harvard.iq.dataverse.pidprovider.datacite.FieldLooper;
import edu.harvard.iq.dataverse.pidprovider.datacite.IMetadataFieldHandler;
import edu.harvard.iq.dataverse.util.MarkupChecker;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author devel
 */
@AutoService(IPIDProvider.class)
public class TestPlugin implements IPIDProvider {

    private static final Logger logger = Logger.getLogger(TestPlugin.class.getCanonicalName());

    @Override
    public String getPluginName() {
        return "Test";
    }

    @Override
    public String getProtocol() {
        return "doi";
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
        return "Testprovider";
    }

    @Override
    public Optional<String> onCreation(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, PIDInUseException {
        logger.log(Level.INFO, "onCreation called for dataset " + ds.getIdentifier());
        testDatasetSpecific(ds);
        testFiles(ds);

        String[] versionArr = ds.listVersions();
        if (versionArr != null) {
            if (versionArr.length != 0) {
                throw new InsufficientMetadataException("Versions, apart from draft, were found on event onCreate.");
            }
        }
        if (ds.getPublicationDate() != null) {
            throw new InsufficientMetadataException("A publication date was available at event onCreate.");
        }

        testVersionSpecific(ds);
        return Optional.empty();

    }

    @Override
    public Optional<String> onUpdate(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException, PIDInUseException {
        logger.log(Level.INFO, "onUpdate called for dataset " + ds.getIdentifier());
        testDatasetSpecific(ds);
        testFiles(ds);
        testVersionSpecific(ds);
        return Optional.empty();
    }

    @Override
    public Optional<String> onPublish(IDataset ds, String version) throws IOException, InsufficientMetadataException, PIDProviderException, PIDInUseException {
        logger.log(Level.INFO, "onPublish called for dataset " + ds.getIdentifier());
        testDatasetSpecific(ds);
        testFiles(ds);
        checkPresence("Version list for publish", version);
        testVersionSpecific(ds);
        logger.log(Level.INFO, "onPublish complete.");
        return Optional.empty();
    }

    @Override
    public void onDeaccession(IDataset ds, String version) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        logger.log(Level.INFO, "onDeaccession called for dataset " + ds.getIdentifier());
        testDatasetSpecific(ds);
        testFiles(ds);
        String[] versionArr = ds.listVersions();
        checkPresence("Publication date", ds.getPublicationDate());
        checkPresenceAndSize("Version list for deaccess", versionArr);
        testVersionSpecific(ds);
    }

    @Override
    public void onDestroy(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        logger.log(Level.INFO, "onDestroy called for dataset " + ds.getIdentifier());
        testDatasetSpecific(ds);
        testFiles(ds);
        checkPresence("Publication date", ds.getPublicationDate());
        String[] versionArr = ds.listVersions();
        checkPresenceAndSize("Version list for destroy", versionArr);
        testVersionSpecific(ds);
        logger.log(Level.INFO, "onDestroy complete " + ds.getIdentifier());
    }

    @Override
    public void onUpdateDatasetURL(IDataset ds) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        logger.log(Level.INFO, "onUpdateDatasetURL called for dataset " + ds.getIdentifier());
        testDatasetSpecific(ds);
        testFiles(ds);
        checkPresence("Publication date", ds.getPublicationDate());
        String[] versionArr = ds.listVersions();
        checkPresenceAndSize("Version list for update of URL", versionArr);
        testVersionSpecific(ds);
    }

    @Override
    public boolean pidExists(String pid) throws NotSupportedException, IOException, PIDProviderException {
        logger.log(Level.INFO, "pidExists called for PID " + pid);
        try {
            checkPresence("PID", pid);
        } catch (InsufficientMetadataException ex) {
            throw new RuntimeException(ex);
        }
        if (pid.startsWith("doi:") || pid.startsWith("hdl:")) {
            throw new RuntimeException("PID starts with doi: or hdl:");
        }
        return false;
    }

    @Override
    public Optional<String> onCreateFile(IDataset ds, IFile file) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, PIDInUseException {
        logger.log(Level.INFO, "onCreateFile called for file " + file.getFileIdentifier());
        return Optional.empty();
    }

    @Override
    public void onDeleteFile(IDataset ds, IFile file) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        logger.log(Level.INFO, "onDeleteFile called for file " + file.getFileIdentifier());
    }

    @Override
    public Optional<String> onPublishFile(IDataset ds, String version, IFile file) throws IOException, InsufficientMetadataException, PIDProviderException, PIDInUseException {
        logger.log(Level.INFO, "onPublishFile called for file " + file.getFileIdentifier());
        return Optional.empty();
    }

    @Override
    public void onDeaccessionFile(IDataset ds, IFile file, String version) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        logger.log(Level.INFO, "onDeaccessionFile called for file " + file.getFileIdentifier());
    }

    @Override
    public void onDestroyFile(IDataset ds, IFile file) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        logger.log(Level.INFO, "onDestroyFile called for file " + file.getFileIdentifier());
    }

    @Override
    public void onUpdateFileURL(IDataset ds, IFile file) throws NotSupportedException, IOException, InsufficientMetadataException, PIDProviderException, UnknownPIDException {
        logger.log(Level.INFO, "onUpdateFileURL called for file " + file.getFileIdentifier());
    }

    private static void testDatasetSpecific(IDataset ds) throws InsufficientMetadataException {
        // Check dataset specific
        checkPresence("Identifier", ds.getIdentifier());
        checkPresence("Authority", ds.getAuthority());
        checkPresence("Doi separator", ds.getDoiSeparator());
        checkPresence("Dataset URL", ds.getDatasetURL());
        checkPresence("Creation date", ds.getCreationDate());
        try {
            URL url = new URL(ds.getDatasetURL());
        } catch (MalformedURLException ex) {
            throw new InsufficientMetadataException("Dataset URL is not valid. " + ds.getDatasetURL());
        }
        checkPresence("Dataverse name", ds.getDataverseName());
        checkPresence("Global id", ds.getGlobalId());
        if (!ds.getGlobalId().startsWith("doi:") && !ds.getGlobalId().startsWith("hdl")) {
            throw new InsufficientMetadataException("Global id is missing doi: or hdl: prefix");
        }
        checkPresence("Root Dataverse", ds.getRootDataverseName());
    }

    private static void testFiles(IDataset ds) throws InsufficientMetadataException {
        // check files
        IFile[] fileArr = ds.getFiles(null);
        for (IFile f : fileArr) {
            checkPresence("File identifier", f.getFileIdentifier());
            checkPresence("File URL", f.getFileURL());
            checkPresence("File fingerprint", f.getFingerprint());
            checkPresence("File fingerprint method", f.getFingerprintMethod());
            checkPresence("File formate", f.getFormat());
            checkPresence("File name", f.getName());
            checkPresence("File PID", f.getPID());
            checkPresence("File size", f.getSize());
        }
    }

    private static void testVersionSpecific(IDataset ds) throws InsufficientMetadataException {
        List<String>versionList = new ArrayList<>();
        versionList.addAll(Arrays.asList(ds.listVersions())); 
        versionList.add(null);
        
        // Check fields / version specific
        for (String version : versionList) {
            String title = readTitle(ds, version);
            checkPresence("Title", title);
            List<Author> authorList = readAuthors(ds, version);
            checkPresenceAndSize("Authors", authorList);
            List<String[]> contactList = readContacts(ds, version);
            checkPresenceAndSize("Contacts", contactList);
            String description = readDescription(ds, version);
            checkPresence("Description", description);
        }
    }

    private static void checkPresence(String referenceName, Object ref) throws InsufficientMetadataException {
        if (ref == null) {
            throw new InsufficientMetadataException(referenceName + " was null");
        }
    }

    private static void checkPresenceAndSize(String referenceName, Collection col) throws InsufficientMetadataException {
        checkPresence(referenceName, col);
        if (col.isEmpty()) {
            throw new InsufficientMetadataException(referenceName + " is empty");
        }
    }

    private static void checkPresenceAndSize(String referenceName, Object[] arr) throws InsufficientMetadataException {
        checkPresenceAndSize(referenceName, Arrays.asList(arr));
    }

    public static String readTitle(IDataset ds, String version) throws InsufficientMetadataException {
        String[] sArr = new String[1];
        FieldLooper looper = new FieldLooper(ds, "citation", "title", version);
        looper.loopSuperField(new IMetadataFieldHandler() {
            @Override
            public void handleField(IMetadataField field) throws InsufficientMetadataException {
                String title = field.getValue();
                sArr[0] = title;
            }

            @Override
            public void onNoField() throws InsufficientMetadataException {
                throw new InsufficientMetadataException("No title provided.");
            }
        });
        return sArr[0];
    }

    public static int readPublicationYear(IDataset ds, String version) {
        Timestamp ts = ds.getPublicationDate();
        if (ts == null) { // Unpublished datasets do not have publication date.
            // Use creation date instead
            ts = ds.getCreationDate();
        }
        long timestamp = ts.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    public static String readDescription(IDataset ds, String version) throws InsufficientMetadataException {
        // Description
        // Only use last value.
        String[] sArr = new String[1];
        FieldLooper looper = new FieldLooper(ds, "citation", "dsDescription", version);
        looper.loopTupels(new IMetadataFieldHandler() {
            @Override
            public void handleField(IMetadataField field) throws InsufficientMetadataException {
                String descriptionText = getValue(field, "dsDescriptionValue");
                sArr[0] = MarkupChecker.stripAllTags(descriptionText);
            }

            @Override
            public void onNoField() throws InsufficientMetadataException {
                throw new InsufficientMetadataException("No description provided.");
            }
        });
        return sArr[0];
    }

    private static String getValue(IMetadataField compoundField, String identifier) {
        IMetadataField childField = compoundField.getChild(identifier);
        if (childField != null) {
            return childField.getValue();
        } else {
            return null;
        }
    }

    public static List<Author> readAuthors(IDataset ds, String version) throws InsufficientMetadataException {
        List<Author> authorList = new ArrayList<>();

        FieldLooper looper = new FieldLooper(ds, "citation", "author", version);
        looper.loopTupels(new IMetadataFieldHandler() {
            @Override
            public void handleField(IMetadataField field) throws InsufficientMetadataException {
                String authorName = getValue(field, "authorName");
                String authorAffiliation = getValue(field, "authorAffiliation");
                String authorIdentifierScheme = getValue(field, "authorIdentifierScheme");
                String authorIdentifier = getValue(field, "authorIdentifier");

                Author author = new Author();
                author.setName(authorName);
                author.setAffiliation(authorAffiliation);
                author.setIdentifierScheme(authorIdentifierScheme);
                author.setIdentifier(authorIdentifier);
                authorList.add(author);
            }

            @Override
            public void onNoField() throws InsufficientMetadataException {
                // Cannot happen, author is mandatory
            }
        });
        return authorList;
    }

    public static List<String[]> readContacts(IDataset ds, String version) throws InsufficientMetadataException {
        List<String[]> contactList = new ArrayList<>();

        FieldLooper looper = new FieldLooper(ds, "citation", "datasetContact", version);
        looper.loopTupels(new IMetadataFieldHandler() {
            @Override
            public void handleField(IMetadataField field) throws InsufficientMetadataException {
                String datasetContactName = getValue(field, "datasetContactName");
                String datasetContactAffiliaton = getValue(field, "datasetContactAffiliation");

                contactList.add(new String[]{datasetContactName, datasetContactAffiliaton});
            }

            @Override
            public void onNoField() throws InsufficientMetadataException {
                // Ignore
            }
        });
        return contactList;
    }

   
}
