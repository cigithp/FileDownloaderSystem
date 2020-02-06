package com.app.solution.controller;

import com.app.solution.exceptions.WebException;
import com.app.solution.model.DownloadMetrics;
import com.app.solution.model.FileDetail;
import com.app.solution.service.FDSService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class FileV1ResourceTest {

    @InjectMocks
    FileResourceV1 fileResourceV1;

    @Mock
    FDSService fdsService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void test_download_success() throws WebException {
        DownloadMetrics d = new DownloadMetrics();
        when(fdsService.createDownload(any())).thenReturn(d);
        DownloadMetrics dm = fileResourceV1.download(new HashMap<>());
        Assert.assertNotNull(dm);
    }

    @Test
    public void test_download_failure() throws WebException {
        when(fdsService.createDownload(any())).thenReturn(null);
        Assert.assertNull(fileResourceV1.download(new HashMap<>()));
    }


    @Test
    public void test_get_fileStatus_success() {
        when(fdsService.get(any())).thenReturn(Optional.of(new FileDetail()));
        Assert.assertNotNull(fileResourceV1.fileStatus(any()));
    }

    @Test
    public void test_get_fileStatus_failure() {
        when(fdsService.get(any())).thenReturn(null);
        Assert.assertNull(fileResourceV1.fileStatus(any()));
    }

    @Test
    public void test_get_download_success() {
        when(fdsService.getDownload(any())).thenReturn(Optional.of(new DownloadMetrics()));
        Assert.assertNotNull(fileResourceV1.status(any()));
    }

    @Test
    public void test_get_download_failure() {
        when(fdsService.getDownload(any())).thenReturn(null);
        Assert.assertNull(fileResourceV1.status(any()));
    }


}
