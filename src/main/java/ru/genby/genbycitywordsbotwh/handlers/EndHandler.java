package ru.genby.genbycitywordsbotwh.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.genbycitywordsbotwh.Buttons.MenuKeyboard;
import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.bot_api.InputMessageHandler;
import ru.genby.genbycitywordsbotwh.cache.UserDataCache;
import ru.genby.genbycitywordsbotwh.model.UserProfileData;
import ru.genby.genbycitywordsbotwh.service.ReplyMessagesService;
import ru.genby.genbycitywordsbotwh.service.StopWatch;
import ru.genby.genbycitywordsbotwh.service.UserProfileServiceImp;
import ru.genby.genbycitywordsbotwh.service.WordExceptionServiceImp;

@Component
public class EndHandler implements InputMessageHandler {

    private final MenuKeyboard menuKeyboard;
    private final ReplyMessagesService messagesService;
    private final UserProfileServiceImp userProfileServiceImp;
    private final WordExceptionServiceImp wordExceptionServiceImp;
    private final UserDataCache userDataCache;
    private final StopWatch stopWatch;

    public EndHandler(MenuKeyboard menuKeyboard, ReplyMessagesService messagesService, UserProfileServiceImp userProfileServiceImp, WordExceptionServiceImp wordExceptionServiceImp, UserDataCache userDataCache, StopWatch stopWatch) {
        this.menuKeyboard = menuKeyboard;
        this.messagesService = messagesService;
        this.userProfileServiceImp = userProfileServiceImp;
        this.wordExceptionServiceImp = wordExceptionServiceImp;
        this.userDataCache = userDataCache;
        this.stopWatch = stopWatch;
    }

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

        UserProfileData profileData = userDataCache.getUserProfileData(chatId);
        userDataCache.setUsersCurrentBotState(chatId, BotState.START);
        wordExceptionServiceImp.deleteByName(profileData.getName());
        userProfileServiceImp.save(profileData);

        replyToUser = new SendMessage((String.valueOf(chatId)), "Спасибо за игру!" + "\n" + "Ваш счет: " + profileData.getScope()
                + "\n" + messagesService.getReplyText("reply.endGame"));
        replyToUser.setReplyMarkup(menuKeyboard.getMainMenuKeyboard());
        return replyToUser;
    }

}
