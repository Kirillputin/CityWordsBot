package ru.genby.genbycitywordsbotwh.service;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.genby.genbycitywordsbotwh.model.UserProfileEntity;
import ru.genby.genbycitywordsbotwh.repository.UserProfileRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomUserProfileRepoImp implements CustomUserProfileRepo {
    private final UserProfileRepository userProfileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public CustomUserProfileRepoImp(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public void save(UserProfileEntity userProfileEntity) {
        userProfileRepository.save(userProfileEntity);
    }

    public List<UserProfileEntity> findOrderedBySeatNumberLimitedTo(int limit) {
        return entityManager.createQuery("SELECT user FROM UserProfileEntity user ORDER BY user.scope DESC ",
                UserProfileEntity.class).setMaxResults(limit).getResultList();
    }
}
