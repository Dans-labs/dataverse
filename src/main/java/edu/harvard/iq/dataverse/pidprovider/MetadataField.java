package edu.harvard.iq.dataverse.pidprovider;

import edu.harvard.iq.dataverse.pidprovider.contract.IMetadataField;

import java.util.ArrayList;
import java.util.List;

public class MetadataField implements IMetadataField {

	private List<IMetadataField> children = null;
	private String identifier = null;
	private String value = null;
	private List<String> values = null;

	public MetadataField() {
		children = new ArrayList<IMetadataField>();

	}

	public void addChild(IMetadataField mf) {
		children.add(mf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.iq.dataverse.IMetadataField#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.iq.dataverse.IMetadataField#isNode()
	 */
	@Override
	public boolean isNode() {
		return !isLeaf();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.iq.dataverse.IMetadataField#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return identifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.iq.dataverse.IMetadataField#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	@Override
	public List<String> getValues() {
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.iq.dataverse.IMetadataField#childrenCount()
	 */
	@Override
	public int childrenCount() {
		return children.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.iq.dataverse.IMetadataField#getChidren()
	 */
	@Override
	public List<IMetadataField> getChidren() {
		return children;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.iq.dataverse.IMetadataField#getChild(int)
	 */
	@Override
	public IMetadataField getChild(int idx) {
		return children.get(idx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.harvard.iq.dataverse.IMetadataField#getChild(java.lang.String)
	 */
	@Override
	public IMetadataField getChild(String identifier) {
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).getIdentifier().equals(identifier)) {
				return children.get(i);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.harvard.iq.dataverse.IMetadataField#childExists(java.lang.String)
	 */
	@Override
	public boolean childExists(String identifier) {
		return getChild(identifier) != null;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return toString2("");
	}

	private String toString2(String indent) {
		StringBuffer buf = new StringBuffer(
				indent + "MetadataField(id=" + identifier + ", value=" + value + ", values=[");
		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				buf.append(values.get(i));
				if (i < values.size() - 1) {
					buf.append(",");
				}
			}
		}
		buf.append("]");
		buf.append(indent).append("{\n");
		for (int i = 0; i < children.size(); i++) {
			MetadataField c = (MetadataField) children.get(i);
			String str = c.toString2(indent + "  ");
			buf.append(str);
		}
		buf.append(indent).append("}\n");
		return buf.toString();
	}

}
