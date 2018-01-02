package com.butchjgo.linkservice.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;

@Service(value = "uniqueService")
public class UniqueServiceImpl implements UniqueService {

    final String baseString = LocalTime.now().toString();
    AtomicLong atomicLong = new AtomicLong();

    @Override
    synchronized public String get() {
        String cur = String.valueOf(atomicLong.incrementAndGet()).concat(baseString);
        return DigestUtils.md2Hex(cur);
    }
}
