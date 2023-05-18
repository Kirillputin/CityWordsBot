package ru.genby.genbycitywordsbotwh.handlers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.bot_api.InputMessageHandler;
import ru.genby.genbycitywordsbotwh.service.ReplyMessagesService;

@Component
@AllArgsConstructor
public class HelpHandler implements InputMessageHandler {
    private final ReplyMessagesService messagesService;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.RULES;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        return messagesService.getReplyMessage(inputMsg.getChatId(), "reply.helpText");
    }
}
