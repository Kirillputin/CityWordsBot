package ru.genby.genbycitywordsbotwh.cache;

import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.model.UserProfileEntity;

public interface DataCache {
    void setUsersCurrentBotState(Long userId, BotState botState);

    BotState getUsersCurrentBotState(Long userId);

    UserProfileEntity getUserProfileData(Long userId);

    void saveUserProfileData(Long userId, UserProfileEntity userProfileEntity);

    void setUserLetterData(Long userId, String letter);

    String getUserLetterData(Long userId);
}
