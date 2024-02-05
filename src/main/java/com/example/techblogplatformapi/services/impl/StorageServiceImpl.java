package com.example.techblogplatformapi.services.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.techblogplatformapi.services.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;


    @Override
    public String uploadFile(MultipartFile file) {

        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));
        fileObj.delete();
        return fileName;
    }

    private File convertMultiPartFileToFile(MultipartFile file) {

        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try(FileOutputStream fos=new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());

        }catch(IOException e){

            log.error("Error converting MultiPartFile to File");
        }

     return convertedFile;

    }

    @Override
    public byte[] downloadFile(String filename) {

        S3Object s3Object= s3Client.getObject(bucketName,filename);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        try{
            return IOUtils.toByteArray(inputStream);
        }catch(IOException io){
            io.printStackTrace();
        }

        return null;
    }

    @Override
    public String deleteFile(String fileName) {

        s3Client.deleteObject(bucketName,fileName);
        return fileName +" deleted...";
    }
}
