package com.butchjgo.linkservice.web.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@Service(value = "uniqueService")
public class UniqueServiceImpl implements UniqueService {

    final String baseString = LocalTime.now().toString();
    final AtomicLong atomicLong = new AtomicLong();
    private Supplier<String> stringSupplier = () -> DigestUtils.md2Hex(String.valueOf(atomicLong.incrementAndGet()).concat(baseString));

    @Override
    public String get() {
        return stringSupplier.get();
    }
}
