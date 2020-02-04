package com.app.solution.service;

import com.app.solution.model.FileDetail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class FileServiceImplTest {

    @Mock
    FileDetail fileDetail;

    @Mock
    Environment environment;

    @Before
    public void setup() {

    }

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
