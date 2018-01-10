package com.butchjgo.linkservice.common.pool;

import com.butchjgo.linkservice.common.entity.BadURL;
import com.butchjgo.linkservice.common.repository.BadURLRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "badURLPool")
public class BadURLPool implements Pool<String> {

    @Resource(name = "badURLRepository")
    BadURLRepository badURLRepository;

    @Override
    public boolean contain(String s) {
        return badURLRepository.findById(s).isPresent();
    }

    @Override
    public boolean isSupported(String s) {
        return false;
    }
}
