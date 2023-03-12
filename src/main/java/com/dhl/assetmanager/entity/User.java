package com.dhl.assetmanager.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String email;
    private String password;
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    private Role role;
    @OneToMany(mappedBy = "currentUser",
            cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    private List<Asset> assets;
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    private List<Order> requestedOrders;
    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL)
    private List<Order> assignedToOrders;
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Order> recivedOrders;
    @OneToMany(mappedBy = "generatedBy", cascade = CascadeType.ALL)
    private List<Report> generatedReposts;
    private Boolean isDeleted;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
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
        return !isDeleted;
    }
}
