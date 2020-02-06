package com.app.solution.util;

import com.app.solution.constants.ApplicationConstants;
import com.app.solution.model.FileDetail;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

@Component
public class ClientFactory {
    private static final Logger logger = LoggerFactory.getLogger(ClientFactory.class);
    @Autowired
    Environment environment;

    public boolean downloadHTTP(FileDetail fileDetail) {
        ReadableByteChannel readableByteChannel = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        boolean success;
        int count = 0;
        int retryCount = Integer.parseInt(Objects.requireNonNull(
                environment.getProperty(ApplicationConstants.RETRIES_COUNT)));
        do {
            try {
                long startTime = System.currentTimeMillis();
                URL downloadURL = new URL(fileDetail.getSource());
                URLConnection urlConnection = downloadURL.openConnection();
                urlConnection.connect();
                fileDetail.setSize(
                        urlConnection.getContentLengthLong() > 0
                                ? String.valueOf(urlConnection.getContentLengthLong())
                                : "0");
                urlConnection.getInputStream().close();
                readableByteChannel = Channels.newChannel(downloadURL.openStream());
                fileOutputStream = new FileOutputStream(fileDetail.getDestination());
                fileChannel = fileOutputStream.getChannel();
                fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                success = true;
                long endTime = System.currentTimeMillis();
                logger.info("Download completed in {} milliseconds", (endTime - startTime));
            } catch (IOException ex) {
                success = false;

            } finally {
                try {
                    if (fileOutputStream != null)
                        fileOutputStream.close();
                    if (readableByteChannel != null)
                        readableByteChannel.close();
                    if (fileChannel != null)
                        fileChannel.close();
                } catch (Exception ex) {
                    success = false;
                }
            }
            count++;
        } while (!success && count < retryCount);
        return success;
    }

    public boolean downloadFTP(FileDetail fileDetail) {
        OutputStream outputStream = null;
        FTPClient ftpClient = null;
        boolean success = false;
        int count = 0;
        int retryCount = Integer.parseInt(Objects.requireNonNull(
                environment.getProperty(ApplicationConstants.RETRIES_COUNT)));
        String prefix = fileDetail.getProtocol().toLowerCase()+ "://";
        String host = fileDetail.getSource().substring(prefix.length(), fileDetail.getSource().lastIndexOf("/"));
        do {
            try {
                long startTime = System.currentTimeMillis();
                ftpClient = new FTPClient();
                ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
                ftpClient.connect(host);
                ftpClient.login("anonymous", "anonymous");
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                ftpClient.sendCommand("SIZE", fileDetail.getName());
                String reply = ftpClient.getReplyString();
                fileDetail.setSize(reply.substring(reply.indexOf(" ")).trim());
                outputStream = new BufferedOutputStream(new FileOutputStream(fileDetail.getDestination()));
                success = ftpClient.retrieveFile(fileDetail.getName(), outputStream);
                long endTime = System.currentTimeMillis();
                logger.info("Download completed in {} milliseconds", (endTime - startTime));
            } catch (IOException ioex) {
                success = false;
            } finally {
                try {
                    if (ftpClient != null && ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                    if (outputStream != null)
                        outputStream.close();
                } catch (Exception ex) {
                    success = false;
                }
            }
            count++;
        } while (!success && count < retryCount);
        return success;
    }
}
