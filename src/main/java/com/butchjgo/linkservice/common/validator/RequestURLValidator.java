package com.butchjgo.linkservice.common.validator;

import com.butchjgo.linkservice.common.domain.RequestURL;
import com.butchjgo.linkservice.common.pool.Pool;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

@Service(value = "requestURLValidator")
public class RequestURLValidator implements Validator {

    @Resource(name = "badURLPool")
    Pool<String> badURLPool;

    @Resource(name = "supportedURLPool")
    Pool<String> supportedURLPool;

    @Override
    public boolean supports(Class<?> clazz) {
        return RequestURL.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String url = RequestURL.class.cast(target).getUrl();
        if (badURLPool.contain(url)) {
            // TODO refactoring message and code
            errors.reject(HttpStatus.BAD_REQUEST.toString(), "bad request url");
        }
        if (!supportedURLPool.isSupported(url)) {
            errors.reject(HttpStatus.UNPROCESSABLE_ENTITY.toString(), "url does not supported");
        }
    }
}
