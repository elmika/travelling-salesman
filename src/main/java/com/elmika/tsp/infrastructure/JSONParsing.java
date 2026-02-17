package com.elmika.tsp.infrastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elmika.tsp.application.ProblemConfiguration;

public class JSONParsing {

    private static final Logger log = LoggerFactory.getLogger(JSONParsing.class);

    public static JSONObject read(String filename) {
        JSONObject json = null;
        File f = new File(filename);
        if (f.exists()) {
            try {
                InputStream is = new FileInputStream(filename);
                String jsonTxt = IOUtils.toString(is, "UTF-8");
                json = new JSONObject(jsonTxt);
            } catch (Exception e) {
                log.warn("Exception when reading json file: {}", e.getMessage());
            }
        } else {
            log.warn("Could not find json file: {}", filename);
        }
        return json;
    }

    public static void test() {
        log.info("RUNNING TEST.");
        JSONObject json = read("file.json");
        try {
            String a = json.getString("yes");
            log.info(a);
        } catch (Exception e) {
            log.warn("Exception when retrieving json value: {}", e.getMessage());
        }
    }

    public static ProblemConfiguration getConfig() {
        String filename = "problemConfiguration.json";
        String problem = "simple";
        String strategy = "random10";

        JSONObject json = read(filename);
        if (json == null) {
            log.info("Default configuration loaded");
            return new ProblemConfiguration(problem, strategy);
        }
        try {
            problem = json.getString("problem");
            strategy = json.getString("resolutionStrategy");
        } catch (Exception e) {
            log.info("Default configuration loaded");
            return new ProblemConfiguration(problem, strategy);
        }
        log.info("Problem: {}, Strategy: {}", problem, strategy);
        return new ProblemConfiguration(problem, strategy);
    }
}
