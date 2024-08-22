package com.cms.helpdesk.config.dataSeeding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.management.roles.model.Role;
import com.cms.helpdesk.management.roles.repository.RoleRepository;
import com.cms.helpdesk.management.users.model.Employee;
import com.cms.helpdesk.management.users.model.User;
import com.cms.helpdesk.management.users.repository.EmployeeRepository;
import com.cms.helpdesk.management.users.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class InitialData {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void insertRoles() {
        if (roleRepository.count() == 0) {
            String[] roleNames = { "SUPERADMIN", "HELPDESK", "SUPERVISOR", "USER" };

            for (String roleName : roleNames) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }

    @PostConstruct
    public void insertUser() {
        if (employeeRepository.count() == 0) {
            Employee employee = new Employee();
            employee.setNip("cmssuperadmin");
            employee.setName("Super Admin");
            Employee resEmployee = employeeRepository.save(employee);

            if (userRepository.count() == 0) {
                User user = new User();
                user.setEmployee(resEmployee);
                user.setEmail("itcmsmaju@gmail.com");
                user.setPassword(passwordEncoder.encode("@cmssuperadmin"));
                user.setRole(getRole(1L));
                user.setApprove(true);
                user.setActive(true);
                user.setDeleted(false);
                userRepository.save(user);
            }
        }
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

}
