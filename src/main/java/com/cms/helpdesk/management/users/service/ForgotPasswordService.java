package com.cms.helpdesk.management.users.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.common.exception.ResourceNotFoundException;
import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.utils.EmailUtil;
import com.cms.helpdesk.management.users.dto.request.ReqResetPassword;
import com.cms.helpdesk.management.users.model.User;
import com.cms.helpdesk.management.users.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${link.forgotpassword}")
    private String linkForgotPassword;

    public ResponseEntity<Object> createLinkForgotPassword(String nipOrEmail) {
        try {
            User user = userRepository.findByEmailOrNip(nipOrEmail, nipOrEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User tidak di temukan"));
            String email = user.getEmail();
            String encodedString = Base64.getEncoder().encodeToString(user.getEmployee().getNip().getBytes());
            String bodyHtml = generateLinkForgotPass(user.getEmployee().getName(),
                    linkForgotPassword + "/" + encodedString);
            emailUtil.sendEmail(email, "LINK RESET PASSWORD", bodyHtml, true, null);
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_REGISTER.getStatusCode(), null,
                    "Link reset password telah dikirim ke " + email, null, null, null), 0);
        } catch (Exception e) {
            log.error("error", e);
            return Response.buildResponse(new GlobalDto(404, null, "NIP Tidak Terdaftar", null, null, null), 3);
        }
    }

    public ResponseEntity<Object> forgotPassword(ReqResetPassword dto) {
        userRepository.resetPasswordUser(passwordEncoder.encode(dto.getPassword()),
                dto.getNip());
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_REGISTER.getStatusCode(), null,
                Message.SUCCESSFULLY_REGISTER.getMessage(), null, null, null), 0);
    }

    private String generateLinkForgotPass(String name, String link) {
        String bodyHtml = emailUtil.readHtmlTemplate("forgot_password.html");
        return bodyHtml.replace("{USER}", name).replace("{LINK}", link);
    }
    
}
