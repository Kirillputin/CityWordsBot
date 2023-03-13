package ru.genby.genbycitywordsbotwh.service;

import org.springframework.stereotype.Service;
import ru.genby.genbycitywordsbotwh.model.City;
import ru.genby.genbycitywordsbotwh.repository.CityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImp implements CityService {
    private final CityRepository cityRepository;

    public CityServiceImp(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> getCitiesByCityIgnoreCaseIsLikeAndCityIgnoreCaseIsNotIn(String mask, List<String> city) {
        return cityRepository.getCitiesByCityIgnoreCaseIsLikeAndCityIgnoreCaseIsNotIn(mask.trim(), city);
    }

    @Override
    public int getCount(String mask) {
        return cityRepository.countDistinctByCityLike(mask.trim());
    }

    @Override
    public Optional<City> getCitiesById(int id) {
        return cityRepository.getCitiesById(id);
    }

    @Override
    public boolean isExistsByName(String city) {
        return cityRepository.existsCityByCityIgnoreCase(city.trim());
    }

    @Override
    public List<City> getCitiesByMask(String mask) {
        return cityRepository.findAllByCityLikeIgnoreCase(mask.trim());
    }
}
