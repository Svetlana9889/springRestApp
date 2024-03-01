package com.example.springresttask.service;

import com.example.springresttask.model.Role;
import com.example.springresttask.repositories.RoleRepository;
import org.springframework.stereotype.Service;


import java.util.Collection;

@Service
public class  RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

//    @Transactional(readOnly = true)
    public Collection<Role> getAll() {
        return (Collection<Role>) roleRepository.findAll();
    }
}