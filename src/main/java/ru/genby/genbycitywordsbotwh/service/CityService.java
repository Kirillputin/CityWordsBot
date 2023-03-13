package ru.genby.genbycitywordsbotwh.service;

import ru.genby.genbycitywordsbotwh.model.City;

import java.util.List;
import java.util.Optional;

public interface CityService {
    List<City> getCitiesByCityIgnoreCaseIsLikeAndCityIgnoreCaseIsNotIn(String mask, List<String> city);

    int getCount(String mask);

    Optional<City> getCitiesById(int id);

    boolean isExistsByName(String city);

    List<City> getCitiesByMask(String mask);
}
