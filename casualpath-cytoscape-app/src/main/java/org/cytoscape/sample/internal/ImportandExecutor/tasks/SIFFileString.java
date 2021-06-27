/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.sample.internal.ImportandExecutor.tasks;

/**
 *
 * @author francescoceccarelli
 */
public class SIFFileString {
    
    private static String fileName;
    
    private SIFFileString(){}
    

    public static String getInstance() {
       if(fileName == null){
            fileName = new String();
        }
        return fileName;
    }
    
    public static void setInstance(String file){
        fileName = file;
    }
    
    
}
