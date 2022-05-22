package com.hua.api.entity;

import com.hua.api.converter.PostgreSQLEnumType;
import com.hua.api.enums.GenderEnum;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "HUA_USER")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class HuaUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(name = "fatherName")
    private String fatherName;

    @Column(name = "motherName")
    private String motherName;

    @Column(name = "birthDate")
    @Temporal(TemporalType.DATE)
    private LocalDateTime birthDate;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "vat_number", nullable = false, unique = true, length = 20)
    private String vatNumber;

    @Column(name = "mobile_number", nullable = false, unique = true, length = 20)
    private String mobileNumber;

    @Column(name = "is_verified")
    private Boolean verified;

    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    @Column(name = "gender")
    private GenderEnum gender;

    @Column(name = "department")
    private String department;

    @Column(name = "direction")
    private String direction;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "changed_date")
    private LocalDateTime dateChanged;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<HuaRole> roles = new HashSet<>();

    public HuaUser() {
    }
}
