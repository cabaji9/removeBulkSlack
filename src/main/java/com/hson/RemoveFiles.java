package com.hson;

import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by Hyun Woo Son on 11/20/17.
 */
public class RemoveFiles {

    private final static Logger logger = LoggerFactory.getLogger(RemoveFiles.class);


    public static void main(String args[]) throws Exception {
        Properties prop= loadProperties();

        String token = prop.getProperty("token");
        String userId = prop.getProperty("userId");

        logger.info("token {} and userid {}",token,userId);

        List<String> filesIdList = obtainIdFiles(token, userId);
        deleteFiles(filesIdList, token);
    }


    private static Properties loadProperties() throws Exception {
        Properties prop = new Properties();
        String filename = "config.properties";
        InputStream input = RemoveFiles.class.getClassLoader().getResourceAsStream(filename);
        if (input == null) {
            throw new Exception("Sorry, unable to find "+filename);
        }
        //load a properties file from class path, inside static method
        prop.load(input);
        return prop;
    }


    private static List<String> obtainIdFiles(String token, String userId) throws IOException {
        List<String> fileList = new ArrayList<>();

        String pathObtainFiles = "/api/files.list";
        WebClient webClient = RestUtil.getWebClient(pathObtainFiles, "https://slack.com");
        webClient.query("token", token);
        webClient.query("user", userId);
        Response response = webClient.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            HashMap<String, Object> result = RestUtil.obtainEntity(response, HashMap.class);
            if (result.get("ok").toString().equals("true")) {
                List<Object> filesList = (List) result.get("files");
                if (filesList != null && !filesList.isEmpty()) {
                    for (Object file : filesList) {
                        HashMap<String, Object> fileMap = (HashMap<String, Object>) file;
                        String idFile = (String) fileMap.get("id");
                        fileList.add(idFile);
                    }
                }
            } else {
                logger.error("Error on rest service  {}", result.get("error"));
            }
        } else {
            logger.error("Error calling rest service http status: {}", response.getStatus());
        }
        logger.info("obtainIdFiles | File list size {} ,File list:  {}", fileList.size(), fileList);
        return fileList;
    }


    private static void deleteFiles(List<String> filesIdList, String token) throws IOException {
        String pathDeleteFiles = "/api/files.delete";
        if (filesIdList != null && !filesIdList.isEmpty()) {
            filesIdList.forEach(fileId -> {
                deleteFile(fileId, token, pathDeleteFiles);
            });
        }
    }


    private static void deleteFile(String fileId, String token, String pathDeleteFiles) {
        try {
            WebClient webClient = RestUtil.getWebClient(pathDeleteFiles, "https://slack.com");
            webClient.query("token", token);
            webClient.query("file", fileId);
            Response response = webClient.get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {

                HashMap<String, Object> result = RestUtil.obtainEntity(response, HashMap.class);
                if (result.get("ok").toString().equals("true")) {
                    logger.info("deleteFiles | File deleted : {}", fileId);

                } else {
                    logger.error("deleteFiles | Error on rest service {} with fileId {}", result.get("error"), fileId);
                }
            } else {
                logger.error("deleteFiles | Error calling rest service http status: {}", response.getStatus());
            }
        } catch (Exception e) {
            logger.error("deleteFiles | Error on calling rest {} ", e.getMessage(), e);
        }
    }


}
