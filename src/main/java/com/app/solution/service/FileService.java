package com.app.solution.service;

import com.app.solution.dao.IFileDetailDAO;
import com.app.solution.model.FileDetail;
import com.app.solution.util.ClientFactory;
import com.app.solution.util.Protocol;
import com.app.solution.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface FileService {
    FileDetail download(FileDetail fd);

    @Service
    @Transactional
    class FileServiceImpl implements FileService {

        @Autowired
        IFileDetailDAO fileDetail;

        @Autowired
        ApplicationContext context;

        @Override
        @Async
        public FileDetail download(FileDetail fd) {
            try {
                switch (Protocol.valueOf(fd.getProtocol())) {
                    case HTTP:
                    case HTTPS:
                        ClientFactory httpClient = context.getBean(ClientFactory.class);
                        httpClient.downloadHTTP(fd.getSource(), fd.getDestination());
                        fd = fileDetail.getOne(fd.getId());
                        fd.setStatus(Status.SUCCESS.name());
                        fileDetail.saveAndFlush(fd);
                        break;
                    case FTP:
                        ClientFactory ftpClient = context.getBean(ClientFactory.class);
                        String prefix = fd.getProtocol().toLowerCase()+ "://";
                        String host = fd.getSource().substring(prefix.length(), fd.getSource().lastIndexOf("/"));
                        boolean success = ftpClient.downloadFTP(host, fd.getName(), fd.getDestination());
                        if (success) {
                            fd = fileDetail.getOne(fd.getId());
                            fd.setStatus(Status.SUCCESS.name());
                            fileDetail.saveAndFlush(fd);
                        } else {
                            fd = fileDetail.getOne(fd.getId());
                            fd.setStatus(Status.FAILURE.name());
                            fileDetail.saveAndFlush(fd);
                        }
                        break;
                    default:
                        fd = fileDetail.getOne(fd.getId());
                        fd.setStatus(Status.FAILURE.name());
                        fileDetail.saveAndFlush(fd);
                }
            } catch (Exception e) {
                e.printStackTrace();
                fd = fileDetail.getOne(fd.getId());
                fd.setStatus(Status.FAILURE.name());
                fileDetail.saveAndFlush(fd);
            }
            return fd;
        }
    }
}
