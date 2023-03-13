package ru.genby.genbycitywordsbotwh.model;

import javax.persistence.*;

@Entity
@Table(name = "user_profile")

public class UserProfileData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_name")
    String name;
    @Column(name = "scope")
    Integer scope;

    public UserProfileData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

}

