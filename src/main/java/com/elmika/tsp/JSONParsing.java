package com.elmika.tsp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

import org.json.JSONObject;

public class JSONParsing {

    public static void test() {     

        System.out.println("RUNNING TEST.");

        File f = new File("file.json");
        if (f.exists()){
            try {     
                 System.out.println("File exists.");       
                InputStream is = new FileInputStream("file.json");
                String jsonTxt = IOUtils.toString(is, "UTF-8");
                System.out.println(jsonTxt);
                JSONObject json = new JSONObject(jsonTxt);       
                String a = json.getString("yes");
                System.out.println(a);
            } catch(Exception e){
                System.out.println("Exception has been thrown");
            }
        } else {
            System.out.println("Could not find file.json");
        }
    }
}