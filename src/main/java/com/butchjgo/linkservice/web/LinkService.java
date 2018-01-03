package com.butchjgo.linkservice.web;

import com.butchjgo.linkservice.common.domain.RequestData;
import com.butchjgo.linkservice.common.domain.ResultInfo;
import com.butchjgo.linkservice.common.exception.BadRequestException;
import com.butchjgo.linkservice.service.RequestPublisher;
import com.butchjgo.linkservice.service.UniqueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "linkservice",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LinkService {

    @Resource(name = "uniqueService")
    UniqueService uniqueService;

    @Resource(name = "requestURLValidator")
    Validator requestURLValidator;

    @Resource(name = "requestURLPublisher")
    RequestPublisher<RequestData> requestURLPublisher;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    ResultInfo doPost(@Valid @RequestBody RequestData req, BindingResult result) throws BadRequestException, JsonProcessingException {
        if (result.hasErrors()) {
            throw new BadRequestException(result.getAllErrors().toString());
        }
        String id = uniqueService.get();
        req.setId(id);
        requestURLPublisher.publish(req);
        // TODO refactoring message instead of hard code
        return new ResultInfo(id, "Request success");
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(requestURLValidator);
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
