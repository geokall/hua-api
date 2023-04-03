package com.hua.api.utilities;

import com.hua.api.dto.FileDTO;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class MinioUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioUtil.class);

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

    public byte[] getFile(String bucketName, String objName) {
        byte[] file = null;

        try {
            boolean isExist = minioClient.bucketExists(bucketName);

            if (isExist) {
                InputStream inputStream = minioClient.getObject(bucketName, objName);
                file = IOUtils.toByteArray(inputStream);

                LOGGER.info("Fetched file fro minio with object name:" + objName);
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }

        return file;
    }

    @SneakyThrows
    public List<FileDTO> getFilesByUsername(String username, LocalDate from, LocalDate to) {
        List<FileDTO> files = new ArrayList<>();

        LOGGER.info("Fetching all files by username: " + username);
        Iterable<Result<Item>> results = minioClient.listObjects(username);

        for (Result<Item> result : results) {
            Object fileName = result.get().get("Key");
            Object lastModifiedKey = result.get().get("LastModified");

            LocalDate lastModifiedDate = LocalDate.parse(lastModifiedKey.toString(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));

            if (lastModifiedDate.isAfter(from) && lastModifiedDate.isBefore(to)) {
                byte[] file = getFile(username, String.valueOf(fileName));
                String fileEncoded = Base64.getEncoder().encodeToString(file);

                FileDTO fileDTO = new FileDTO();

                fileDTO.setActualFile(fileEncoded);
                fileDTO.setFileName(String.valueOf(fileName));

                files.add(fileDTO);
            }
        }

        return files;
    }
}
