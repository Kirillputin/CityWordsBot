package ru.genby.genbycitywordsbotwh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.genby.genbycitywordsbotwh.model.WordException;

import java.util.List;

public interface WordExceptionRepository extends JpaRepository<WordException, Integer> {
    List<WordException> getWordExceptionByNameAndCityIsLikeIgnoreCase(String name, String mask);

    void deleteWordExceptionByName(String name);

    boolean existsWordExceptionByNameAndAndCity(String name, String city);

    boolean existsWordExceptionByName(String name);
}
