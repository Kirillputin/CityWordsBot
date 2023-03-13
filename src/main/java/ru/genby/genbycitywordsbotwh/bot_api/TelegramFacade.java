package ru.genby.genbycitywordsbotwh.bot_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.genby.genbycitywordsbotwh.cache.UserDataCache;
import ru.genby.genbycitywordsbotwh.service.ReplyMessagesService;

@Component
@Slf4j
public class TelegramFacade {
    private final BotStateContext botStateContext;
    private final UserDataCache userDataCache;
    private final ReplyMessagesService messagesService;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache,
                          ReplyMessagesService messagesService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        long chatId = message.getChatId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.START;
                break;
            case "Да, хочу!": //Спросить про выражения
                botState = BotState.FILLING_PROFILE;
                break;
            case "Нет, спасибо":
                return messagesService.getReplyMessage(chatId, "reply.cancel");
            case "Закончить игру":
                botState = BotState.END_GAME;
                break;
            case "Правила игры":
                return messagesService.getReplyMessage(chatId, "reply.helpText");
            case "Лидеры":
                botState = BotState.SCOPE;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(chatId);
                break;
        }

        userDataCache.setUsersCurrentBotState(chatId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        BotApiMethod<?> callBackAnswer = null;
        BotState botState = userDataCache.getUsersCurrentBotState(chatId);

//         Кнопки из начать игру
        if (!botState.equals(BotState.PROFILE_FILLED)) {
            return messagesService.getReplyMessage(chatId, "reply.error");
        }

        if (buttonQuery.getData().equals("buttonYes")) {
            userDataCache.setUsersCurrentBotState(chatId, BotState.START_GAME);
            callBackAnswer = botStateContext.processInputMessage(userDataCache.getUsersCurrentBotState(chatId), buttonQuery.getMessage());

        } else if (buttonQuery.getData().equals("buttonNo")) {
            callBackAnswer = sendAnswerCallbackQuery(messagesService.getReplyText("reply.cancel"), false, buttonQuery);
        }

        return callBackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }
}

