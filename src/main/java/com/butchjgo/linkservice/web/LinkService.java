package com.butchjgo.linkservice.web;

import com.butchjgo.linkservice.common.domain.RequestData;
import com.butchjgo.linkservice.common.domain.ResultInfo;
import com.butchjgo.linkservice.common.exception.BadRequestException;
import com.butchjgo.linkservice.service.RequestPublisher;
import com.butchjgo.linkservice.service.UniqueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(path = "linkservice")
public class LinkService {

    @Resource(name = "uniqueService")
    UniqueService uniqueService;

    @Resource(name = "requestURLValidator")
    Validator requestURLValidator;

    @Resource(name = "requestURLPublisher")
    RequestPublisher<RequestData> requestURLPublisher;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping
    String doPost(@Valid @RequestBody RequestData req,
                          BindingResult result,
                          HttpSession session,
                          HttpServletResponse response)
            throws BadRequestException, JsonProcessingException {
        if (result.hasErrors()) {
            throw new BadRequestException(result.getAllErrors().toString());
        }
        String clientId = session.getId();

        String id = uniqueService.get();

        req.setId(id);
        req.setClientid(clientId);

        requestURLPublisher.publish(req);
        // TODO refactoring message instead of hard code
        ResultInfo resultInfo = new ResultInfo(id, "Request success");

        resultInfo.setClientid(clientId);

        String finalRes = objectMapper.writeValueAsString(resultInfo);

        return finalRes;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    void doOption(HttpServletResponse response) {
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(requestURLValidator);
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
