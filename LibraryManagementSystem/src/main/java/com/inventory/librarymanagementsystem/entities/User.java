package com.inventory.librarymanagementsystem.entities;

import com.inventory.librarymanagementsystem.enums.MembershipType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
@Entity
@Table(name="Users")
@NamedQueries({
        @NamedQuery(name="User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name="User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable=false, length=100)
    private String name;

    @Column(name="email", nullable=false, unique=true, length=100)
    private String email;

    private LocalDateTime membershipDate;
    @Enumerated(EnumType.STRING)
    @Column(name="membershipType")
    private MembershipType membershipType;
    @Column(name="password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
        membershipDate=LocalDateTime.now();
    }
    public User(String name, String email, MembershipType membershipType,String password) {
        this.name = name;
        this.email = email;
        this.membershipType = membershipType;
        this.membershipDate = LocalDateTime.now();
        this.password=password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public LocalDateTime getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDateTime membershipDate) {
        this.membershipDate = membershipDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", membershipDate=" + membershipDate +
                ", membershipType=" + membershipType +
                '}';
    }
}
