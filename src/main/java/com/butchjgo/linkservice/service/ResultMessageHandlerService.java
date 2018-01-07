package com.butchjgo.linkservice.service;

import com.butchjgo.linkservice.common.domain.ResultData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Service
public class ResultMessageHandlerService {

    @Autowired
    ObjectMapper objectMapper;

    @Resource(name = "resultPool")
    Map<String, String> resultPool;

    @Resource(name = "emitterPool")
    Map<String, SseEmitter> emitterPool;

    public ResultMessageHandlerService() {
    }

    @JmsListener(destination = "result",containerFactory = "resultJmsListenerContainerFactory")
    void onMessage(ResultData rs) throws IOException {
        System.out.println(String.format("received: %s",rs.getResult()));
        String id = rs.getClientid();

        resultPool.put(id, rs.getResult());

        if (emitterPool.containsKey(id)) {
            SseEmitter emitter = emitterPool.get(id);
            try {
                emitter.send(rs, MediaType.APPLICATION_JSON_UTF8);
            } catch (Exception e) {
                // todo logging implement here
                e.printStackTrace();
            }
        }
    }
}
