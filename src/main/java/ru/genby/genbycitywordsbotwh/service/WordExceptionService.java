package ru.genby.genbycitywordsbotwh.service;

import ru.genby.genbycitywordsbotwh.model.WordException;

import java.util.List;

public interface WordExceptionService {
    void saveAll(List<WordException> wordExceptionsList);

    void deleteByName(String name);

    void save(WordException wordException);

    boolean isExistsByNameAndCity(String name, String city);

    boolean isExistsByName(String name);

    List<WordException> getWordExceptionByNameAndCityIsLikeIgnoreCase(String name, String mask);
}
