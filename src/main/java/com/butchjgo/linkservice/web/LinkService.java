package com.butchjgo.linkservice.web;

import com.butchjgo.linkservice.common.domain.RequestURL;
import com.butchjgo.linkservice.common.domain.RequestURLResult;
import com.butchjgo.linkservice.service.UniqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "linkservice",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LinkService {

    @Resource(name = "uniqueService")
    UniqueService uniqueService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    RequestURLResult doPost(@Valid @RequestBody RequestURL req, BindingResult result) {
        if (result.hasErrors()) {
            //TODO throw errors for Errors handler
        }
        String id = uniqueService.get();
        // TODO refactoring message instead of hard code
        return new RequestURLResult(id, "Request success");
    }
}
