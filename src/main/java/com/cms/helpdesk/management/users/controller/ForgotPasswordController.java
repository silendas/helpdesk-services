package com.cms.helpdesk.management.users.controller;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.users.dto.request.ReqResetPassword;
import com.cms.helpdesk.management.users.service.ForgotPasswordService;
import com.cms.helpdesk.management.users.service.UserService;

@Controller
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService service;

    @Autowired
    private UserService userService;

    @RequestMapping(value = BasePath.BASE_USERS + "/forgotpwd", method = RequestMethod.GET)
    public ResponseEntity<Object> forgotPassword(@RequestParam("nipOrEmail") String nipOrEmail) {
        return service.createLinkForgotPassword(nipOrEmail);
    }

    @RequestMapping(BasePath.BASE_FORGOT_PASSWORD + "/{nipEncode}")
    public ModelAndView doViewForgorPwd(ModelAndView modelAndView, @PathVariable("nipEncode") String nipEncode) {
        modelAndView.setViewName("forgotPassword");
        byte[] decodedBytes = Base64.getDecoder().decode(nipEncode);
        String decodedString = new String(decodedBytes);
        modelAndView.addObject("nipValue", decodedString);
        userService.getUserByNip(decodedString);
        return modelAndView;
    }

    @RequestMapping(value = BasePath.BASE_FORGOT_PASSWORD + "/forgotpasssubmit", method = RequestMethod.PUT)
    public ResponseEntity<Object> doSubmitForgotPassword(@RequestBody ReqResetPassword request) {
        return service.forgotPassword(request);
    }

}
