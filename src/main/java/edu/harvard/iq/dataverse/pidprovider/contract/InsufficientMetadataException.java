/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.pidprovider.contract;

/**
 * Indicates insufficient metadata in the dataset. This exception shall contain
 * a message explaining what is wrong with the metadata, so it can be shown to
 * the user.
 * 
 * @author devel
 */
public class InsufficientMetadataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficientMetadataException(String msg) {
		super(msg);
	}

}
