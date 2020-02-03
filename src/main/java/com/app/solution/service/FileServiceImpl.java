package com.app.solution.service;

import com.app.solution.dao.IFileDetail;
import com.app.solution.model.FileDetail;
import com.app.solution.util.Protocol;
import com.app.solution.util.Status;
import com.app.solution.util.URLValidator;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final String LOCATION = "local.store.location";
    private final String DOWNLOAD_THRESHOLD = "size.download.threshold";

    @Autowired
    IFileDetail fileDetail;
    @Autowired
    Environment environment;

    @Override
    public List<FileDetail> download(List<String> urls) {
        //read the urls
        List<FileDetail> result = new ArrayList<>();
        FileDetail fd = null;
        for(String url : urls) {
            //validate the url
            if(URLValidator.validate(url)) {
                String name = url.substring(url.lastIndexOf("/") + 1);
                String protocol = url.substring(0, url.indexOf(":"));
                try {
                    Path parentDir = Paths.get("..", environment.getProperty(LOCATION));
                    File dir = new File(String.valueOf(parentDir));
                    if(!dir.exists())
                        dir.mkdir();
                    //create temp files?? for storing chunks??
                    //based on size divide files and store
                    // use temp files and merge all temp files to one single file
                    //parallel execution??
                    Path finalPath = Paths.get(String.valueOf(parentDir), name);
                    Files.deleteIfExists(finalPath);
                    Path path = Files.createFile(finalPath);
                    String destination = path.toString();
                    fd = new FileDetail(name, url, destination, Protocol.getName(protocol.toUpperCase()), Status.IN_PROGRESS.name());
                    fileDetail.save(fd);
                    switch(Protocol.valueOf(protocol.toUpperCase())) {
                        case HTTP:
                        case HTTPS:
                            //create a HTTP downloader that will
                                //check the size of the file to be downloaded
                                //if it is below the threshold = download it in a single thread
                                //if it is above = spawn multiple threads and download it
                                //return
                            ReadableByteChannel readableByteChannel = null;
                            FileOutputStream fileOutputStream = null;
                            FileChannel fileChannel = null;
                            try {
                                URL downloadURL = new URL(url);
                                readableByteChannel = Channels.newChannel(downloadURL.openStream());
                                fileOutputStream = new FileOutputStream(destination);
                                fileChannel = fileOutputStream.getChannel();
                                fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                            } catch(IOException ex) {

                            } finally {
                                if(fileOutputStream != null)
                                    fileOutputStream.close();
                                if(readableByteChannel != null)
                                    readableByteChannel.close();
                                if(fileChannel != null)
                                    fileChannel.close();
                            }
                            fd = fileDetail.getOne(fd.getId());
                            fd.setStatus(Status.SUCCESS.name());
                            fileDetail.saveAndFlush(fd);
                            break;
                        case FTP:
                            //create a FTP downloader that will
                                //check the size of the file to be downloaded
                                //if it is below the threshold = download it in a single thread
                                //if it is above = spawn multiple threads and download it
                                //return
                            OutputStream outputStream = null;
                            FTPClient ftpClient = null;
                            try {
                                ftpClient = new FTPClient();
                                ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
                                ftpClient.connect("speedtest.tele2.net");
                                ftpClient.login("anonymous", "anonymous");
                                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                                ftpClient.enterLocalPassiveMode();
                                outputStream = new BufferedOutputStream(new FileOutputStream(destination));
                                boolean success = ftpClient.retrieveFile(name, outputStream);
                                if(success) {
                                    fd = fileDetail.getOne(fd.getId());
                                    fd.setStatus(Status.SUCCESS.name());
                                    fileDetail.saveAndFlush(fd);
                                } else {
                                    fd = fileDetail.getOne(fd.getId());
                                    fd.setStatus(Status.FAILURE.name());
                                    fileDetail.saveAndFlush(fd);
                                }
                            } catch (IOException ioex) {

                            } finally {
                                if(ftpClient != null && ftpClient.isConnected()) {
                                    ftpClient.logout();
                                    ftpClient.disconnect();
                                }
                                if(outputStream != null)
                                    outputStream.close();
                            }
                            break;
                        default:
                            fd = fileDetail.getOne(fd.getId());
                            fd.setStatus(Status.FAILURE.name());
                            fileDetail.saveAndFlush(fd);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    fd = fileDetail.getOne(fd.getId());
                    fd.setStatus(Status.FAILURE.name());
                    fileDetail.saveAndFlush(fd);
                }
            }
            result.add(fd);
        }
        return result;
    }
}
