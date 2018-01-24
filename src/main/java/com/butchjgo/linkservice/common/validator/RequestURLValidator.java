package com.butchjgo.linkservice.common.validator;

import com.butchjgo.linkservice.common.domain.RequestData;
import com.butchjgo.linkservice.common.pool.SupportedURLPool;
import com.butchjgo.linkservice.common.repository.BadURLRepository;
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
        String url = RequestData.class.cast(target).getUrl();
        if (badURLRepository.findById(url).isPresent()) {
            // TODO refactoring message and code
            errors.reject(HttpStatus.BAD_REQUEST.toString(), "bad request url");
        }
        if (!supportedURLPool.isSupported(url)) {
            errors.reject(HttpStatus.UNPROCESSABLE_ENTITY.toString(), "url does not supported");
        }
    }
}
