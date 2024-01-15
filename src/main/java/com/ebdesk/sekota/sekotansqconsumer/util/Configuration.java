/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebdesk.sekota.sekotansqconsumer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * 
 */
public class Configuration {
    
//    static final String DEFAULT_CONFIG = "config-main.properties";
    public static String DEFAULT_CONFIG = "config.properties";
    private Properties prop;
    private InputStream input = null;
    
    private static Configuration instance;

    public static Configuration config() {
        if (configurationValidation(DEFAULT_CONFIG))
        {
            instance = new Configuration();
        }
        return instance;
    }
    
    public static Configuration config(String fileName) {
        if (configurationValidation(fileName))
        {
            instance = new Configuration(fileName);
        }
        return instance;
    }
    
    private static Boolean configurationValidation(String fileName)
    {
        Boolean result = false;
        File f = new File(fileName);
        if(f.exists() && !f.isDirectory() && f.canRead()) {
            String ext = FilenameUtils.getExtension(fileName);
            if (ext.equals("properties"))
            {
                result = true;
            }
            else
            {
                System.out.println("File is not properties");
            }
        }
        else
        {
            System.out.println("File is not exist!");
        }
        return result;
    }
    
    private Configuration ()
    {
        this(DEFAULT_CONFIG);
    }
    
    private Configuration (String fileName) {
        try {
            prop = new Properties();
            input = new FileInputStream(fileName);
            prop.load(input);
        } catch (IOException e) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }         
    }
    
    public String getProperty(String key)
    {
        return (prop == null) ? null : prop.getProperty(key);
    }
    
    public Boolean isAvailable()
    {
        return !(input == null || prop == null || prop.isEmpty());
    }
}
