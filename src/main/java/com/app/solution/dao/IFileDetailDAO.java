package com.app.solution.dao;

import com.app.solution.model.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IFileDetailDAO extends
        JpaRepository<FileDetail, UUID> {}
