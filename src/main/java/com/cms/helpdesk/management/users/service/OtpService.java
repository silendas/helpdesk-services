package com.cms.helpdesk.management.users.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.apache.commons.io.IOUtils;

import com.cms.helpdesk.common.exception.OtpValidationException;
import com.cms.helpdesk.common.exception.ResourceNotFoundException;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.utils.EmailUtil;
import com.cms.helpdesk.common.utils.OtpUtil;
import com.cms.helpdesk.management.users.dto.request.SendOtpDto;
import com.cms.helpdesk.management.users.dto.request.VerifyOtpDTO;
import com.cms.helpdesk.management.users.model.Employee;
import com.cms.helpdesk.management.users.model.Registration;
import com.cms.helpdesk.management.users.model.User;
import com.cms.helpdesk.management.users.repository.EmployeeRepository;
import com.cms.helpdesk.management.users.repository.RegistrationRepository;
import com.cms.helpdesk.management.users.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OtpService {

    @Autowired
    private RegistrationRepository repo;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public ResponseEntity<Object> sendOtp(SendOtpDto dto) {
        String otp;
        do {
            otp = OtpUtil.generateDigitOTP();
        } while (repo.countByOtp(otp) > 0);

        Registration registration = repo.findByEmailLimitDesc(dto.getEmail());
        registration.setOtp(otp);
        repo.save(registration);

        String email = registration.getEmail();
        String bodyHtml = generateOtpEmailBody(registration.getName(), otp);
        emailUtil.sendEmail(email, "OTP", bodyHtml, true, null);
        return Response.buildResponse(new GlobalDto(200, null, "OTP telah dikirim ke " + email, null, null, null), 0);
    }

    public ResponseEntity<Object> verifyOtp(VerifyOtpDTO dto) {
        Optional<Registration> registration = repo.findByNipLimit1Desc(dto.getNip());
        validateEnteredOtp(dto.getOtp(), registration.get());
        validateOtpExpiration(registration.get().getUpdateAt());
        Registration createRes = repo.save(registration.get());
        createUser(createRes);
        updateEmployee(createRes);
        return Response.buildResponse(new GlobalDto(200, null, "Berhasil diverifikasi", null, null, null), 0);
    }

    public void createUser(Registration registration) {
        User user = new User();
        user.setEmployee(getEmployeeByNip(registration.getNip()));
        user.setEmail(registration.getEmail());
        user.setPassword(registration.getPassword());
        user.setApprove(false);
        user.setOtp(registration.getOtp());
        user.setRegistration(registration);
        userRepository.save(user);
    }

    public void updateEmployee(Registration registration) {
        Employee employee = getEmployeeByNip(registration.getNip());
        employee.setRegistered(true);
        employee.setName(registration.getName());
        employee.setPhone(registration.getPhone());
        employeeRepository.save(employee);
    }

    private String generateOtpEmailBody(String name, String otp) {
        String bodyHtml = readHtmlTemplate("otp_regis.html");
        return bodyHtml.replace("{USER}", name).replace("{OTP_CODE}", otp);
    }

    private String readHtmlTemplate(String templateFileName) {
        Resource resource = resourceLoader.getResource("classpath:templates/" + templateFileName);
        try (InputStream inputStream = resource.getInputStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("error", e);
            throw new RuntimeException(e);
        }
    }

    private void validateEnteredOtp(String enteredOtp, Registration registration) {
        if (!enteredOtp.equalsIgnoreCase(registration.getOtp())) {
            throw new OtpValidationException("Otp yang dimasukkan salah");
        }
    }

    private void validateOtpExpiration(Timestamp updatedAt) {
        if (OtpUtil.isMoreThanFiveMinutesOTP(updatedAt)) {
            throw new OtpValidationException("Otp sudah lebih dari 5 menit");
        }
    }

    public Employee getEmployeeByNip(String nip) {
        return employeeRepository.findByNip(nip)
                .orElseThrow(() -> new ResourceNotFoundException("Karyawan tidak ditemukan"));
    }

}
