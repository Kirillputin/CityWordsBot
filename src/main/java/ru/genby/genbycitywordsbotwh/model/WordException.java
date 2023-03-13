package ru.genby.genbycitywordsbotwh.model;

import javax.persistence.*;

@Entity
@Table(name = "word_except")
public class WordException {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_name")
    private String name;
    @Column(name = "city_name")
    private String city;

    public WordException() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
