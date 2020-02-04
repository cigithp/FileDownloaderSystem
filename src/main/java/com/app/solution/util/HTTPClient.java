package com.app.solution.util;

import com.app.solution.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

public class HTTPClient implements Runnable {
    private String source = "";
    private String destination = "";

    @Autowired
    Environment environment;

    public HTTPClient(String source, String destination) {
        this.source = source;
        this.destination = destination;
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

    public boolean downloadHTTP() {
        ReadableByteChannel readableByteChannel = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        boolean success;
        int count = 0;
        int retryCount = Integer.parseInt(Objects.requireNonNull(
                environment.getProperty(ApplicationConstants.RETRIES_COUNT)));
        do {
            try {
                URL downloadURL = new URL(source);
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

    @Override
    public void run() {
        downloadHTTP();
    }
}
