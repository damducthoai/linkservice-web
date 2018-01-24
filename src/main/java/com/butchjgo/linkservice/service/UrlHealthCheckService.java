package com.butchjgo.linkservice.service;

import com.butchjgo.linkservice.common.entity.BadURL;
import com.butchjgo.linkservice.common.repository.BadURLRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class UrlHealthCheckService {
    @Resource(name = "badURLRepository")
    BadURLRepository badURLRepository;

    RestTemplate restTemplate = new RestTemplate();

    @JmsListener(destination = "${linkservice.jms-healthc-destination}", containerFactory = "healthCheckJmsListenerContainerFactory")
    void onMessage(String url) {
        check(url);
    }

    void check(String url) {
        try {
            ResponseEntity re = restTemplate.getForEntity(url, ResponseEntity.class);
            if (re.getStatusCode() != HttpStatus.OK) {
                badURLRepository.save(new BadURL(url));
            }
        } catch (Exception e) {
            badURLRepository.save(new BadURL(url));
        }

    }
}
