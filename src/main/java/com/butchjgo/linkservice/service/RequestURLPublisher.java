package com.butchjgo.linkservice.service;

import com.butchjgo.linkservice.common.domain.RequestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Map;
import java.util.function.UnaryOperator;

@Service(value = "requestURLPublisher")
@ConfigurationProperties(prefix = "linkservice")
public class RequestURLPublisher implements RequestPublisher<RequestData> {
    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    JmsTemplate jmsTemplate;

    @Resource(name = "patternPool")
    Map<String, String> patternPool;

    String jmsRequestDestination;

    private UnaryOperator<String> getMathChanel = (String url) -> patternPool.keySet().stream().
            filter(p -> url.matches(p)).
            map(p -> patternPool.get(p)).
            findFirst().orElse(null);

    @Override
    public void publish(RequestData requestURL) throws JsonProcessingException {
        String json = objectWriter.writeValueAsString(requestURL);
        //String chanel = getMathChanel(requestURL.getUrl());
        String chanel = getMathChanel.apply(requestURL.getUrl());

        if (chanel == null) return;

        jmsTemplate.send(chanel, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(json);
            }
        });
    }

    public String getJmsRequestDestination() {
        return jmsRequestDestination;
    }

    public void setJmsRequestDestination(String jmsRequestDestination) {
        this.jmsRequestDestination = jmsRequestDestination;
    }
}
