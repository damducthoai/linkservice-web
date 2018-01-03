package com.butchjgo.linkservice;

import com.butchjgo.linkservice.common.domain.RegisterInfo;
import com.butchjgo.linkservice.common.domain.RequestURL;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestRegisterService {

    private static String serverURL = "http://localhost:8080/";

    @Value("${linkservice.jms-register-destination}")
    String registerChanel;

    @Autowired
    JmsTemplate jmsTemplate;

    @Test
    public void registration() {
        RegisterInfo info = new RegisterInfo("fshare.vn", "https://www.fshare.vn/file/[a-zA-Z0-9]+", "fshare");
        jmsTemplate.send("register", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(new JSONObject(info).toString());
            }
        });
    }

    @Test
    public void verifyValid() throws IOException {
        registration();
        CloseableHttpResponse response = request();
        assert response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value();
        unregister();
    }

    @Test
    public void verifyInvalid() throws IOException {
        unregister();
        CloseableHttpResponse response = request();
        assert response.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value();
    }

    @Test
    public void unregister() {
        RegisterInfo info = new RegisterInfo("fshare.vn", "https://www.fshare.vn/file/[a-zA-Z0-9]+", "fshare", false);
        jmsTemplate.send("register", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(new JSONObject(info).toString());
            }
        });
    }

    private CloseableHttpResponse request() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(serverURL + "linkservice");
        JSONObject jsonObject = new JSONObject(new RequestURL("https://www.fshare.vn/file/P3YBDNV9AFYCJF677", "no"));

        StringEntity entity = new StringEntity(jsonObject.toString());
        post.setEntity(entity);
        post.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(post);

        // close resource
        response.close();
        httpClient.close();
        return response;
    }
}
