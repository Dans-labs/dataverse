/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.pidprovider.contract;

/**
 * Indicates that a PID is already in use and that the current operation is
 * forbidden for that reason. E.g. assigning a new dataset to a taken DOI.
 * 
 * @author devel
 */
public class PIDInUseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PIDInUseException(String msg){
		super(msg);
	}

}
