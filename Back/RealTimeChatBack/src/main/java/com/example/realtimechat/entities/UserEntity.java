package com.example.realtimechat.entities;

import com.example.realtimechat.entities.enums.Gender;
import com.example.realtimechat.entities.enums.Roles;
import com.example.realtimechat.infrastructure.helpers.HasId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Exclude friends from equals/hashCode to avoid infinite recursion
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserEntity implements UserDetails, HasId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private String surname;
    private LocalDate birthdate;
    private Gender gender;

    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role = Roles.USER;

    // Excluded from equals/hashCode
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @Builder.Default
    private Set<UserEntity> friends = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;  //kjo e tregon se qka ko me return principal.getName()
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Always return true unless you have logic
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}
