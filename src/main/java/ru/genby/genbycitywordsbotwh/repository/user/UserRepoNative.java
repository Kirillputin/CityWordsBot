package ru.genby.genbycitywordsbotwh.repository.user;

import ru.genby.genbycitywordsbotwh.model.UserProfileData;

import java.util.List;

public interface UserRepoNative<T> {
    List<T> findOrderedBySeatNumberLimitedTo(int limit);
}
