package edu.harvard.iq.dataverse.pidprovider.datacite;

import edu.harvard.iq.dataverse.pidprovider.contract.IDataset;
import edu.harvard.iq.dataverse.pidprovider.contract.IMetadataField;
import edu.harvard.iq.dataverse.pidprovider.contract.InsufficientMetadataException;

import java.util.List;


public class FieldLooper {

    private String metadataBlock = null;
    private String superFieldId = null;
    private IDataset dataset = null;
    private String newVersion = null;

    public FieldLooper(IDataset dataset, String metadataBlock, String superFieldId,
            String newVersion) {
        this.metadataBlock = metadataBlock;
        this.superFieldId = superFieldId;
        this.dataset = dataset;
        this.newVersion = newVersion;
    }

    /**
     * Loops over super fields. Calls IMetadataFieldHandler.onNoField() on every
     * field, or IMetadataFieldHandler.handleField() if no fields are present.
     *
     * @param handler
     * @throws InsufficientMetadataException
     */
    public void loopSuperField(IMetadataFieldHandler handler) throws InsufficientMetadataException {

        IMetadataField[] mfArr = dataset.getFields(newVersion, metadataBlock, superFieldId);
        boolean handlerCalled = false;

        // Check if super fields are present
        if (mfArr != null && mfArr.length > 0) {

            // Loop super fields
            for (int i = 0; i < mfArr.length; i++) {

                // Check if a value was set
                if (mfArr[i].getValue() != null || (mfArr[i].getValues() != null && !mfArr[i].getValues().isEmpty())) {
                    handler.handleField(mfArr[i]);
                    handlerCalled = true;
                }
            }
        }
    }

    /**
     * Loops over tupel subfields. Calls IMetadataFieldHandler.handleField() on
     * every tupel, or IMetadataFieldHandler.onNoField() if no fields are
     * present.
     *
     * @param compField
     * @param handler
     * @throws InsufficientMetadataException
     */
    public void loopTupels(IMetadataFieldHandler handler) throws InsufficientMetadataException {

        IMetadataField[] mfArr = dataset.getFields(newVersion, metadataBlock, superFieldId);
        boolean handlerCalled = false;

        // Check if super fields are present
        if (mfArr != null && mfArr.length > 0) {

            // Loop super fields
            for (int i = 0; i < mfArr.length; i++) {

                List<IMetadataField> tuples = mfArr[i].getChidren();

                // Check if tuples are present
                if (tuples != null && !tuples.isEmpty()) {

                    // Loop tuples
                    for (IMetadataField tuple : tuples) {

                        List<IMetadataField> children = tuple.getChidren();

                        // Check if children present
                        if (children != null && !children.isEmpty()) {

                            // Loop children
                            boolean oneChildSet = false;
                            for (IMetadataField child : children) {

                                // Check child values
                                if (child.getValue() != null
                                        || (child.getValues() != null && !child.getValues().isEmpty())) {

                                    oneChildSet = true;
                                    break;
                                }
                            }
                            // If at least one child has a value... call the handler
                            if (oneChildSet) {
                                // Call handler
                                handlerCalled = true;
                                handler.handleField(tuple);
                            }
                        }
                    }
                }
            }
        }
    }
    
   

}
