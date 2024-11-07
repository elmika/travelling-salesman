package com.elmika.tsp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

import org.json.JSONObject;

public class JSONParsing {

    private static void log(String $message) {
        System.out.println($message);
    }

    public static JSONObject read(String filename) {
        JSONObject json = null;
        File f = new File(filename);
        if (f.exists()){
            try {     
                InputStream is = new FileInputStream(filename);
                String jsonTxt = IOUtils.toString(is, "UTF-8");
                json = new JSONObject(jsonTxt);       
            } catch(Exception e){
                log("Exception has been thrown when reading json file.");
            }
        } else {
            log("Could not find json file");
        }

        return json;
    }

    public static void test() {     

        log("RUNNING TEST.");
        JSONObject json = read("file.json");
        try {
            String a = json.getString("yes");
            log(a);
        } catch(Exception e){
            log("Exception has been thrown when retrieving json value.");
        }

    }

    public static ProblemConfiguration getConfig() {
        String filename = "problemConfiguration.json";
        String problem = "simple";  // default values
        String strategy = "random10"; // default values

        JSONObject json = read(filename);
        if (json == null) {
            log("Default configuration loaded");
            return new ProblemConfiguration(problem, strategy);
        }

        try {
            problem = json.getString("problem");
            strategy = json.getString("resolutionStrategy");
        } catch(Exception e) {
            log("Default configuration loaded");
            return new ProblemConfiguration(problem, strategy);
        }
        
        log("Problem: "+problem+", Strategy: "+strategy);
        return new ProblemConfiguration(problem, strategy);
    }
}