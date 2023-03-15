package ru.genby.genbycitywordsbotwh.handlers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.bot_api.InputMessageHandler;
import ru.genby.genbycitywordsbotwh.cache.UserDataCache;
import ru.genby.genbycitywordsbotwh.constants.TextConstants;
import ru.genby.genbycitywordsbotwh.model.UserProfileData;
import ru.genby.genbycitywordsbotwh.service.UserProfileServiceImp;

import java.util.List;

@Component
@AllArgsConstructor
public class ScopeHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final UserProfileServiceImp userProfileServiceImp;

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

        List<UserProfileData> userProfileData = userProfileServiceImp.findOrderedBySeatNumberLimitedTo(10);

        StringBuilder message = new StringBuilder(TextConstants.tableLeaders);
        int numb = 0;

        for (UserProfileData profile : userProfileData
        ) {
            message.append("\n").append(++numb).append(". ").append(profile.getName()).append(" - ").append(profile.getScope());
        }

        replyToUser = new SendMessage((String.valueOf(chatId)), message.toString());
        userDataCache.setUsersCurrentBotState(chatId, BotState.START);
        return replyToUser;
    }
}
