package com.butchjgo.linkservice;

import com.butchjgo.linkservice.common.domain.RequestURL;
import com.butchjgo.linkservice.service.RegisterService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
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
        JSONObject jsonObject = new JSONObject(new RequestURL("test", "test"));
        StringEntity entity = new StringEntity(jsonObject.toString());
        post.setEntity(entity);
        post.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(post);

        //evaluate result
        assert response.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value();
    }

    @Test
    public void testFshare() throws IOException {
        // init resource
        final String fsharePattern = "https://www.fshare.vn/file/[a-zA-Z0-9]+";
        patternRegister.register(fsharePattern);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(serverURL + "linkservice");
        JSONObject jsonObject = new JSONObject(new RequestURL("https://www.fshare.vn/file/P3YBDNV9AFYCJF6", "no"));

        StringEntity entity = new StringEntity(jsonObject.toString());
        post.setEntity(entity);
        post.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(post);

        // close resource
        response.close();
        httpClient.close();
        patternRegister.unregister("Xin chao" + fsharePattern);

        //evaluate result
        assert response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value();

    }
}
