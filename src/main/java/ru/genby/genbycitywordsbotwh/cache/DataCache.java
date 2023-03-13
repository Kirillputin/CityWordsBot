package ru.genby.genbycitywordsbotwh.cache;

import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.model.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState(Long userId, BotState botState);

    BotState getUsersCurrentBotState(Long userId);

    UserProfileData getUserProfileData(Long userId);

    void saveUserProfileData(Long userId, UserProfileData userProfileData);

    void setUserLetterData(Long userId, String letter);

    String getUserLetterData(Long userId);
}
