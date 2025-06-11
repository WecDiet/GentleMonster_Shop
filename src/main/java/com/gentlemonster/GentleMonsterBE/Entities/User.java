package com.gentlemonster.GentleMonsterBE.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    @Column(name = "middle_name", length = 45, nullable = false)
    private String middleName;

    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName;

    @Column(name = "slug" , length = 150, nullable = false)
    private String slug;

    @Column(name = "email", length = 200, nullable = false, unique = true)
    private String email;

    @Column(name = "code", length = 20, unique = true)
    private String code;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "phoneNumber", length = 45, unique = true)
    private String  phoneNumber;

    @Column(name = "birthday", length = 45)
    private Date birthDay;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;

//    @Column(name = "photo", length = 150)
//    private String photoUrl;

    @Column(name = "status")
    private boolean status;

    @Column(name = "userType")
    private int userType = 1; // 1: Google, 2: Facebook, 3: email

//    @Column(name = "cloudinaryImageId")
//    private String cloudinaryImageId;

    @Column(name = "facebook_account_id")
    private String facebookAccountId;

    @Column(name = "google_account_id")
    private String googleAccountId;

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
    // bảng table user sẽ join với bảng role thông qua bảng user_role với user_id và role_id là khóa ngoại của 2 bảng user và role
    // Được quản lý bởi JPA thông qua Set<Role> roles = new HashSet<>();
    // mối quan hệ nhiều nhièu giữa user và role thông qua bảng user_role
//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "user_role",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles = new HashSet<>();

    @ManyToOne
    @JoinTable(
            name = "permission",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Role role;

    // Lấy danh sách quyền của user
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
//        List<String> Permissions = role.getPermissions();
//        for (String permission : Permissions) {
//            authorityList.add(new SimpleGrantedAuthority(permission));
//        }
//        return authorityList;
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.getName().toLowerCase()));
        return authorityList;
    }

    @ManyToMany
    @JoinTable(
            name = "user_view_product",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "product_id", nullable = false)
    )
    Set<Product> viewedProductList = new HashSet<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "uniqueviews")
    private Product product;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Token> tokenList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedBackList;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Order> orderList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SocialAccount> socialAccounts;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "avatar",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private Media avatar;

}
