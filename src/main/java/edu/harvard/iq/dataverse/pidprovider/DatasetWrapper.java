package edu.harvard.iq.dataverse.pidprovider;

import edu.harvard.iq.dataverse.*;
import edu.harvard.iq.dataverse.pidprovider.contract.IDataset;
import edu.harvard.iq.dataverse.pidprovider.contract.IFile;
import edu.harvard.iq.dataverse.pidprovider.contract.IMetadataField;
import edu.harvard.iq.dataverse.util.SystemConfig;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DatasetWrapper implements IDataset {

    private static final Logger logger = Logger.getLogger(DatasetWrapper.class.getCanonicalName());
    private Dataset dataset = null;

    public DatasetWrapper(Dataset dataset) {
        this.dataset = dataset;
    }

    @Override
    public String getIdentifier() {
        return dataset.getIdentifier();
    }

    @Override
    public String getGlobalId() {
        return dataset.getGlobalId();
    }

    @Override
    public String getAuthority() {
        return dataset.getAuthority();
    }

    @Override
    public String getDoiSeparator() {
        return dataset.getDoiSeparator();
    }

    @Override
    public String getDataverseName() {
        return dataset.getOwner().getName();
    }

    @Override
    public String getRootDataverseName() {
        Dataverse ref = dataset.getOwner();
        while (ref.getOwner() != null) {
            ref = ref.getOwner();
        }
        return ref.getName();
    }

    @Override
    public Timestamp getCreationDate() {
        return dataset.getCreateDate();
    }

    @Override
    public Timestamp getPublicationDate() {
        return dataset.getPublicationDate();
    }

    @Override
    public String[] listVersions() {
        List<DatasetVersion> list = dataset.getVersions();

        List<String> vList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isReleased()) {
                Long major = list.get(i).getVersionNumber();
                Long minor = list.get(i).getMinorVersionNumber();
                vList.add(major + "." + minor);
            }
        }
        return vList.toArray(new String[vList.size()]);
    }

    /**
     *
     * @param version Use null to use draft version.
     * @param metadatablock
     * @param topLevelFieldId
     * @return
     */
    @Override
    public IMetadataField[] getFields(String version, String metadatablock, String topLevelFieldId) {
        DatasetVersion dsVersion = getVersion(version);

        List<DatasetField> dsField = find(metadatablock, topLevelFieldId, dsVersion.getDatasetFields());

        MetadataField[] mfArr = new MetadataField[dsField.size()];

        for (int i = 0; i < mfArr.length; i++) {
            MetadataField mf = new MetadataField();
            fill(mf, dsField.get(i));
            mfArr[i] = mf;
        }
        return mfArr;
    }

    private MetadataField fill(MetadataField mf, DatasetField df) {
        DatasetFieldType type = df.getDatasetFieldType();
        mf.setIdentifier(type.getName());
        mf.setValue(df.getValue());
        mf.setValues(df.getValues());

        // Get the subfield touples
        List<DatasetFieldCompoundValue> compValueList = df.getDatasetFieldCompoundValues();

        // For each touple of subfields...
        for (DatasetFieldCompoundValue compValue : compValueList) {
            List<DatasetField> childList = compValue.getChildDatasetFields();
            MetadataField mf2 = new MetadataField();
            mf.addChild(mf2);
            for (DatasetField df2 : childList) {
                MetadataField mf3 = new MetadataField();
                mf2.addChild(mf3);
                fill(mf3, df2);
            }
        }
        return mf;
    }

    private DatasetVersion getVersion(String version) {
        if (version == null) {
            return dataset.getEditVersion();
        }
        List<DatasetVersion> list = dataset.getVersions();

        String[] strArr = version.split("\\.");
        if (strArr.length != 2) {
            return null;
        }
        long wantedMajor;
        long wantedMinor;
        try {
            wantedMajor = Long.valueOf(strArr[0]);
            wantedMinor = Long.valueOf(strArr[1]);
        } catch (NumberFormatException ex) {
            return null;
        }

        for (DatasetVersion v : list) {
            Long major = v.getVersionNumber();
            Long minor = v.getMinorVersionNumber();
            if (major == null && minor == null) {
                major = new Long(1);
                minor = new Long(0);
            }
            if (major == wantedMajor && minor == wantedMinor) {
                return v;
            }
        }
        return null;
    }

    private static List<DatasetField> find(String metadataBlock, String datasetFieldName, List<DatasetField> list) {
        List<DatasetField> retList = new ArrayList<>();

        for (DatasetField f : list) {
            DatasetFieldType fType = f.getDatasetFieldType();
            if (fType != null) {
                if (fType.getMetadataBlock().getName().equals(metadataBlock)
                        && fType.getName().equals(datasetFieldName)) {
                    retList.add(f);
                }
            }
        }
        return retList;
    }

    @Override
    public IFile[] getFiles(String version) {
        DatasetVersion dsVersion = getVersion(version);
        IFile[] files = new IFile[dsVersion.getFileMetadatas().size()];
        for (int i = 0; i < files.length; i++) {
            files[i] = new DataFileWrapper(dsVersion.getFileMetadatas().get(i).getDataFile());
        }
        return files;
    }

    @Override
    public String getDatasetURL() {
        String hostUrl = System.getProperty(SystemConfig.SITE_URL);

        if (hostUrl != null && !"".equals(hostUrl)) {
            return hostUrl + Dataset.TARGET_URL + dataset.getGlobalId();
        }
        String hostName = System.getProperty(SystemConfig.FQDN);
        if (hostName == null) {
            try {
                hostName = InetAddress.getLocalHost().getCanonicalHostName();
            } catch (UnknownHostException e) {
                return null;
            }
        }
        hostUrl = "https://" + hostName;
        return hostUrl + Dataset.TARGET_URL + dataset.getGlobalId();

    }

}
