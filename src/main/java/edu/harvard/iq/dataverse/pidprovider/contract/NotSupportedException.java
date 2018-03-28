/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.iq.dataverse.pidprovider.contract;

/**
 * Indicates that an operation is not supported by a plugin.
 * @author devel
 */
public class NotSupportedException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotSupportedException(String msg){
		super(msg);
	}
    
}
