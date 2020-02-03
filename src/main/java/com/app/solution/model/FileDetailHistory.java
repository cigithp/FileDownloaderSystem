package com.app.solution.model;

import com.app.solution.util.Status;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class FileDetailHistory {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID histId;
    private UUID id;
    private String name;
    private String source;
    private String destination;
    private String protocol;
    private Status status;
    private int version;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    public FileDetailHistory(UUID id, String name, String source, String destination, String protocol, Status status, int version) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.protocol = protocol;
        this.status = status;
        this.version = version;
        this.creationDate = LocalDateTime.now();
        this.lastModified = getCreationDate(); //TODO: change
    }

    public UUID getHistId() {
        return histId;
    }

    public void setHistId(UUID histId) {
        this.histId = histId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
}
