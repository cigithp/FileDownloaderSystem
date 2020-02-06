package com.app.solution.controller;

import com.app.solution.exceptions.WebException;
import com.app.solution.model.DownloadMetrics;
import com.app.solution.model.FileDetail;
import com.app.solution.service.FDSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@Component
public class FileResourceV1 {
    private static final Logger logger = LoggerFactory.getLogger(FileResourceV1.class);

    @Autowired
    FDSService fdsService;

    @PostMapping("/download")
    public DownloadMetrics download(@RequestBody Map<String, List<String>> body) throws WebException {
        DownloadMetrics result = null;
        try {
            result = fdsService.createDownload(body.get("urls"));
        } catch (Exception ex) {
            logger.error("Exception occurred : {}", ex.getMessage());
            throw new WebException("Exception occurred");
        }
        return result;
    }

    @GetMapping("/file/{id}/status")
    public Optional<FileDetail> fileStatus(@PathVariable("id") UUID id) {
        return fdsService.get(id);
    }

    @GetMapping("/download/{id}/status")
    public Optional<DownloadMetrics> status(@PathVariable("id") UUID id) {
        return fdsService.getDownload(id);
    }


}
