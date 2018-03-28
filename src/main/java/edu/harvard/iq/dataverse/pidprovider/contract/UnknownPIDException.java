/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.pidprovider.contract;

/**
 * Indicates that the PID used with the current operation is not known. Thrown
 * e.g. when trying to update the metadata associated with an unknown DOI.
 * 
 * @author devel
 */
public class UnknownPIDException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownPIDException(String msg) {
		super(msg);
	}
}
