package com.tt.unitify.modules.utils;

import com.google.cloud.storage.Blob;
import com.google.firebase.cloud.StorageClient;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Log4j2
public class FirebaseUtil {
    public static final String UTILITY_CLASS = "Utility class";

    private FirebaseUtil() {
        throw new IllegalStateException(UTILITY_CLASS);
    }
    private static final String BUCKET_NAME = "unitify-dc8f2.appspot.com";
    public static String uploadFile(byte[] file, String remoteFileName) {
        try {
            // Use StorageClient to interact with Firebase Storage
            StorageClient storageClient = StorageClient.getInstance();
            Blob fileSaved= storageClient.bucket(BUCKET_NAME).create(remoteFileName, file);
            log.info("File uploaded successfully. URL: {}",fileSaved.getMediaLink());
            return fileSaved.getMediaLink();
        } catch (Exception e) {
            log.info("Error uploading file: {} {}",e.getMessage(),e.getStackTrace());
            return null;
        }

    }

    public static byte [] downloadFile(String fileName){
        try {
            StorageClient storageClient = StorageClient.getInstance();
            byte[] file= storageClient.bucket(BUCKET_NAME).get(fileName).getContent();
            log.info("File downloaded successfully. {}",fileName);
            return file;
        }catch (Exception e){
            log.info("Error downloading file: {} {}",e.getMessage(),e.getStackTrace());
            return new byte[0];
        }
    }
}
