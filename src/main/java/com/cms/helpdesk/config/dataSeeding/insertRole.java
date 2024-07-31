package com.cms.helpdesk.config.dataSeeding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.management.roles.model.Role;
import com.cms.helpdesk.management.roles.repository.RoleRepository;
import com.cms.helpdesk.management.users.model.Employee;
import com.cms.helpdesk.management.users.model.User;

import jakarta.annotation.PostConstruct;

@Service
public class insertRole {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void insert() {
        if(roleRepository.count() == 0) {
            Role role = new Role();
            role.setName("SUPERADMIN");
            roleRepository.save(role);

            Role role2 = new Role();
            role2.setName("HELPDESK");
            roleRepository.save(role2);

            Role role3 = new Role();
            role3.setName("SUPERVISOR");
            roleRepository.save(role3);

            Role role4 = new Role();
            role4.setName("USER");
            roleRepository.save(role4);
        }
    }

    
}
