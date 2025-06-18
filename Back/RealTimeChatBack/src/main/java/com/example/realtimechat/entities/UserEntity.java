package com.example.realtimechat.entities;

import com.example.realtimechat.entities.enums.Gender;
import com.example.realtimechat.entities.enums.Roles;
import com.example.realtimechat.infrastructure.helpers.HasId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements UserDetails, HasId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthdate;
    private Gender gender;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )

    private Set<UserEntity> friends = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Roles role = Roles.USER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() { //qikjo sussy baka
        return password;
    }

}

//TODO nese sbon diqka me security me kqyr te UserDetails interface

