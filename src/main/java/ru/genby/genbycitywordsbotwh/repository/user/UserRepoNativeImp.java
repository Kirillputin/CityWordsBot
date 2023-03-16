package ru.genby.genbycitywordsbotwh.repository.user;

import lombok.RequiredArgsConstructor;
import ru.genby.genbycitywordsbotwh.model.UserProfileData;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
public class UserRepoNativeImp implements UserRepoNative<UserProfileData> {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<UserProfileData> findOrderedBySeatNumberLimitedTo(int limit) {
        return entityManager.createQuery("SELECT user FROM UserProfileData user ORDER BY user.scope DESC ",
                UserProfileData.class).setMaxResults(limit).getResultList();
    }
}
