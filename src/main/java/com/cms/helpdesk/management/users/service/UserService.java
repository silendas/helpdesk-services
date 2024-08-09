package com.cms.helpdesk.management.users.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.cms.helpdesk.common.reuse.PatchField;
import com.cms.helpdesk.management.roles.model.Role;
import com.cms.helpdesk.management.roles.repository.RoleRepository;
import com.cms.helpdesk.management.users.dto.request.ReqEmployeeDTO;
import com.cms.helpdesk.management.users.dto.request.ReqUserDTO;
import com.cms.helpdesk.management.users.dto.response.OrganizeRes;
import com.cms.helpdesk.management.users.dto.response.UserRes;
import com.cms.helpdesk.management.users.filter.UserFilter;
import com.cms.helpdesk.management.users.model.Employee;
import com.cms.helpdesk.management.users.model.User;
import com.cms.helpdesk.management.users.repository.EmployeeRepository;
import com.cms.helpdesk.management.users.repository.PaginateUser;
import com.cms.helpdesk.management.users.repository.UserRepository;

@Service
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
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    public ResponseEntity<Object> getUsers(String search, String approval, boolean pageable, int page, int size) {
        Specification<User> spec = Specification
                .where(new Filter<User>().orderByIdDesc())
                .and(new UserFilter().approval(approval))
                .and(new UserFilter().query(search));
        if (pageable) {
            Page<User> res = paginate.findAll(spec, PageRequest.of(page, size));
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                    Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res),
                    buildResUsers(res.getContent()),
                    null), 1);
        } else {
            return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                    Message.SUCCESSFULLY_DEFAULT.getMessage(), null, buildResUsers(userRepository.findAll(spec)), null), 1);
        }
    }

    public ResponseEntity<Object> getUserById(Long id) {
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, buildResUser(getUser(id)), null), 1);
    }

    public ResponseEntity<Object> createUser(ReqUserDTO dto) {
        Role role = getRole(dto.getRoleId());

        Employee employee = employeeService.buildReqToEmployee(new ReqEmployeeDTO(dto.getNip(), dto.getName(),
                dto.getPhone(), dto.getDepartmentId(), dto.getRegionId(), dto.getBranchId()), 1L);
        employee.setRegistered(true);

        User user = new User();
        user.setEmployee(employeeRepository.save(employee));
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        user.setApprove(true);
        user.setActive(true);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, userRepository.save(user), null), 0);
    }

    public ResponseEntity<Object> updateUser(String nip, ReqUserDTO dto) {
        User user = getUserByNip(nip);

        Employee employee = user.getEmployee();
        Employee reqEmployee = employeeService.buildReqToEmployee(new ReqEmployeeDTO(dto.getNip(), dto.getName(),
                dto.getPhone(), dto.getDepartmentId(), dto.getRegionId(), dto.getBranchId()), 1L);

        User reqUser = new User();
        reqUser.setEmployee(employeeRepository.save(new PatchField<Employee>().fusion(employee, reqEmployee)));
        reqUser.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            reqUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRoleId() != null && dto.getRoleId() > 0) {
            reqUser.setRole(getRole(dto.getRoleId()));
        }
        reqUser.setApprove(dto.isApproval());
        reqUser.setActive(dto.isActive());
        reqUser.setDeleted(dto.isDelete());
        userRepository.save(new PatchField<User>().fusion(user, reqUser));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, null, null), 0);
    }

    public ResponseEntity<Object> deleteUser(String nip) {
        User user = getUserByNip(nip);
        user.setDeleted(true);
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, userRepository.save(user), null), 0);
    }

    private List<UserRes> buildResUsers(List<User> user) {
        List<UserRes> res = new ArrayList<>();
        for (User u : user) {
            res.add(buildResUser(u));
        }
        return res;
    }

    public UserRes buildResUser(User user) {
        UserRes res = new UserRes();
        res.setNip(user.getEmployee().getNip());
        res.setName(user.getEmployee().getName());
        res.setEmail(user.getEmail());
        res.setIsApprove(user.isApprove());
        res.setIsDelete(user.isDeleted());
        res.setIsActive(user.isActive());
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
