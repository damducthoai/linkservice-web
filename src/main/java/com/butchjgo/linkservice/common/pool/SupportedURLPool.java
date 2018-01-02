package com.butchjgo.linkservice.common.pool;

import com.butchjgo.linkservice.service.RegisterService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service(value = "supportedURLPool")
public class SupportedURLPool implements Pool<String>, RegisterService<String> {

    Set<String> patternPool = new HashSet<>();

    @Override
    public boolean contain(String s) {
        return false;
    }

    @Override
    public boolean isSupported(String url) {
        boolean status = false;
        for (String pattern : patternPool) {
            if (url.matches(pattern)) {
                status = true;
                break;
            }
        }
        return status;
    }

    @Override
    public void register(String s) {
        patternPool.add(s);
    }

    @Override
    public void unregister(String s) {
        patternPool.remove(s);
    }
}
