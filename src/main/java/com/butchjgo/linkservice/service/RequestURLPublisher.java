package com.butchjgo.linkservice.service;

import com.butchjgo.linkservice.common.domain.RequestURL;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
public class RequestURLPublisher implements RequestPublisher<RequestURL> {
    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    JmsTemplate jmsTemplate;

    String jmsRequestDestination;

    @Override
    public void publish(RequestURL requestURL) throws JsonProcessingException {
        String json = objectWriter.writeValueAsString(requestURL);
        jmsTemplate.send(jmsRequestDestination, new MessageCreator() {
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
