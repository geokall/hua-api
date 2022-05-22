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
import java.util.*;

@Entity
@Table(name = "HUA_USER")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class HuaUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    //generated from system without @hua.gr
    private String username;

    @Column(nullable = false, unique = true)
    //generated from system with @hua.gr
    private String email;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(nullable = false)
    private String name;

    @Column(name = "fatherName")
    private String fatherName;

    @Column(name = "motherName")
    private String motherName;

    @Column(name = "birthDate")
    @Temporal(TemporalType.DATE)
    private LocalDateTime birthDate;

    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    @Column(name = "gender")
    private GenderEnum gender;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "changed_date")
    private LocalDateTime dateChanged;

    @Column(name = "is_verified")
    private Boolean verified;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<HuaRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "huaUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HuaContactInfo> contactInfos = new ArrayList<>();

    @OneToMany(mappedBy = "huaUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HuaStudentDetails> studentDetails = new ArrayList<>();

    public HuaUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(LocalDateTime dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Set<HuaRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<HuaRole> roles) {
        this.roles = roles;
    }

    public List<HuaContactInfo> getContactInfos() {
        return contactInfos;
    }

    public void setContactInfos(List<HuaContactInfo> contactInfos) {
        this.contactInfos = contactInfos;
    }

    public List<HuaStudentDetails> getStudentDetails() {
        return studentDetails;
    }

    public void setStudentDetails(List<HuaStudentDetails> studentDetails) {
        this.studentDetails = studentDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuaUser huaUser = (HuaUser) o;
        return Objects.equals(id, huaUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
