package com.example.springresttask.controller;

import com.example.springresttask.exeption_handing.NoSuchUserException;
import com.example.springresttask.model.Role;
import com.example.springresttask.model.User;
import com.example.springresttask.service.RoleService;
import com.example.springresttask.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public List<User> indexView() {
        return (List<User>) userService.findAll();
    }

    // получение юзера по айди
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new NoSuchUserException("there is no user with ID = " +
                    id + " in Database");
        }
        return user;
    }

    // работает create
    @PostMapping("/addUser")
    public ResponseEntity<HttpStatus> addUserView(@RequestBody @Valid User user, BindingResult bindingResult) {
        userService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // работает edit
    @PutMapping("/addOrUpdate/{id}")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid User user, @PathVariable("id") long id,
                                          BindingResult bindingResult) {
        user.setId(id);
        userService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // работает
    @DeleteMapping("/removeUser/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
//        return "User with ID = " + id + " was deleted";
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return (List<Role>) roleService.getAll();
    }

}
