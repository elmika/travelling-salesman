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

    public static ProblemConfiguration getConfig(){

        log("Loading configuration.");

        String filename = "problemConfiguration.json";
        String problem = "simple";  // default values
        String strategy = "random10"; // default values

        File f = new File(filename);
        if (f.exists()){
            try {
                InputStream is = new FileInputStream(filename);
                String jsonTxt = IOUtils.toString(is, "UTF-8");
                JSONObject json = new JSONObject(jsonTxt);
                problem = json.getString("problem");
                strategy = json.getString("resolutionStrategy");
                log("Custom configuration loaded.");
            } catch(Exception e){
                log("Exception has been thrown. Default configuration loaded.");
            }
        } else {
            log("Could not find configuration file "+filename);
        }

        log("Problem: "+problem+", Strategy: "+strategy);

        return new ProblemConfiguration(problem, strategy);

    }
}