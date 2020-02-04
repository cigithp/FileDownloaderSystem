package com.app.solution.dao;

import com.app.solution.model.FileDetailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IFileDetailHistoryDAO
        extends JpaRepository<FileDetailHistory, UUID> { }
