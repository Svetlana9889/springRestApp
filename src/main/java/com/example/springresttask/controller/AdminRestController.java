package com.example.springresttask.controller;

import com.example.springresttask.dto.UserDto;
import com.example.springresttask.exeption_handing.NoSuchUserException;
import com.example.springresttask.model.Role;
import com.example.springresttask.model.User;
import com.example.springresttask.service.RoleService;
import com.example.springresttask.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private UserService userService;

    private RoleService roleService;

    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public List<UserDto> indexView() {
        return (List<UserDto>) userService.findAll();
    }

    // получение юзера по айди
    @GetMapping("/{username}")
    public UserDto getUserBySurname(@PathVariable("username") String username) {
        UserDto userDto = userService.findByUsername(username);
        if (userDto == null) {
            throw new NoSuchUserException("there is no user with username = " +
                    username + " in Database");
        }
        return userDto;
    }

    // работает create
    @PostMapping("/addUser")
    public ResponseEntity<HttpStatus> addUserView(@RequestBody @Valid UserDto userDto) {
        userService.save(userDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // работает edit
    @PutMapping("/addOrUpdate/{username}")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid UserDto userDto, @PathVariable("username") String username) {
        userDto.setUsername(username);
        userService.save(userDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // работает
    @DeleteMapping("/removeUser/{username}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("username") String username) {
        userService.delete(username);
//        return "User with ID = " + id + " was deleted";
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return (List<Role>) roleService.getAll();
    }

//    private User convertToUser(UserDto userDto) {
//        return modelMapper.map(userDto, User.class);
//    }
}
