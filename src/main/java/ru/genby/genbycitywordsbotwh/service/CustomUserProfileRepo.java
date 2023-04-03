package ru.genby.genbycitywordsbotwh.service;

import ru.genby.genbycitywordsbotwh.model.UserProfileEntity;

import java.util.List;

public interface CustomUserProfileRepo {
    void save(UserProfileEntity userProfileEntity);

    List<UserProfileEntity> findOrderedBySeatNumberLimitedTo(int limit);
}
