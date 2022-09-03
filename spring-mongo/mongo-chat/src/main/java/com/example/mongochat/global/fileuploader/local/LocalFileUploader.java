package com.example.mongochat.global.fileuploader.local;


import com.sidepr.mono.sns.global.fileuploader.FileUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class LocalFileUploader implements FileUploader {

    @Value("${file.dir}")
    private String baseDir;

    @Override
    public String upload(MultipartFile file, String dirName) throws IOException {
        if(file.isEmpty()){
            return null;
        }

        String originalFilename = file.getOriginalFilename();

        String storeFilename = FileUploader.createStoreFileName(originalFilename);
        File placeToTransfer = getQualifiedFolder(dirName, storeFilename);

        log.info("store file with path = {}", getFullPath(dirName, storeFilename));
        file.transferTo(placeToTransfer);
        return dirName + "/" + storeFilename;
    }

    private File getQualifiedFolder(String dirName, String storeFilename) throws IOException {
        File placeToTransfer = new File(getFullPath(dirName, storeFilename));
        if(!placeToTransfer.exists()){
            Files.createDirectories(Path.of(baseDir + dirName));
        }
        return placeToTransfer;
    }

    private String getFullPath(String dirName, String storeFilename) {
        return baseDir + dirName + storeFilename;
    }

    private String getFullPath(String filename) {
        return baseDir + filename;
    }

    @Override
    public void delete(String filename) {
        File file = new File(getFullPath(filename));
        if(file.exists()) file.delete();
    }
}