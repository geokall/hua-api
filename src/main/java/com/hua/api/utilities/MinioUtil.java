package com.hua.api.utilities;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Component
public class MinioUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioUtil.class);

    public static final String HUA_BUCKET = "hua";

    @Value("${hua.minio.endpoint}")
    private String endPoint;

    @Value("${hua.minio.access.key}")
    private String accessKey;

    @Value("${hua.minio.secret.key}")
    private String secretKey;

    private MinioClient minioClient;

    @PostConstruct
    public void initMinioClient() {
        try {
            minioClient = new MinioClient(endPoint, accessKey, secretKey);
        } catch (InvalidEndpointException | InvalidPortException e) {
            LOGGER.info(e.getMessage());
        }
    }

    public void uploadFile(String bucketName, String objName, InputStream inputStream, Long length, String contentType) {
        try {
            boolean isExist = minioClient.bucketExists(bucketName);

            if (!isExist) {
                minioClient.makeBucket(bucketName);
                LOGGER.info("created bucket with name:" + bucketName);
            }

            minioClient.putObject(bucketName, objName, inputStream, length, contentType);
            LOGGER.info("saved file on bucket");

        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }
}
