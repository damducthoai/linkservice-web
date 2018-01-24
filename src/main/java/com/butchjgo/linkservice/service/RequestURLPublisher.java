package com.butchjgo.linkservice.service;

import com.butchjgo.linkservice.common.domain.RequestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Service(value = "requestURLPublisher")
@ConfigurationProperties(prefix = "linkservice")
public class RequestURLPublisher implements RequestPublisher<RequestData> {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JmsTemplate jmsTemplate;

    String jmsRequestDestination;

    @Override
    public void publish(RequestData requestURL) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(requestURL);
        String chanel = requestURL.getChanel();

        if (chanel == null || chanel.isEmpty()) return;

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
