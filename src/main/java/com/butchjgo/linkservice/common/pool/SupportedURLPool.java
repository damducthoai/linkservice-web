package com.butchjgo.linkservice.common.pool;

import com.butchjgo.linkservice.common.domain.RegisterInfo;
import com.butchjgo.linkservice.common.domain.RequestData;
import com.butchjgo.linkservice.common.entity.SupportedPattern;
import com.butchjgo.linkservice.common.repository.SupportedPatternRepository;
import com.butchjgo.linkservice.service.RegisterService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
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

    public boolean isSupported(final RequestData req) {
        boolean isSuport = false;
        SupportedPattern supportedPattern = registedInfo.stream()
                .filter(info -> info.getCompiledPattern().matcher(req.getUrl()).matches())
                .findFirst()
                .orElse(null);
        if (supportedPattern != null) {
            isSuport = true;
            req.setChanel(supportedPattern.getChanel());
        }
        return isSuport;
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
