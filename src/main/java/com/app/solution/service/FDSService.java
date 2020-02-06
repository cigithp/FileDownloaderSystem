package com.app.solution.service;

import com.app.solution.constants.ApplicationConstants;
import com.app.solution.dao.IDownloadMetrics;
import com.app.solution.dao.IFileDetailDAO;
import com.app.solution.model.DownloadMetrics;
import com.app.solution.model.FileDetail;
import com.app.solution.util.Protocol;
import com.app.solution.util.Status;
import com.app.solution.util.URLValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Optional<FileDetail> get(UUID id);
    DownloadMetrics createDownload(List<String> urls);
    Optional<DownloadMetrics> getDownload(UUID id);

    @Service
    class FDSServiceImpl implements FDSService {
        private static final Logger logger = LoggerFactory.getLogger(FDSServiceImpl.class);
        private static final String INITIALIZER = "-";
        @Autowired
        IFileDetailDAO fileDetail;

        @Autowired
        IDownloadMetrics downloadMetrics;

        @Autowired
        Environment environment;

        @Autowired
        FileService fileService;

        private List<FileDetail> create(List<String> urls, DownloadMetrics dm) {
            FileDetail fd = null;
            List<FileDetail> result = new ArrayList<>();
            for (String url : urls) {
                fd = new FileDetail(INITIALIZER, INITIALIZER, INITIALIZER, Protocol.NONE.name(), Status.NONE.name());
                if (URLValidator.validate(url)) {
                    String name = url.substring(url.lastIndexOf("/") + 1);
                    String protocol = url.substring(0, url.indexOf(":")).toUpperCase();
                    dm.setTotalFiles(dm.getTotalFiles() + 1);
                    try {
                        Path parentDir = Paths.get("..", environment.getProperty(ApplicationConstants.LOCATION));
                        File dir = new File(String.valueOf(parentDir));
                        if (!dir.exists())
                            dir.mkdir();
                        Path finalPath = Paths.get(String.valueOf(parentDir), name);
                        Files.deleteIfExists(finalPath);
                        Path path = Files.createFile(finalPath);
                        String destination = path.toString();
                        fd.setStatus(Status.IN_PROGRESS.name());
                        fd.setName(name);
                        fd.setSource(url);
                        fd.setDestination(destination);
                        fd.setProtocol(Protocol.getName(protocol));
                        fd.setDownloadId(dm.getId());
                        fileDetail.save(fd);
                        FileDetail finalFd = fd;
                        CompletableFuture<FileDetail> completableFuture =
                                CompletableFuture.supplyAsync(() -> fileService.download(finalFd));
                        completableFuture.get();
                    } catch (IOException | InterruptedException | ExecutionException ex) {
                        fd = fileDetail.getOne(fd.getId());
                        fd.setStatus(Status.FAILURE.name());
                        fileDetail.saveAndFlush(fd);
                    }
                } else {
                    logger.error("URL Validation failed.");
                }
                result.add(fd);
            }
            return result;
        }

        @Override
        public Optional<FileDetail> get(UUID id) {
            return fileDetail.findById(id);
        }

        @Override
        public DownloadMetrics createDownload(List<String> urls) {
            DownloadMetrics dm = new DownloadMetrics(Status.IN_PROGRESS.name());
            downloadMetrics.save(dm);
            create(urls, dm);
            return dm;
        }

        @Override
        public Optional<DownloadMetrics> getDownload(UUID id) {
            return downloadMetrics.findById(id);
        }
    }
}