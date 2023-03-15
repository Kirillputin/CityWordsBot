package ru.genby.genbycitywordsbotwh.handlers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.genbycitywordsbotwh.Buttons.MenuKeyboard;
import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.bot_api.InputMessageHandler;
import ru.genby.genbycitywordsbotwh.cache.UserDataCache;
import ru.genby.genbycitywordsbotwh.constants.TextConstants;
import ru.genby.genbycitywordsbotwh.model.City;
import ru.genby.genbycitywordsbotwh.model.UserProfileData;
import ru.genby.genbycitywordsbotwh.model.WordException;
import ru.genby.genbycitywordsbotwh.service.*;
import ru.genby.genbycitywordsbotwh.utils.Letter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GameHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final CityService cityServiceImp;
    private final WordExceptionService wordExceptionServiceImp;
    private final MenuKeyboard menuKeyboard;
    private final StopWatch stopWatch;
    private final EndHandler endHandler;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.GAME;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser;

        if (stopWatch.isOverTime()) {
            userDataCache.setUsersCurrentBotState(chatId, BotState.END);
            //время вышло, конец игры
            return endHandler.handle(inputMsg);
        }

        String inputCity = inputMsg.getText();
        UserProfileData ProfileData = userDataCache.getUserProfileData(chatId);
        Integer scope = ProfileData.getScope();

        userDataCache.setUsersCurrentBotState(chatId, BotState.GAME);
        String currLastLetterCity = userDataCache.getUserLetterData(chatId);

        if (!Objects.equals(Letter.getCurrFirstLetter(inputCity), currLastLetterCity)) {
            //сравнить предыдущую букву с текущей
            scope--;
            ProfileData.setScope(scope);
            replyToUser = new SendMessage((String.valueOf(chatId)), TextConstants.youTo + "'" + currLastLetterCity + "'!");

        } else if (wordExceptionServiceImp.isExistsByNameAndCity(ProfileData.getName(), inputCity)) {
            //город повторяется
            scope--;
            ProfileData.setScope(scope);
            replyToUser = new SendMessage((String.valueOf(chatId)), TextConstants.existsCity);
        }

        //проверим что город существует
        else if (cityServiceImp.isExistsByName(inputCity)) {
            // правильно
            replyToUser = processNextCity(inputMsg);
        } else {
            replyToUser = new SendMessage((String.valueOf(chatId)), TextConstants.city + inputCity.toUpperCase() + TextConstants.noExists);
        }

        replyToUser.setReplyMarkup(menuKeyboard.getEndMenuKeyboard());
        return replyToUser;
    }

    private SendMessage processNextCity(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        UserProfileData ProfileData = userDataCache.getUserProfileData(chatId);
        Integer scope = ProfileData.getScope();
        String inputCity = inputMsg.getText();
        String currLastLetterCity;

        stopWatch.stop();
        scope++;
        ProfileData.setScope(scope);

        String currLastLetterInputCity = Letter.getCurrLastLetter(inputCity);
        List<WordException> wordExceptionsList = wordExceptionServiceImp.getWordExceptionByNameAndCityIsLikeIgnoreCase(ProfileData.getName(), currLastLetterInputCity + TextConstants.maskSymbol);
        List<String> cityExceptionsList = wordExceptionsList.stream().map(WordException::getCity).collect(Collectors.toList());
        List<City> cityList;

        if (cityExceptionsList.isEmpty()) {
            cityList = cityServiceImp.getCitiesByMask(currLastLetterInputCity + TextConstants.maskSymbol);
        } else {
            cityList = cityServiceImp.getCitiesByCityIgnoreCaseIsLikeAndCityIgnoreCaseIsNotIn(currLastLetterInputCity + TextConstants.maskSymbol, cityExceptionsList);
        }

        int indexRandomCity = (int) Math.floor(Math.random() * cityList.size());
        String currCity = String.valueOf(cityList.get(indexRandomCity).getCity());

        currLastLetterCity = Letter.getCurrLastLetter(currCity);
        userDataCache.setUserLetterData(chatId, currLastLetterCity);

        List<WordException> wordExceptionList = new ArrayList<>();
        WordException currWordException = new WordException();
        WordException inputWordException = new WordException();

        currWordException.setCity(currCity);
        currWordException.setName(userDataCache.getUserProfileData(chatId).getName());
        wordExceptionList.add(currWordException);

        inputWordException.setCity(inputCity);
        inputWordException.setName(userDataCache.getUserProfileData(chatId).getName());
        wordExceptionList.add(inputWordException);

        wordExceptionServiceImp.saveAll(wordExceptionList);
        stopWatch.start();

        return new SendMessage((String.valueOf(chatId)), currCity + "." + "\n" + TextConstants.youTo + "'" + currLastLetterCity + "'");
    }
}