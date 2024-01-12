package com.app.service.impl;

import com.app.entity.Privilege;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.UserRepo;
import com.app.security.MyUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("Login information is incorrect!"));
        return new MyUserDetails(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getEnabled(),
                    true,
                    true,
                    true,
                    getAuthorities(user.getRoles())
                );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}