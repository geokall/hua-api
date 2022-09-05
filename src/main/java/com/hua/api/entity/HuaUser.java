package com.hua.api.entity;

import com.hua.api.enums.GenderEnum;
import com.hua.api.enums.MyPostgreSQLEnumType;
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
@TypeDef(name = "pgsql_enum", typeClass = MyPostgreSQLEnumType.class)
public class HuaUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    //generated from system without @hua.gr
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    //generated from system with @hua.gr
    private String email;

    //for testing reasons
    @Column(name = "personal_email")
    private String personalEmail;

    @Column(name = "surname")
    private String surname;

    @Column(nullable = false)
    private String name;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    @Column(name = "gender")
    private GenderEnum gender;

    //contact info
    private String address;

    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "mobile_number", nullable = false, unique = true, length = 20)
    private String mobileNumber;

    @Column(name = "vat_number", nullable = false, unique = true, length = 20)
    private String vatNumber;

    //student details
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

    @Column(name = "is_verified")
    private Boolean verified;

    @Column(name = "file_name")
    private String fileName;

    @CreatedDate
    @Column(name = "date_file_created")
    private LocalDateTime dateFileCreated;

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
    private List<HuaEvent> huaEvents = new ArrayList<>();

    public void addRole(HuaRole role) {
        roles.add(role);
        role.getRoles().add(this);
    }

    public void removeRole(HuaRole role) {
        roles.remove(role);
        role.getRoles().remove(this);
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getDateFileCreated() {
        return dateFileCreated;
    }

    public void setDateFileCreated(LocalDateTime dateFileCreated) {
        this.dateFileCreated = dateFileCreated;
    }

    public Set<HuaRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<HuaRole> roles) {
        this.roles = roles;
    }

    public List<HuaEvent> getHuaEvents() {
        return huaEvents;
    }

    public void setHuaEvents(List<HuaEvent> huaEvents) {
        this.huaEvents = huaEvents;
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
