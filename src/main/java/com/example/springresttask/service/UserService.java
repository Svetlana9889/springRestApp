package com.example.springresttask.service;

import com.example.springresttask.model.User;
import com.example.springresttask.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Transactional
    public void save(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    @Transactional(readOnly = true)
    public Collection<User> findAll() {
        return userRepository.findAll();
    }
    @Transactional
    public void update(Long id, User user) {
        user.setId(id);
        userRepository.save(user);
        }

        @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("No Username");
        }
        return user;
    }
}
