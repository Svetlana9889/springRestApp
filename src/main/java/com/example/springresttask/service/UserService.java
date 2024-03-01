package com.example.springresttask.service;

import com.example.springresttask.dto.UserDto;
import com.example.springresttask.model.User;
import com.example.springresttask.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;


    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;


        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(UserDto userDto) {
        User user = convertToUser(userDto);
        userRepository.save(user);
    }
//
@Transactional(readOnly = true)
public UserDto findByUsername(String username) {
    return convertToUserDto(userRepository.findByUsername(username));
}
//    @Transactional(readOnly = true)
//    public UserDto findByUsername(String username) {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new EntityNotFoundException("User not found");
//        }
//        return convertToUserDto(user);
//    }

    @Transactional(readOnly = true)
    public Collection<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public void update(String username, UserDto userDto) {
        userDto.setUsername(username);
        userRepository.save(convertToUser(userDto));
        }

    @Transactional
    public void delete(String username) {
        userRepository.delete(userRepository.findByUsername(username));
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

    private User convertToUser(UserDto userDto) {
        User user = new User();

        // Получение максимального Id из таблицы
        Long maxId = userRepository.findMaxId(); // Предположим, что у вас есть метод findMaxId в репозитории

        // Генерация нового Id на 1 больше максимального Id
        Long newId = maxId != null ? maxId + 1 : 1;

        user.setId(newId);
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRoles(userDto.getRoles());

        return user;
    }

    private UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
