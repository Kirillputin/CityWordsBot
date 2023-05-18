package ru.genby.genbycitywordsbotwh.bot_api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.genby.genbycitywordsbotwh.cache.UserDataCache;
import ru.genby.genbycitywordsbotwh.constants.TextConstants;
import ru.genby.genbycitywordsbotwh.service.ReplyMessagesService;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class TelegramFacade {
    private final BotStateContext botStateContext;
    private final UserDataCache userDataCache;
    private final ReplyMessagesService messagesService;
    private final Map<String, BotState> caseHandlers = new HashMap<>() {{
        put(TextConstants.start, BotState.START);
        put(TextConstants.yesWant, BotState.FILLING_PROFILE);
        put(TextConstants.noWant, BotState.CANCEL);
        put(TextConstants.endGame, BotState.END_GAME);
        put(TextConstants.rules, BotState.RULES);
        put(TextConstants.leaders, BotState.SCOPE);
    }};

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
        long chatId = message.getChatId();

        BotState botState = caseHandlers.get(message.getText());
        if (botState == null) {
            botState = userDataCache.getUsersCurrentBotState(chatId);
        }

        userDataCache.setUsersCurrentBotState(chatId, botState);
        return botStateContext.processInputMessage(botState, message);
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        BotApiMethod<?> callBackAnswer = null;
        BotState botState = userDataCache.getUsersCurrentBotState(chatId);

//         Кнопки из начать игру
        if (!botState.equals(BotState.PROFILE_FILLED)) {
            return messagesService.getReplyMessage(chatId, "reply.error");
        }

        if (buttonQuery.getData().equals(TextConstants.buttonYes)) {
            userDataCache.setUsersCurrentBotState(chatId, BotState.START_GAME);
            callBackAnswer = botStateContext.processInputMessage(userDataCache.getUsersCurrentBotState(chatId), buttonQuery.getMessage());

        } else if (buttonQuery.getData().equals(TextConstants.buttonNo)) {
            callBackAnswer = sendAnswerCallbackQuery(messagesService.getReplyText("reply.cancel"), buttonQuery);
        }

        return callBackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(false);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }
}
//        switch (inputMsg) {
//            case TextConstants.start:
//                botState = BotState.START;
//                break;
//            case TextConstants.yesWant:
//                botState = BotState.FILLING_PROFILE;
//                break;
//            case TextConstants.noWant:
//                return messagesService.getReplyMessage(chatId, "reply.cancel");
//            case TextConstants.endGame:
//                botState = BotState.END_GAME;
//                break;
//            case TextConstants.rules:
//                return messagesService.getReplyMessage(chatId, "reply.helpText");
//            case TextConstants.leaders:
//                botState = BotState.SCOPE;
//                break;
//            default:
//                botState = userDataCache.getUsersCurrentBotState(chatId);
//                break;
//        }
