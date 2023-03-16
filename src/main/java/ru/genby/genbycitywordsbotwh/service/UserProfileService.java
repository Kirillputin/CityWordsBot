package ru.genby.genbycitywordsbotwh.service;

import ru.genby.genbycitywordsbotwh.model.UserProfileData;

import java.util.List;

public interface UserProfileService {
    void save(UserProfileData userProfileData);

    List<UserProfileData> findOrderedBySeatNumberLimitedTo(int limit);
}
