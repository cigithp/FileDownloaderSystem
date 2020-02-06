package com.app.solution.service;

import com.app.solution.dao.IFileDetailDAO;
import com.app.solution.model.FileDetail;
import com.app.solution.util.ClientFactory;
import com.app.solution.util.Protocol;
import com.app.solution.util.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
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
                        if(httpClient.downloadHTTP(fd)) {
                            fd.setStatus(Status.SUCCESS.name());
                        } else {
                            fd.setStatus(Status.FAILURE.name());
                        }
                        fileDetail.saveAndFlush(fd);
                        break;
                    case FTP:
                        ClientFactory ftpClient = context.getBean(ClientFactory.class);
                        if (ftpClient.downloadFTP(fd)) {
                            fd.setStatus(Status.SUCCESS.name());
                        } else {
                            fd.setStatus(Status.FAILURE.name());
                        }
                        fileDetail.saveAndFlush(fd);
                        break;
                    default:
                        fd.setStatus(Status.FAILURE.name());
                        fileDetail.saveAndFlush(fd);
                }
            } catch (Exception e) {
                e.printStackTrace();
                fd.setStatus(Status.FAILURE.name());
                fileDetail.saveAndFlush(fd);
            }
            return fd;
        }
    }
}
