package com.butchjgo.linkservice.service;

import com.butchjgo.linkservice.common.domain.RequestURL;
import org.springframework.stereotype.Service;

@Service(value = "requestPublisher")
public class RequestPublisherImpl implements RequestPublisher<RequestURL> {
    @Override
    public void publish(RequestURL requestURL) {
        // TODO implement business here
    }
}
