package com.app.solution.dao;

import com.app.solution.model.DownloadMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IDownloadMetrics
        extends JpaRepository<DownloadMetrics, UUID> {}
