package ru.genby.genbycitywordsbotwh.service;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.genby.genbycitywordsbotwh.model.UserProfileData;
import ru.genby.genbycitywordsbotwh.repository.UserProfileRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Repository
public class UserProfileServiceImp implements UserProfileService {
    private UserProfileRepository userProfileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public UserProfileServiceImp(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public void save(UserProfileData userProfileData) {
        userProfileRepository.save(userProfileData);
    }

    public List<UserProfileData> findOrderedBySeatNumberLimitedTo(int limit) {
        return entityManager.createQuery("SELECT user FROM UserProfileData user ORDER BY user.scope DESC ",
                UserProfileData.class).setMaxResults(limit).getResultList();
    }
}
