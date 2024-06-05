package com.servicecops.mentors.models.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "system_user", schema = "public", catalog = "mentors")
public class SystemUserModel implements UserDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "first_name")
    private String firstName;
    @Basic
    @Column(name = "last_name")
    private String lastName;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "role_code")
    private String roleCode;
    @Basic
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
    @Basic
    @Column(name = "last_logged_in_at")
    private Timestamp lastLoggedInAt;
    @Basic
    @Column(name = "is_active")
    private Boolean isActive;

    @Transient
    private Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        //  we shall be setting these up when we login but for
        //  now let's just maintain an empty array
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.getIsActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getIsActive();
    }

    @Override
    public boolean isEnabled() {
        return getIsActive();
    }
}
