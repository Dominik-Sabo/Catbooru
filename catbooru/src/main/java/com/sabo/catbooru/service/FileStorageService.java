package com.sabo.catbooru.service;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class FileStorageService {

    private final Path root = Paths.get("target\\classes\\Pictures");

    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path path = root.resolve(filename);
            return new UrlResource(path.toUri());
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            return null;
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }
}
