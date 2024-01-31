package com.app.security;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username)  {
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
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getAlias()));
        }
        return authorities;
    }
}
