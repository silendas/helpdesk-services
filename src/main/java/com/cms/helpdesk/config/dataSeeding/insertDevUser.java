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
public class insertDevUser {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void insert() {
        if (userRepository.count() == 0) {
            Employee employee = new Employee();
            employee.setNip("cmssuperadmin");
            employee.setName("Super Admin");
            employeeRepository.save(employee);

            User user = new User();
            user.setNip("cmssuperadmin");
            user.setEmail("itcmsmaju@gmail.com");
            user.setPassword(passwordEncoder.encode("@cmssuperadmin"));
            user.setRole(getRole(1L));
            user.setApprove(true);
            userRepository.save(user);
        }
    }

    public Role getRole(Long id) {
        Role role = new Role();
        role.setId(id);
        return role;
    }

}
