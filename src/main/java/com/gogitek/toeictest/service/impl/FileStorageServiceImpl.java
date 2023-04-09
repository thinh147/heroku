package com.gogitek.toeictest.service.impl;

import com.gogitek.toeictest.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path fileStorageLocation;

    private final PlatformTransactionManager transactionManager;

    @Autowired
    public FileStorageServiceImpl(Environment env, PlatformTransactionManager transactionManager) {
        this.fileStorageLocation = Paths.get(env.getProperty("app.file.upload-dir", "./upload-file"))
                .toAbsolutePath().normalize();
        this.transactionManager = transactionManager;
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public Resource loadFiles(String fileName) {
        try {
            var file = Paths.get(fileStorageLocation + "/" + fileName);
            var res = new UrlResource(file.toUri());

            if (res.exists() || res.isReadable())
                return res;
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        String[] fileNameParts = fileName.split("\\.");

        return fileNameParts[fileNameParts.length - 1];
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        var fileName =
                new Date().getTime() + "-file." + getFileExtension(file.getOriginalFilename());

        try {
            // Check if the filename contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException(
                        "Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
        return fileName;
    }

    public String save(MultipartFile[] files){
        try{
            String message = "";
            List<String> filesNames = new ArrayList<>();
            Arrays.stream(files).forEach(file -> {
                DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
                TransactionStatus transaction = transactionManager.getTransaction(definition);
                try{
                    storeFile(file);
                    transactionManager.commit(transaction);
                }catch (Exception e){
                    transactionManager.rollback(transaction);
                    throw new RuntimeException(e.getMessage());
                }
                filesNames.add(file.getOriginalFilename());
            });
            message = "upload successfully" + filesNames;
            return  message;
        }catch (Exception e){
            throw  new RuntimeException("Loi");
        }
    }
}
