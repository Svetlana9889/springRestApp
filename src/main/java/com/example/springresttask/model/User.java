package com.example.springresttask.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*$", message = "Name should be contains only from letters ")
    private String name;
    @Column(name = "surname")
    @NotEmpty(message = "Surname should not be empty")
    @Size(min = 2, max = 30, message = "Surname should be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]*$", message = "Name should be contains only from letters ")
    private String surname;
    @Column(name = "email")
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "password")
    private String password;
    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "user_roles"
            , joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    public Collection<String> getRoleNames() {
        Collection<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleNames.add(role.getName());
        }
        return roleNames;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles != null
                ? new HashSet<>(roles)
                : Collections.emptySet();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
