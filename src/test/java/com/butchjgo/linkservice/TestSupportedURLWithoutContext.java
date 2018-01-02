package com.butchjgo.linkservice;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class TestSupportedURLWithoutContext {

    private static String serverURL = "http://localhost:8080/";

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

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(serverURL + "linkservice");

        StringEntity entity = new StringEntity("{\"url\":\"https://www.fshare.vn/file/P3YBDNV9AFYCJF6\",\"password\":\"test\"}");
        post.setEntity(entity);
        post.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(post);

        assert response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value();

    }
}
