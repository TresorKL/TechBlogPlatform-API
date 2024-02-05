package com.example.techblogplatformapi.services;


import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

     String uploadFile(MultipartFile file);

     byte[]downloadFile(String filename);

     String deleteFile(String fileName);
}
