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
import ru.genby.genbycitywordsbotwh.service.*;

@Component
@AllArgsConstructor
public class EndGameHandler implements InputMessageHandler {
    private final MenuKeyboard menuKeyboard;
    private final ReplyMessagesService messagesService;
    private final UserDataCache userDataCache;
    private final EndHandler endHandler;

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.END_GAME)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_END);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.END_GAME;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        String message = inputMsg.getText();
        SendMessage replyToUser = null;

        BotState botState = userDataCache.getUsersCurrentBotState(chatId);

        if (botState.equals(BotState.ASK_END)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.conform");
            replyToUser.setReplyMarkup(menuKeyboard.getConformKeyboard());
            userDataCache.setUsersCurrentBotState(chatId, BotState.ANS_END);
        }

        if (botState.equals(BotState.ANS_END)) {
            switch (message) {
                case TextConstants.yes -> {
                    botState = BotState.END;
                    userDataCache.setUsersCurrentBotState(chatId, botState);
                    replyToUser = endHandler.handle(inputMsg);
                }
                case TextConstants.no -> {
                    userDataCache.setUsersCurrentBotState(chatId, BotState.GAME);
                    replyToUser = new SendMessage((String.valueOf(chatId)), TextConstants.continues
                            + "\n" + TextConstants.youTo + "'" + userDataCache.getUserLetterData(chatId) + "'");
                    replyToUser.setReplyMarkup(menuKeyboard.getEndMenuKeyboard());
                }
                default -> replyToUser = messagesService.getReplyMessage(chatId, "reply.error");
            }
        }

        return replyToUser;
    }
}




