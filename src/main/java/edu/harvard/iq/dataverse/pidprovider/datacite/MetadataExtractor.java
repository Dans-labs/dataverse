/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.pidprovider.datacite;

import edu.harvard.iq.dataverse.pidprovider.contract.IDataset;
import edu.harvard.iq.dataverse.pidprovider.contract.IMetadataField;
import edu.harvard.iq.dataverse.pidprovider.contract.InsufficientMetadataException;
import edu.harvard.iq.dataverse.util.MarkupChecker;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author devel
 */
public class MetadataExtractor {
    
    
    public static String readAuthorString(IDataset ds, String version) throws InsufficientMetadataException {
        // Author information
        List<String> authorStringList = new ArrayList<>();
        FieldLooper looper = new FieldLooper(ds, "citation", "author", version);
        looper.loopTupels(new IMetadataFieldHandler() {
            @Override
            public void handleField(IMetadataField field) throws InsufficientMetadataException {
                String authorName = getValue(field, "authorName");
                String authorAffiliation = getValue(field, "authorAffiliation");

                String composedString = authorName;
                if (authorAffiliation != null) {
                    composedString += "(" + authorAffiliation + ")";
                }
                authorStringList.add(composedString);
            }

            @Override
            public void onNoField() throws InsufficientMetadataException {
                throw new InsufficientMetadataException("No autor information provided.");
            }
        });

        // Put authors in one line
        String authorString = "";
        String[] authors = authorStringList.toArray(new String[authorStringList.size()]);
        for (int i = 0; i < authors.length; i++) {
            authorString += authors[i];
            if (i < authors.length - 1) {
                authorString += ";";
            }
        }
        
        if (authorString.isEmpty()) {
            authorString = ":unav";
        }
        return authorString;
    }
    
    public static String readTitle(IDataset ds, String version) throws InsufficientMetadataException {
        String[] sArr = new String[1];
        FieldLooper looper = new FieldLooper(ds, "citation", "title", version);
        looper.loopSuperField(new IMetadataFieldHandler() {
            @Override
            public void handleField(IMetadataField field) throws InsufficientMetadataException {
                String title = field.getValue();
                sArr[0]=title;
            }

            @Override
            public void onNoField() throws InsufficientMetadataException {
                throw new InsufficientMetadataException("No title provided.");
            }
        });
        return sArr[0];
    }
    
    
    public static int readPublicationYear(IDataset ds, String version){
        Timestamp ts = ds.getPublicationDate();
        if(ts==null){ // Unpublished datasets do not have publication date.
            // Use creation date instead
            ts = ds.getCreationDate();
        }
        long timestamp = ts.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        int year = cal.get(Calendar.YEAR);
        return year;
    }
    
    public static String readTargetURL(IDataset ds){
        return ds.getDatasetURL();
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
                sArr[0]= MarkupChecker.stripAllTags(descriptionText);
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

    public static List<String[]> readProducers(IDataset ds, String version) throws InsufficientMetadataException {
        List<String[]> producerList = new ArrayList<>();

        FieldLooper looper = new FieldLooper(ds, "citation", "producer", version);
        looper.loopTupels(new IMetadataFieldHandler() {
            @Override
            public void handleField(IMetadataField field) throws InsufficientMetadataException {
                String producerName = getValue(field, "producerName");
                String producerAffiliaton = getValue(field, "producerAffiliation");

                producerList.add(new String[]{producerName, producerAffiliaton});
            }

            @Override
            public void onNoField() throws InsufficientMetadataException {
                // Ignore
            }
        });
        return producerList;
    }
    
    
}
