package com.butchjgo.linkservice.service;

import com.butchjgo.linkservice.common.domain.RegisterInfo;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "registerService")
public class RegisterServiceImpl implements RegisterService<RegisterInfo> {
    @Resource(name = "supportedURLPool")
    RegisterService<String> patternRegister;

    @JmsListener(destination = "${linkservice.jms-register-destination}", containerFactory = "registerJmsListenerContainerFactory")
    void onMessage(RegisterInfo info) {
        System.out.println(info);
        if (info.isRegistration()) {
            register(info);
        } else {
            unregister(info);
        }
    }

    @Override
    public void register(RegisterInfo info) {
        patternRegister.register(info.getPattern());
    }

    @Override
    public void unregister(RegisterInfo info) {
        patternRegister.unregister(info.getPattern());
    }
}
