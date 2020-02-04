package com.app.solution.controller;

import com.app.solution.model.FileDetail;
import com.app.solution.service.FDSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/file")
@Component
public class FileResourceV1 {

    @Autowired
    FDSService fdsService;

    @PostMapping("/download")
    public List<String[]> download(@RequestBody Map<String, List<String>> body) {
        return fdsService.create(body.get("urls"));
    }

    @GetMapping("/{id}/status")
    public Optional<FileDetail> status(@PathVariable("id") UUID id) {
        return fdsService.get(id);
    }


}
