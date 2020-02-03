package com.app.solution.service;

import com.app.solution.model.FileDetail;

import java.util.List;

public interface FileService {
    List<FileDetail> download(List<String> urls);
}
