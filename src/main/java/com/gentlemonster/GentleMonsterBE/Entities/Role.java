package com.gentlemonster.GentleMonsterBE.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name="name", length = 40, nullable = false)
    private String name;
    @Column(name="description", length = 250, nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @CreationTimestamp
    @Column(name = "modified_date")
    private LocalDateTime  modifiedDate;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
//    @Column(name = "permission")
//    private List<String> permissions;

//    @OneToMany(mappedBy = "role")
//    private List<User> user;

}
