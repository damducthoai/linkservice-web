package com.butchjgo.linkservice;

import com.butchjgo.linkservice.common.domain.AccountInfo;
import com.butchjgo.linkservice.common.domain.RegisterInfo;
import com.butchjgo.linkservice.common.domain.ResultData;
import com.butchjgo.linkservice.service.UniqueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@EnableJms
@EnableWebMvc
public class LinkserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkserviceApplication.class, args);
	}

    @Bean
    public Map<String, String> patternPool() {
        return new HashMap<>();
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        return jmsTemplate;
    }
    @Bean
    public DefaultJmsListenerContainerFactory registerJmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                                  DefaultJmsListenerContainerFactoryConfigurer configurer,
                                                                                  ObjectMapper objectMapper,
                                                                                  @Qualifier(value = "registerMessageConverter") MessageConverter messageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory healthCheckJmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                                  DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory resultJmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                                DefaultJmsListenerContainerFactoryConfigurer configurer,
                                                                                @Qualifier(value = "resultMessageConverter") MessageConverter messageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }


    @Bean
    public MessageConverter registerMessageConverter(ObjectMapper objectMapper) {
        return new MessageConverter() {
            @Override
            public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
                String info = null;
                try {
                    info = objectMapper.writeValueAsString(object);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    return session.createTextMessage(info);
                }
            }

            @Override
            public Object fromMessage(Message message) throws JMSException, MessageConversionException {
                ActiveMQTextMessage textMessage = ActiveMQTextMessage.class.cast(message);
                RegisterInfo info = null;
                try {
                    info = objectMapper.readValue(textMessage.getText(), RegisterInfo.class);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    return info;
                }
            }
        };
    }

    @Bean
    public MessageConverter resultMessageConverter(ObjectMapper objectMapper) {
        return new MessageConverter() {
            @Override
            public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
                String info = null;
                try {
                    info = objectMapper.writeValueAsString(object);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    return session.createTextMessage(info);
                }
            }

            @Override
            public Object fromMessage(Message message) throws JMSException, MessageConversionException {
                ActiveMQTextMessage textMessage = ActiveMQTextMessage.class.cast(message);
                ResultData info = null;
                try {

                    info = objectMapper.readValue(textMessage.getText(), ResultData.class);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    return info;
                }
            }
        };
    }

    @Bean
    public Map<String, String> resultPool() {
        return new HashMap<>();
    }

    @Bean
    @Scope(value = "session")
    public String clientId(@Qualifier(value = "uniqueService") UniqueService uniqueService) {
	    return uniqueService.get();
    }

    @Bean
    public Map<String, SseEmitter> emitterPool() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(@Value("${allow.origins}") String origins) {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(origins.split(","))
                        .allowCredentials(true);
            }
        };
    }

    @Bean("accountPool")
    public Map<String, LinkedList<AccountInfo>> accountInfoMap() {

        Map<String, LinkedList<AccountInfo>> map = new HashMap<>();
        return map;
    }
}
