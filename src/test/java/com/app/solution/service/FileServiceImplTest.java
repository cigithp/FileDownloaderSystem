package com.app.solution.service;

import com.app.solution.dao.IFileDetailDAO;
import com.app.solution.model.FileDetail;
import com.app.solution.util.ClientFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceImplTest {

    @InjectMocks
    FileService.FileServiceImpl fileService;

    @Mock
    IFileDetailDAO fileDetail;

    @Mock
    ApplicationContext context;

    @Before
    public void setup() {
        initMocks(this);
    }

    private FileDetail createMockFileDetailObject() {
        return new FileDetail();
    }
    private FileDetail createMockFileDetailObject(String protocol) {
        FileDetail fd = new FileDetail();
        fd.setProtocol(protocol);
        return fd;
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
