/*
 * OutOfSpaceException.java
 *
 * Created on 17 Subat 2007 Cumartesi, 16:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.evo.de.util;

/**
 *
 * @author ali.keles
 */
public class DEException extends Exception{
    
    /** Creates a new instance of OutOfSpaceException */
    public DEException() {
        super();
    }
    
    public DEException(String msg)
    {
        super(msg);
        
    }
    
}
