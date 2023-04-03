package ru.genby.genbycitywordsbotwh.cache;

import org.springframework.stereotype.Component;
import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.model.UserProfileEntity;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
    private Map<Long, BotState> usersBotStates = new HashMap<>();
    private Map<Long, UserProfileEntity> usersProfileData = new HashMap<>();
    private Map<Long, String> usersLetterData = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(Long userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(Long userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.START;
        }

        return botState;
    }

    @Override
    public UserProfileEntity getUserProfileData(Long userId) {
        UserProfileEntity userProfileEntity = usersProfileData.get(userId);
        if (userProfileEntity == null) {
            userProfileEntity = new UserProfileEntity();
        }
        return userProfileEntity;
    }

    @Override
    public void saveUserProfileData(Long userId, UserProfileEntity userProfileEntity) {
        usersProfileData.put(userId, userProfileEntity);
    }

    @Override
    public void setUserLetterData(Long userId, String letter) {
        usersLetterData.put(userId, letter);
    }

    @Override
    public String getUserLetterData(Long userId) {
        return usersLetterData.get(userId);
    }
}
