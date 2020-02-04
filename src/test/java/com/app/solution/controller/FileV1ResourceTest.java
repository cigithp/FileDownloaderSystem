package com.app.solution.controller;

import com.app.solution.service.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class FileV1ResourceTest {

    @Mock
    FileService fileService;

    @Test
    public void test_http_download_success() {

    }

    @Test
    public void test_http_download_failure() {

    }


    @Test
    public void test_ftp_download_success() {

    }

    @Test
    public void test_ftp_download_failure() {

    }


}
