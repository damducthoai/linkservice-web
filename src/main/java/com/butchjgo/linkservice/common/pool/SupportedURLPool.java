package com.butchjgo.linkservice.common.pool;

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
public class SupportedURLPool implements Pool<String>, RegisterService<String> {

    final Set<String> patternPool = new HashSet<>();
    @Resource(name = "supportedPatternRepository")
    SupportedPatternRepository supportedPatternRepository;

    @Override
    public boolean contain(String s) {
        return patternPool.contains(s);
    }

    @Override
    public boolean isSupported(final String url) {
        return patternPool.stream().anyMatch(p -> url.matches(p));
    }

    @Override
    @Transactional
    public void register(String s) {
        supportedPatternRepository.save(new SupportedPattern(s));
        patternPool.add(s);
    }

    @Override
    @Transactional
    public void unregister(String s) {
        supportedPatternRepository.deleteById(s);
        patternPool.remove(s);
    }

    @PostConstruct
    @Transactional
    void loadData() {
        supportedPatternRepository.findAll().stream().peek(p -> patternPool.add(p.getPattern())).close();
    }
}
