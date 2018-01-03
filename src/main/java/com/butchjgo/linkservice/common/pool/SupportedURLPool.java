package com.butchjgo.linkservice.common.pool;

import com.butchjgo.linkservice.common.domain.RegisterInfo;
import com.butchjgo.linkservice.common.entity.SupportedPattern;
import com.butchjgo.linkservice.common.repository.SupportedPatternRepository;
import com.butchjgo.linkservice.service.RegisterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Service(value = "supportedURLPool")
public class SupportedURLPool implements Pool<String>, RegisterService<RegisterInfo> {

    @Resource(name = "patternPool")
    Map<String, String> patternPool;

    @Resource(name = "supportedPatternRepository")
    SupportedPatternRepository supportedPatternRepository;

    @Override
    public boolean contain(String s) {
        return patternPool.keySet().contains(s);
    }

    @Override
    public boolean isSupported(final String url) {
        return patternPool.keySet().stream().anyMatch(p -> url.matches(p));
    }

    @Override
    @Transactional
    public void register(RegisterInfo info) {
        if (!supportedPatternRepository.existsById(info.getPattern())) {
            supportedPatternRepository.save(new SupportedPattern(info.getPattern(), info.getChanel()));
        }
        patternPool.put(info.getPattern(), info.getChanel());
    }

    @Override
    @Transactional
    public void unregister(RegisterInfo info) {
        if (supportedPatternRepository.existsById(info.getPattern())) {
            supportedPatternRepository.deleteById(info.getPattern());
        }
        patternPool.remove(info.getPattern());
    }

    @PostConstruct
    @Transactional
    void loadData() {
        supportedPatternRepository.findAll().stream().forEach(p -> patternPool.put(p.getPattern(), p.getChanel()));
    }
}
