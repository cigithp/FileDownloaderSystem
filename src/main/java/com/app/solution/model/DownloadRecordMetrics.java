package com.app.solution.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadRecordMetrics {
    List<FileDetail> fileDetailList;
    BigDecimal percentFailures;
    int successCount;
    long totalDownloadSize;
    int remainingFiles;

    public int getRemainingFiles() {
        return remainingFiles;
    }

    public void setRemainingFiles(int remainingFiles) {
        this.remainingFiles = remainingFiles;
    }

    public DownloadRecordMetrics(List<FileDetail> fileDetailList, BigDecimal percentFailures,
                                 int successCount, long totalDownloadSize) {
        this.fileDetailList = fileDetailList;
        this.percentFailures = percentFailures;
        this.successCount = successCount;
        this.totalDownloadSize = totalDownloadSize;
    }

    public List<FileDetail> getFileDetailList() {
        return fileDetailList;
    }

    public void setFileDetailList(List<FileDetail> fileDetailList) {
        this.fileDetailList = fileDetailList;
    }

    public BigDecimal getPercentFailures() {
        return percentFailures;
    }

    public void setPercentFailures(BigDecimal percentFailures) {
        this.percentFailures = percentFailures;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public long getTotalDownloadSize() {
        return totalDownloadSize;
    }

    public void setTotalDownloadSize(long totalDownloadSize) {
        this.totalDownloadSize = totalDownloadSize;
    }
}
