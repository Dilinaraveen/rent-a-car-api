package com.dilinaraveen.rent_a_car.entities;

import com.dilinaraveen.rent_a_car.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password; // This field is mapped to the password column in your database
    private UserRole userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }
    @Override
    public String getUsername() {
        return email; // Return the email field as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Customize this logic as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Customize this logic as needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Customize this logic as needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Customize this logic as needed
    }
}
