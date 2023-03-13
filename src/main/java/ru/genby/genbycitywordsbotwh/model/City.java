package ru.genby.genbycitywordsbotwh.model;


import javax.persistence.*;

@Entity
@Table(name = "city")
public class City {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name_city")
    private String city;

    public City() {
    }

    public City(Integer id, String city) {
        this.id = id;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
