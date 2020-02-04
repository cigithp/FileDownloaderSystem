package com.app.solution.util;

import com.app.solution.constants.ApplicationConstants;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

@Component
public class ClientFactory {

    @Autowired
    Environment environment;

    public boolean downloadHTTP(String url, String destination) {
        ReadableByteChannel readableByteChannel = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        boolean success;
        int count = 0;
        int retryCount = Integer.parseInt(Objects.requireNonNull(
                environment.getProperty(ApplicationConstants.RETRIES_COUNT)));
        do {
            try {
                URL downloadURL = new URL(url);
                readableByteChannel = Channels.newChannel(downloadURL.openStream());
                fileOutputStream = new FileOutputStream(destination);
                fileChannel = fileOutputStream.getChannel();
                fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                success = true;
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

    public boolean downloadFTP(String host, String name, String destination) {
        OutputStream outputStream = null;
        FTPClient ftpClient = null;
        boolean success = false;
        int count = 0;
        int retryCount = Integer.parseInt(Objects.requireNonNull(
                environment.getProperty(ApplicationConstants.RETRIES_COUNT)));
        do {
            try {
                ftpClient = new FTPClient();
                ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
                ftpClient.connect(host);
                ftpClient.login("anonymous", "anonymous");
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                outputStream = new BufferedOutputStream(new FileOutputStream(destination));
                success = ftpClient.retrieveFile(name, outputStream);
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
