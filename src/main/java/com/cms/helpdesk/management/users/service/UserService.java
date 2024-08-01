package com.cms.helpdesk.management.users.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.common.exception.ResourceNotFoundException;
import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.reuse.Filter;
import com.cms.helpdesk.common.reuse.PageConvert;
import com.cms.helpdesk.common.utils.EmailUtil;
import com.cms.helpdesk.management.roles.model.Role;
import com.cms.helpdesk.management.roles.repository.RoleRepository;
import com.cms.helpdesk.management.users.dto.request.RegisterDto;
import com.cms.helpdesk.management.users.dto.request.ReqResetPassword;
import com.cms.helpdesk.management.users.dto.request.ReqUserDTO;
import com.cms.helpdesk.management.users.dto.response.OrganizeRes;
import com.cms.helpdesk.management.users.dto.response.UserRes;
import com.cms.helpdesk.management.users.model.Employee;
import com.cms.helpdesk.management.users.model.Registration;
import com.cms.helpdesk.management.users.model.User;
import com.cms.helpdesk.management.users.repository.PaginateUser;
import com.cms.helpdesk.management.users.repository.RegistrationRepository;
import com.cms.helpdesk.management.users.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaginateUser paginate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EmailUtil emailUtil;

    @Value("${link.forgotpassword}")
    private String linkForgotPassword;

    public ResponseEntity<Object> getUsers(int page, int size) {
        Specification<User> spec = Specification
                .where(new Filter<User>().orderByIdDesc());
        Page<User> res = paginate.findAll(spec, PageRequest.of(page, size));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res), buildResUsers(res.getContent()),
                null), 1);
    }

    public ResponseEntity<Object> getUserById(Long id) {
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, buildResUser(getUser(id)), null), 1);
    }

    public ResponseEntity<Object> createUser(ReqUserDTO dto) {

        User user = new User();
        user.setEmployee(employeeService.getEmployeeByNip(dto.getNip()));
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(getRole(dto.getRoleId()));
        user.setApprove(true);

        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, userRepository.save(user), null), 0);
    }

    public ResponseEntity<Object> updateUser(Long id, ReqUserDTO dto) {
        User user = getUser(id);
        User request = new User();
        request.setEmployee(employeeService.getEmployeeByNip(dto.getNip()));
        request.setEmail(dto.getEmail());
        request.setPassword(passwordEncoder.encode(dto.getPassword()));
        request.setRole(getRole(dto.getRoleId()));
        request.setApprove(dto.isApprove());
        Field[] fields = request.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(request);
                if (value != null) {
                    Field userField = user.getClass().getDeclaredField(field.getName());
                    userField.setAccessible(true);
                    userField.set(user, value);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, userRepository.save(user), null), 0);
    }

    public ResponseEntity<Object> deleteUser(Long id) {
        User user = getUser(id);
        user.setDeleted(true);
        userRepository.save(user);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, null, null), 0);
    }

    public ResponseEntity<Object> register(RegisterDto dto) {
        List<String> errors = new ArrayList<>();
        Employee employee = employeeService.getEmployeeByNip(dto.getNip());
        if (employee.isRegistered()) {
            errors.add("Karyawan sudah mendaftar sebelumnya");
            return Response.buildResponse(new GlobalDto(302, null,
                    "Karyawan telah terdaftar", null, null, errors), 1);
        }
        Registration regis = new Registration();
        regis.setNip(dto.getNip());
        regis.setEmail(dto.getEmail());
        regis.setPassword(passwordEncoder.encode(dto.getPassword()));
        regis.setName(dto.getName());
        regis.setPhone(dto.getPhone());
        registrationRepository.save(regis);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_REGISTER.getStatusCode(), null,
                Message.SUCCESSFULLY_REGISTER.getMessage(), null, null, null), 0);
    }

    public ResponseEntity<Object> createLinkForgotPassword(String nipOrEmail) {
        try {
            User user = userRepository.findByEmailOrNip(nipOrEmail, nipOrEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User tidak di temukan"));

            String email = user.getEmail();
            String encodedString = Base64.getEncoder().encodeToString(user.getEmployee().getNip().getBytes());

            String bodyHtml = generateLinkForgotPass(user.getEmployee().getName(), linkForgotPassword + "/" + encodedString);
            emailUtil.sendEmail(email, "LINK RESET PASSWORD", bodyHtml, true, null);

            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_REGISTER.getStatusCode(), null,
                    "Link reset password telah dikirim ke " + email, null, null, null), 0);
        } catch (Exception e) {
            log.error("error", e);
            return Response.buildResponse(new GlobalDto(404, null, "NIP Tidak Terdaftar", null, null, null), 3);
        }
    }

    public ResponseEntity<Object> forgotPassword(ReqResetPassword dto) {
        try {
            userRepository.resetPasswordUser(passwordEncoder.encode(dto.getPassword()),
                    dto.getNip());
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_REGISTER.getStatusCode(), null,
                    Message.SUCCESSFULLY_REGISTER.getMessage(), null, null, null), 0);
        } catch (Exception e) {
            return Response.buildResponse(new GlobalDto(404, null, "NIP Tidak Terdaftar", null, null, null), 3);
        }
    }

    private String generateLinkForgotPass(String name, String link) {
        String bodyHtml = emailUtil.readHtmlTemplate("forgot_password.html");
        return bodyHtml.replace("{USER}", name).replace("{LINK}", link);
    }

    private List<UserRes> buildResUsers(List<User> user) {
        List<UserRes> res = new ArrayList<>();
        for (User u : user) {
            res.add(buildResUser(u));
        }
        return res;
    }

    private UserRes buildResUser(User user) {
        UserRes res = new UserRes();
        res.setId(user.getId());
        res.setNip(user.getEmployee().getNip());
        res.setName(user.getEmployee().getName());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());

        OrganizeRes organizeRes = new OrganizeRes();
        organizeRes.setDepartment(user.getEmployee().getDepartment());
        organizeRes.setRegion(user.getEmployee().getRegion());
        organizeRes.setBranch(user.getEmployee().getBranch());
        res.setOrganize(organizeRes);

        return res;
    }

    private User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getUserByNip(String nip) {
        return userRepository.findByNip(nip).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Role getRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

}
