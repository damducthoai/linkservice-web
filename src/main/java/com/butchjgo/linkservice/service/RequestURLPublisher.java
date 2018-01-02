package com.butchjgo.linkservice.service;

import com.butchjgo.linkservice.common.domain.RequestURL;
import org.springframework.stereotype.Service;

@Service(value = "requestURLPublisher")
public class RequestURLPublisher implements RequestPublisher<RequestURL> {
    @Override
    public void publish(RequestURL requestURL) {
        // TODO implement business here
    }
}
