package ru.genby.genbycitywordsbotwh.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.genbycitywordsbotwh.Buttons.MenuKeyboard;
import ru.genby.genbycitywordsbotwh.bot_api.BotState;
import ru.genby.genbycitywordsbotwh.bot_api.InputMessageHandler;
import ru.genby.genbycitywordsbotwh.cache.UserDataCache;
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
public class GameHandler implements InputMessageHandler {
    private final ReplyMessagesService messagesService;
    private final UserDataCache userDataCache;
    private final CityServiceImp cityServiceImp;
    private final UserProfileServiceImp userProfileServiceImp;
    private final WordExceptionServiceImp wordExceptionServiceImp;
    private final MenuKeyboard menuKeyboard;
    private final StopWatch stopWatch;

    public GameHandler(ReplyMessagesService messagesService, UserDataCache userDataCache, CityServiceImp cityServiceImp, UserProfileServiceImp userProfileServiceImp, WordExceptionServiceImp wordExceptionServiceImp, MenuKeyboard menuKeyboard, StopWatch stopWatch) {
        this.messagesService = messagesService;
        this.userDataCache = userDataCache;
        this.cityServiceImp = cityServiceImp;
        this.userProfileServiceImp = userProfileServiceImp;
        this.wordExceptionServiceImp = wordExceptionServiceImp;
        this.menuKeyboard = menuKeyboard;
        this.stopWatch = stopWatch;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.GAME;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();//
        SendMessage replyToUser;

        String inputCity = inputMsg.getText();
        UserProfileData ProfileData = userDataCache.getUserProfileData(chatId);
        Integer scope = ProfileData.getScope();

        if (stopWatch.isOverTime()) {
            userDataCache.setUsersCurrentBotState(chatId, BotState.END);
            //время вышло, конец игры
            return new EndHandler(menuKeyboard, messagesService, userProfileServiceImp, wordExceptionServiceImp, userDataCache, stopWatch).handle(inputMsg);
        }

        userDataCache.setUsersCurrentBotState(chatId, BotState.GAME);
        String currLastLetterCity = userDataCache.getUserLetterData(chatId);

        if (!Objects.equals(Letter.getCurrFirstLetter(inputCity), currLastLetterCity)) {
            //сравнить предыдущую букву с текущей
            scope--;
            ProfileData.setScope(scope);
            replyToUser = new SendMessage((String.valueOf(chatId)), "Вам на: " + "'" + currLastLetterCity + "'!");

        } else if (wordExceptionServiceImp.isExistsByNameAndCity(ProfileData.getName(), inputCity)) {
            //город повторяется
            scope--;
            ProfileData.setScope(scope);
            replyToUser = new SendMessage((String.valueOf(chatId)), "Такой город был!");
        }

        //проверим что город существует
        else if (cityServiceImp.isExistsByName(inputCity)) {
            // правильно
            // остановим таймер
            stopWatch.stop();
            // добавим очко к счету
            scope++;
            ProfileData.setScope(scope);
            //
            String currLastLetterInputCity = Letter.getCurrLastLetter(inputCity);
//            Optional<City> cityOptional = cityServiceImp.getCitiesByMask(currLastWordCity + "%"); //падает query did not return a unique result

            List<WordException> wordExceptionsList = wordExceptionServiceImp.getWordExceptionByNameAndCityIsLikeIgnoreCase(ProfileData.getName(), currLastLetterInputCity + "%");
            List<String> cityExceptionsList = wordExceptionsList.stream().map(WordException::getCity).collect(Collectors.toList());
            List<City> cityList;

            if (cityExceptionsList.isEmpty()) {
                cityList = cityServiceImp.getCitiesByMask(currLastLetterInputCity + "%");
            } else {
                cityList = cityServiceImp.getCitiesByCityIgnoreCaseIsLikeAndCityIgnoreCaseIsNotIn(currLastLetterInputCity + "%", cityExceptionsList);
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
            //запуск таймера по новой
            stopWatch.start();
            replyToUser = new SendMessage((String.valueOf(chatId)), currCity + "." + "\n" + "Вам на: " + "'" + currLastLetterCity + "'");

        } else {
            //ошибка, Такого " " города не существует
            replyToUser = new SendMessage((String.valueOf(chatId)), "Такого города: " + inputCity + " не существует!");
        }

        replyToUser.setReplyMarkup(menuKeyboard.getEndMenuKeyboard());
        return replyToUser;
    }
}