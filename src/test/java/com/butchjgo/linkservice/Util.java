package com.butchjgo.linkservice;

import com.butchjgo.linkservice.common.domain.RequestData;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.IOException;

public class Util {
    public static final String serverURL = "http://localhost:8080/";
    public static final String serverURLService = "http://localhost:8080/linkservice";
    public static final String fsharePattern = "https://www.fshare.vn/file/[a-zA-Z0-9]+";

    public CloseableHttpResponse requestValidFshareURL() throws IOException {
        final String URL = serverURL.concat("linkservice");
        JSONObject jsonObject = new JSONObject(new RequestData("https://www.fshare.vn/file/P3YBDNV9AFYCJF6", "no"));
        return postJsonRequest(jsonObject.toString(), URL);
    }

    public CloseableHttpResponse postJsonRequest(final String data, final String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        StringEntity entity = new StringEntity(data);
        post.setEntity(entity);
        post.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(post);

        // close resource
        response.close();
        httpClient.close();
        return response;
    }
}
