package ru.genby.genbycitywordsbotwh.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.genby.genbycitywordsbotwh.model.WordException;
import ru.genby.genbycitywordsbotwh.repository.WordExceptionRepository;

import java.util.List;

@Service
public class WordExceptionServiceImp implements WordExceptionService {

    private final WordExceptionRepository wordExceptionRepository;

    public WordExceptionServiceImp(WordExceptionRepository wordExceptionRepository) {
        this.wordExceptionRepository = wordExceptionRepository;
    }

    @Override
    @Transactional
    public void saveAll(List<WordException> wordExceptionsList) {
        wordExceptionRepository.saveAll(wordExceptionsList);
    }

    @Override
    @Transactional //СОХРАНИТЬ-УДАЛИТЬ-АБДЕЙТ ТРАНЗАКЦИОННЫЕ!!
    public void deleteByName(String name) {
        wordExceptionRepository.deleteWordExceptionByName(name.trim());
    }

    @Override
    @Transactional
    public void save(WordException wordException) {
        wordExceptionRepository.save(wordException);
    }

    @Override
    public boolean isExistsByNameAndCity(String name, String city) {
        return wordExceptionRepository.existsWordExceptionByNameAndAndCity(name.trim(), city.trim());
    }

    @Override
    public boolean isExistsByName(String name) {
        return wordExceptionRepository.existsWordExceptionByName(name.trim());
    }

    @Override
    public List<WordException> getWordExceptionByNameAndCityIsLikeIgnoreCase(String name, String mask) {
        return wordExceptionRepository.getWordExceptionByNameAndCityIsLikeIgnoreCase(name.trim(), mask.trim());
    }
}
