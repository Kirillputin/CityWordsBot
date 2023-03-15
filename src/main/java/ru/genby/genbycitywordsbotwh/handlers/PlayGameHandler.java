package ru.genby.genbycitywordsbotwh.handlers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.bot_api.InputMessageHandler;
import ru.genby.genbycitywordsbotwh.cache.UserDataCache;
import ru.genby.genbycitywordsbotwh.model.City;
import ru.genby.genbycitywordsbotwh.model.WordException;
import ru.genby.genbycitywordsbotwh.service.*;
import ru.genby.genbycitywordsbotwh.utils.Letter;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PlayGameHandler implements InputMessageHandler {
    private final ReplyMessagesService messagesService;
    private final UserDataCache userDataCache;
    private final CityService cityServiceImp;
    private final WordExceptionService wordExceptionServiceImp;
    private final StopWatch stopWatch;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.START_GAME;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser;

        if (wordExceptionServiceImp.isExistsByName(userDataCache.getUserProfileData(chatId).getName())) {
            userDataCache.setUsersCurrentBotState(chatId, BotState.PROFILE_FILLED);
            replyToUser = new SendMessage((String.valueOf(chatId)), messagesService.getReplyText("reply.wrongUserName"));
            return replyToUser;
        }

        int countDB = cityServiceImp.getCount("%");
        int indexFirstCity = (int) Math.floor(Math.random() * countDB);

        Optional<City> cityList = cityServiceImp.getCitiesById(indexFirstCity);

        String currCity = cityList.get().getCity();
        String currLastLetterCity = Letter.getCurrLastLetter(currCity);

        userDataCache.setUserLetterData(chatId, currLastLetterCity);
        userDataCache.setUsersCurrentBotState(chatId, BotState.GAME);

        WordException wordException = new WordException();
        wordException.setCity(currCity);
        wordException.setName(userDataCache.getUserProfileData(chatId).getName());

        wordExceptionServiceImp.save(wordException);

        replyToUser = new SendMessage((String.valueOf(chatId)), currCity + "." + "\n" + "Вам на: " + "'" + currLastLetterCity + "'");

        stopWatch.setChatId(chatId);
        stopWatch.start();

        return replyToUser;

    }
}
