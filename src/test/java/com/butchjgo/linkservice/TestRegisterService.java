package com.butchjgo.linkservice;

import com.butchjgo.linkservice.common.domain.RegisterInfo;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
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

    Util util;

    @Before
    public void registration() {

        util = new Util();

        RegisterInfo info = new RegisterInfo("fshare.vn", "https://www.fshare.vn/file/[a-zA-Z0-9]+", "fshare", true);
        jmsTemplate.send("register", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(new JSONObject(info).toString());
            }
        });
    }

    @Test
    public void verifyValid() throws IOException {
        CloseableHttpResponse response = util.requestValidFshareURL();
        assert response.getStatusLine().getStatusCode() == HttpStatus.CREATED.value();
        unregister();
    }

    @Test
    public void verifyInvalid() throws IOException {
        unregister();
        CloseableHttpResponse response = util.requestValidFshareURL();
        int code = response.getStatusLine().getStatusCode();
        assert response.getStatusLine().getStatusCode() == HttpStatus.BAD_REQUEST.value();
        registration();
    }

    @After
    public void unregister() {
        RegisterInfo info = new RegisterInfo("fshare.vn", "https://www.fshare.vn/file/[a-zA-Z0-9]+", "fshare", false);
        jmsTemplate.send("register", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(new JSONObject(info).toString());
            }
        });
    }

}
