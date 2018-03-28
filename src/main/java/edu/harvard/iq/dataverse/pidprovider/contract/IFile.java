package edu.harvard.iq.dataverse.pidprovider.contract;

public interface IFile {

    String getFileIdentifier();

    String getName();

    String getFormat();

    long getSize(); //Bytes

    String getFingerprint();

    String getFingerprintMethod();

    String getPID();

    String getFileURL();

}
