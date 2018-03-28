package edu.harvard.iq.dataverse.pidprovider;

import edu.harvard.iq.dataverse.DataFile;
import edu.harvard.iq.dataverse.pidprovider.contract.IFile;

public class DataFileWrapper implements IFile {

    private DataFile file = null;

    public DataFileWrapper(DataFile file) {
        this.file = file;
    }

    @Override
    public String getName() {
        return file.getDisplayName();
    }

    @Override
    public String getFormat() {
        return file.getFriendlyType();
    }

    @Override
    public long getSize() {
        return file.getFilesize();
    }

    @Override
    public String getFingerprint() {
        return file.getChecksumValue();
    }

    @Override
    public String getFingerprintMethod() {
        return file.getChecksumType().name();
    }

    @Override
    public String getPID() {
        //return file.getGlobalId(); // <- not available yet
        return "not/avlid/pid";
    }

    @Override
    public String getFileIdentifier() {
        return String.valueOf(file.getId());
    }

    @Override
    public String getFileURL() {
        return "https://en.wikipedia.org/wiki/Link_rot";
    }

}
