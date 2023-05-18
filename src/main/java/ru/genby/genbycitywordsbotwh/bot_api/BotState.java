package ru.genby.genbycitywordsbotwh.bot_api;

public enum BotState {
    GAME, // Процесс игры
    START, //Старт обработки
    END, //Полное окончание обработки
    SCOPE,//Таблица с лидерами
    END_GAME,//Окончание игрового процесса
    START_GAME,//Начало игры, первый город
    ASK_NAME,//Заполнение профиля, спрашиваем имя
    ASK_END,//Спрашиваем об окончании игры
    ANS_END,//Обрабатываем ответ от пользователя об окончании игры
    FILLING_PROFILE,//Заполнение профиля, начало
    PROFILE_FILLED, //Профиль заполнен
    CANCEL,
    RULES
}
