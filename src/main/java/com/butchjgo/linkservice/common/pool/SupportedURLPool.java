package com.butchjgo.linkservice.common.pool;

import com.butchjgo.linkservice.common.domain.RegisterInfo;
import com.butchjgo.linkservice.common.entity.SupportedPattern;
import com.butchjgo.linkservice.common.repository.SupportedPatternRepository;
import com.butchjgo.linkservice.service.RegisterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Service(value = "supportedURLPool")
public class SupportedURLPool implements Pool<String>, RegisterService<RegisterInfo> {

    Set<SupportedPattern> registedInfo = new HashSet<>();

    @Resource(name = "supportedPatternRepository")
    SupportedPatternRepository supportedPatternRepository;

    @Override
    public boolean contain(String s) {
        // TODO - do refactoring
        return false;
    }

    @Override
    public boolean isSupported(final String url) {
        return registedInfo.stream().anyMatch(info -> info.getCompiledPattern().matcher(url).matches());
    }

    @Override
    @Transactional
    public void register(RegisterInfo info) {
        if (!supportedPatternRepository.existsById(info.getPattern())) {
            supportedPatternRepository.save(new SupportedPattern(info.getPattern(), info.getChanel()));
        }
        registedInfo.add(new SupportedPattern(info.getPattern(), info.getChanel()));
    }

    @Override
    @Transactional
    public void unregister(RegisterInfo info) {
        if (supportedPatternRepository.existsById(info.getPattern())) {
            supportedPatternRepository.deleteById(info.getPattern());
        }
        registedInfo.removeIf(item -> item.getPattern().equals(info.getPattern()));
    }

    @PostConstruct
    @Transactional
    void loadData() {
        supportedPatternRepository.findAll().stream().forEach(p -> registedInfo.add(new SupportedPattern(p.getPattern(), p.getChanel())));
    }
}
