package ru.genby.genbycitywordsbotwh.model;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileData that = (UserProfileData) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(scope, that.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, scope);
    }
}

