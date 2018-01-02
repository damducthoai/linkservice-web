package com.butchjgo.linkservice.web;

import com.butchjgo.linkservice.common.domain.RequestURL;
import com.butchjgo.linkservice.common.domain.RequestURLResult;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "linkservice",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LinkService {
    @PostMapping
    RequestURLResult doPost(@Valid @RequestBody RequestURL req, BindingResult result) {
        if (result.hasErrors()) {
            //TODO throw errors for Errors handler
        }
        // TODO get id with id generator service
        String id = "";
        // TODO refactoring message instead of hard code
        return new RequestURLResult(id, "Request success");
    }
}
