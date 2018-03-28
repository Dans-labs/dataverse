/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.pidprovider.contract;

/**
 * Indicates an internal exception at the PID providers systems.
 * @author devel
 */
public class PIDProviderException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public PIDProviderException(Exception e){
		super(e);
	}
	
	
	public PIDProviderException(String s){
		super(s);
	}
    
}
