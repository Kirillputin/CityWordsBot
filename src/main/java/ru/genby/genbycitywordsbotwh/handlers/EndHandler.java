package ru.genby.genbycitywordsbotwh.handlers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.genbycitywordsbotwh.buttons.MenuKeyboard;
import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.bot_api.InputMessageHandler;
import ru.genby.genbycitywordsbotwh.cache.UserDataCache;
import ru.genby.genbycitywordsbotwh.constants.TextConstants;
import ru.genby.genbycitywordsbotwh.model.UserProfileEntity;
import ru.genby.genbycitywordsbotwh.service.*;

@Component
@AllArgsConstructor
public class EndHandler implements InputMessageHandler {

    private final MenuKeyboard menuKeyboard;
    private final ReplyMessagesService messagesService;
    private final CustomUserProfileRepo userProfileServiceImp;
    private final WordExceptionService wordExceptionServiceImp;
    private final UserDataCache userDataCache;
    private final StopWatch stopWatch;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.END;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser;

        stopWatch.stop();

        UserProfileEntity profileData = userDataCache.getUserProfileData(chatId);
        userDataCache.setUsersCurrentBotState(chatId, BotState.START);
        wordExceptionServiceImp.deleteByName(profileData.getName());
        userProfileServiceImp.save(profileData);

        replyToUser = new SendMessage((String.valueOf(chatId)), TextConstants.thanks + "\n" + TextConstants.youScope + profileData.getScope()
                + "\n" + messagesService.getReplyText("reply.endGame"));
        replyToUser.setReplyMarkup(menuKeyboard.getMainMenuKeyboard());
        return replyToUser;
    }

}
