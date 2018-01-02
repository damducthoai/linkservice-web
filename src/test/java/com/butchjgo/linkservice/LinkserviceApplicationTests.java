package com.butchjgo.linkservice;

import com.butchjgo.linkservice.service.RegisterService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
    RegisterService<String> patternRegister;

    @Test
    public void testSupportedURL() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(serverURL + "linkservice");

        StringEntity entity = new StringEntity("{\"url\":\"test\",\"password\":\"test\"}");
        post.setEntity(entity);
        post.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(post);
        assert response.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value();
    }

    @Test
    public void testFshare() throws IOException {
        final String fsharePattern = "https://www.fshare.vn/file/[a-zA-Z0-9]+";
        patternRegister.register(fsharePattern);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(serverURL + "linkservice");

        StringEntity entity = new StringEntity("{\"url\":\"https://www.fshare.vn/file/P3YBDNV9AFYCJF6\",\"password\":\"test\"}");
        post.setEntity(entity);
        post.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(post);

        patternRegister.unregister(fsharePattern);
        assert response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value();

    }
}
