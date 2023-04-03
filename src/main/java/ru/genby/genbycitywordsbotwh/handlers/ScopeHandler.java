package ru.genby.genbycitywordsbotwh.handlers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.bot_api.InputMessageHandler;
import ru.genby.genbycitywordsbotwh.cache.UserDataCache;
import ru.genby.genbycitywordsbotwh.constants.TextConstants;
import ru.genby.genbycitywordsbotwh.model.UserProfileEntity;
import ru.genby.genbycitywordsbotwh.service.CustomUserProfileRepo;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScopeHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final CustomUserProfileRepo userProfileService;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SCOPE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser;

        List<UserProfileEntity> userProfileData = userProfileService.findOrderedBySeatNumberLimitedTo(10);

        StringBuilder message = new StringBuilder(TextConstants.tableLeaders);
        int numb = 0;

        for (UserProfileEntity profile : userProfileData
        ) {
            message.append("\n").append(++numb).append(". ").append(profile.getName()).append(" - ").append(profile.getScope());
        }

        replyToUser = new SendMessage((String.valueOf(chatId)), message.toString());
        userDataCache.setUsersCurrentBotState(chatId, BotState.START);
        return replyToUser;
    }
}
