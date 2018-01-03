package com.butchjgo.linkservice;

import com.butchjgo.linkservice.common.domain.RegisterInfo;
import com.butchjgo.linkservice.common.domain.RequestURL;
import com.butchjgo.linkservice.service.RegisterService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LinkserviceApplicationTests {

    private static String serverURL = "http://localhost:8080/";


    @Resource(name = "supportedURLPool")
    RegisterService<RegisterInfo> patternRegister;

    Util util;
    RegisterInfo info = new RegisterInfo("fshare.vn", Util.fsharePattern, "fshare");

    @Before
    public void prepare() {
        util = new Util();
        info.setRegistration(true);
        patternRegister.register(info);
    }

    @After
    public void preExit() {
        info.setRegistration(false);
        patternRegister.unregister(info);
    }


    @Test
    public void testSupportedURL() throws IOException {

        JSONObject jsonObject = new JSONObject(new RequestURL("test", "test"));

        CloseableHttpResponse response = util.postJsonRequest(jsonObject.toString(), Util.serverURLService);

        //evaluate result
        assert response.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value();
    }

    @Test
    public void testFshare() throws IOException {

        CloseableHttpResponse response = util.requestValidFshareURL();

        //evaluate result
        assert response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value();

    }
}
