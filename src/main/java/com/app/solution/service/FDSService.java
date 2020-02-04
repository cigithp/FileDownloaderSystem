package com.app.solution.service;

import com.app.solution.constants.ApplicationConstants;
import com.app.solution.dao.IFileDetailDAO;
import com.app.solution.model.FileDetail;
import com.app.solution.util.Protocol;
import com.app.solution.util.Status;
import com.app.solution.util.URLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface FDSService {
    List<String[]> create(List<String> urls);
    Optional<FileDetail> get(UUID id);

    @Service
    class FDSServiceImpl implements FDSService {
        @Autowired
        IFileDetailDAO fileDetail;

        @Autowired
        Environment environment;

        @Autowired
        FileService fileService;

        @Override
        public List<String[]> create(List<String> urls) {
            FileDetail fd = null;
            List<String[]> result = new ArrayList<>();
            for (String url : urls) {
                if (URLValidator.validate(url)) {
                    String name = url.substring(url.lastIndexOf("/") + 1);
                    String protocol = url.substring(0, url.indexOf(":")).toUpperCase();
                    try {
                        Path parentDir = Paths.get("..", environment.getProperty(ApplicationConstants.LOCATION));
                        File dir = new File(String.valueOf(parentDir));
                        if (!dir.exists())
                            dir.mkdir();
                        Path finalPath = Paths.get(String.valueOf(parentDir), name);
                        Files.deleteIfExists(finalPath);
                        Path path = Files.createFile(finalPath);
                        String destination = path.toString();
                        fd = new FileDetail(name, url, destination, Protocol.getName(protocol), Status.IN_PROGRESS.name());
                        fileDetail.save(fd);
                        result.add(new String[]{fd.getName(), fd.getId().toString()});
                        FileDetail finalFd = fd;
                        CompletableFuture<FileDetail> completableFuture =
                                CompletableFuture.supplyAsync(() -> fileService.download(finalFd));
                        completableFuture.get();
                    } catch (IOException | InterruptedException | ExecutionException ex) {
                        fd = fileDetail.getOne(fd.getId());
                        fd.setStatus(Status.FAILURE.name());
                        fileDetail.saveAndFlush(fd);
                        result.add(new String[]{fd.getName(), fd.getId().toString()});
                    }
                } else {
                    //log
                    System.out.println("URL Validation failed.");
                    result.add(null);
                }
            }
            return result;
        }

        @Override
        public Optional<FileDetail> get(UUID id) {
            return Optional.of(fileDetail.getOne(id));
        }
    }
}
