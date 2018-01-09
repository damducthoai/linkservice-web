package com.butchjgo.linkservice.service;

import com.butchjgo.linkservice.common.domain.ResultData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

@Service
public class ResultMessageHandlerService {

    DateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");

    @Autowired
    ObjectMapper objectMapper;

    @Resource(name = "resultPool")
    Map<String, String> resultPool;

    @Resource(name = "emitterPool")
    Map<String, SseEmitter> emitterPool;

    public ResultMessageHandlerService() {
    }

    @PostConstruct
    void custom() {
        sdfDate.setTimeZone(tz);
    }

    @JmsListener(destination = "result",containerFactory = "resultJmsListenerContainerFactory")
    void onMessage(ResultData rs) throws IOException {

        rs.setTime(new Date().toString());

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
