package com.project.schoolmanagment.security.service;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails{

    private Long id;
    private String username;
    private String name;
    private Boolean isAdvisor;
    @JsonIgnore //we don'r want it public
    private String password;
    // to make it known and usable by security your roles must be extended
    private Collection<? extends GrantedAuthority>authorities; // we are getting an extension of grantedauthoritx

    public UserDetailsImpl(Long id, String username, String name, Boolean isAdvisor, String password, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.isAdvisor = isAdvisor;
        this.password = password;
        List<GrantedAuthority>grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role));
        this.authorities  = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority>getAuthorities(){
        return authorities;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public String getPassword(){
        return password;
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

    @Override
    public boolean equals(Object object){
        if(this==object){
            return true;
        }

        //matching the class type
        if (object == null || getClass() != object.getClass()){
            return false;
        }

        //matching the id
        UserDetailsImpl userDetails = (UserDetailsImpl) object;
        return Objects.equals(id, userDetails.getId());
    }
}
