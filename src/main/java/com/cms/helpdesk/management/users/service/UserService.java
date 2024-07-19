package com.cms.helpdesk.management.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.common.exception.ResourceNotFoundException;
import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.reuse.Filter;
import com.cms.helpdesk.common.reuse.PageConvert;
import com.cms.helpdesk.management.branch.dto.request.BranchDTO;
import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.branch.repository.BranchRepository;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.departments.repository.DepartmentRepository;
import com.cms.helpdesk.management.regions.model.Region;
import com.cms.helpdesk.management.regions.repository.RegionRepository;
import com.cms.helpdesk.management.roles.model.Role;
import com.cms.helpdesk.management.roles.repository.RoleRepository;
import com.cms.helpdesk.management.users.dto.ReqUserDTO;
import com.cms.helpdesk.management.users.model.User;
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
    private DepartmentRepository departmentRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private BranchRepository branchRepository;

    public ResponseEntity<Object> getUsers(int page, int size) {
        Specification<User> spec = Specification
                .where(new Filter<User>().orderByIdDesc());
        Page<User> res = paginate.findAll(spec, PageRequest.of(page, size));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res), res.getContent(), null), 1);
    }

    public ResponseEntity<Object> getUserById(Long id) {
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, userRepository.findById(id), null), 1);
    }

    public ResponseEntity<Object> createUser(ReqUserDTO dto) {
        if ((dto.getDepartmentId() == null || dto.getDepartmentId() == 0) &&
                (dto.getRegionId() == null || dto.getRegionId() == 0) &&
                (dto.getBranchId() == null || dto.getBranchId() == 0)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GlobalDto(HttpStatus.BAD_REQUEST.value(), null,
                            "At least one of Branch, Department, or Region must be provided", null, null, null));
        }

        User user = new User();
        user.setNip(dto.getNip());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoleId(getRole(dto.getRoleId()));

        if (dto.getDepartmentId() != null) {
            user.setDepartmentId(getDepartment(dto.getDepartmentId()));
        }

        if (dto.getRegionId() != null) {
            user.setRegionId(getRegion(dto.getRegionId()));
        }

        if (dto.getBranchId() != null) {
            user.setBranchId(getBranch(dto.getBranchId()));
        }

        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, userRepository.save(user), null), 0);
    }

    public ResponseEntity<Object> updateUser(Long id, ReqUserDTO dto) {
        if ((dto.getDepartmentId() == null || dto.getDepartmentId() == 0) &&
                (dto.getRegionId() == null || dto.getRegionId() == 0) &&
                (dto.getBranchId() == null || dto.getBranchId() == 0)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GlobalDto(HttpStatus.BAD_REQUEST.value(), null,
                            "At least one of Branch, Department, or Region must be provided", null, null, null));
        }

        User user = getUser(id);

        if (dto.getNip() != null && !dto.getNip().isEmpty()) {
            user.setNip(dto.getNip());
        }

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getRoleId() != null && dto.getRoleId() != 0) {
            Role role = getRole(dto.getRoleId());
            if (role == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new GlobalDto(HttpStatus.BAD_REQUEST.value(), null, "Invalid Role ID", null, null,
                                null));
            }
            user.setRoleId(role);
        }

        if (dto.getDepartmentId() != null && dto.getDepartmentId() != 0) {
            Department department = getDepartment(dto.getDepartmentId());
            if (department == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new GlobalDto(HttpStatus.BAD_REQUEST.value(), null, "Invalid Department ID", null, null,
                                null));
            }
            user.setDepartmentId(department);
        }

        if (dto.getRegionId() != null && dto.getRegionId() != 0) {
            Region region = getRegion(dto.getRegionId());
            if (region == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new GlobalDto(HttpStatus.BAD_REQUEST.value(), null, "Invalid Region ID", null, null,
                                null));
            }
            user.setRegionId(region);
        }

        if (dto.getBranchId() != null && dto.getBranchId() != 0) {
            Branch branch = getBranch(dto.getBranchId());
            if (branch == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new GlobalDto(HttpStatus.BAD_REQUEST.value(), null, "Invalid branch ID", null, null,
                                null));
            }
            user.setBranchId(branch);
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

    private User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Role getRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    private Department getDepartment(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    private Region getRegion(Long id) {
        return regionRepository.findById(id).orElse(null);
    }

    private Branch getBranch(Long id) {
        return branchRepository.findById(id).orElse(null);
    }

}
