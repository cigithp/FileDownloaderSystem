package com.app.solution.controller;

import com.app.solution.model.FileDetail;
import com.app.solution.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileResourceV1 {

    @Autowired
    FileService fileService;

    @PostMapping("/download")
    public List<FileDetail> download(@RequestBody Map<String, List<String>> body) {
        return fileService.download(body.get("urls"));
    }
}
