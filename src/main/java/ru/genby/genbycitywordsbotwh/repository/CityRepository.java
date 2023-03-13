package ru.genby.genbycitywordsbotwh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.genby.genbycitywordsbotwh.model.City;

import java.util.List;
import java.util.Optional;


public interface CityRepository extends JpaRepository<City, Integer> {
    boolean existsCityByCityIgnoreCase(String city);

    Optional<City> getCitiesById(int id);

    List<City> getCitiesByCityIgnoreCaseIsLikeAndCityIgnoreCaseIsNotIn(String mask, List<String> city);

    int countDistinctByCityLike(String mask);

    List<City> findAllByCityLikeIgnoreCase(String mask);
}
