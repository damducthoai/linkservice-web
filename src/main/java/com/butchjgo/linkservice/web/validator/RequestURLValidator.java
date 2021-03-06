package com.butchjgo.linkservice.web.validator;

import com.butchjgo.linkservice.common.domain.RequestData;
import com.butchjgo.linkservice.repository.BadURLRepository;
import com.butchjgo.linkservice.web.pool.SupportedURLPool;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Service(value = "requestURLValidator")
public class RequestURLValidator implements Validator {

    @Resource(name = "badURLRepository")
    BadURLRepository badURLRepository;

    @Resource(name = "supportedURLPool")
    SupportedURLPool supportedURLPool;

    @Override
    public boolean supports(Class<?> clazz) {
        return RequestData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestData req = RequestData.class.cast(target);
        String url = req.getUrl();
        if (badURLRepository.findById(url).isPresent()) {
            // TODO refactoring message and code
            errors.reject(HttpStatus.BAD_REQUEST.toString(), "bad request url");
        }
        if (!supportedURLPool.isSupported(req)) {
            errors.reject(HttpStatus.UNPROCESSABLE_ENTITY.toString(), "url does not supported");
        }
    }
}
