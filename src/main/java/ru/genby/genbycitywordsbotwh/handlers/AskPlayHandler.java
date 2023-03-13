package ru.genby.genbycitywordsbotwh.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.genbycitywordsbotwh.Buttons.MenuKeyboard;
import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.bot_api.InputMessageHandler;
import ru.genby.genbycitywordsbotwh.service.ReplyMessagesService;

@Slf4j
@Component
public class AskPlayHandler implements InputMessageHandler {
    private final ReplyMessagesService messagesService;
    private final MenuKeyboard menuKeyboard;

    public AskPlayHandler(ReplyMessagesService messagesService, MenuKeyboard menuKeyboard) {
        this.messagesService = messagesService;
        this.menuKeyboard = menuKeyboard;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.START;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askStartGame");
        replyToUser.setReplyMarkup(menuKeyboard.getMainMenuKeyboard());

        return replyToUser;
    }
}

