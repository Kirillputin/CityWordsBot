package ru.genby.genbycitywordsbotwh.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.genby.genbycitywordsbotwh.CityWordsBot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class StopWatch {
    private final CityWordsBot cityWordsBot;
    private long chatId;
    private int elapsedTime = 0;
    private boolean overTime;

    Timer timer = new Timer(1000, new ActionListener() {

        public void actionPerformed(ActionEvent e) {

            elapsedTime = elapsedTime + 1000;

            if (elapsedTime == 30000) {
                cityWordsBot.sendMessage(chatId, "Осталось 30 секунд");
            } else if (elapsedTime == 45000) {
                cityWordsBot.sendMessage(chatId, "Осталось 15 секунд");
            } else if (elapsedTime == 55000) {
                cityWordsBot.sendMessage(chatId, "Осталось 5 секунд!");
            } else if (elapsedTime == 60000) {
                cityWordsBot.sendMessage(chatId, "Время вышло!");
                overTime = true;
            }
        }
    });

    public StopWatch(@Lazy CityWordsBot cityWordsBot) {
        this.cityWordsBot = cityWordsBot;
    }

    public void start() {
        overTime = false;
        timer.start();
    }

    public void stop() {
        timer.stop();
        overTime = false;
        elapsedTime = 0;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public boolean isOverTime() {
        return overTime;
    }

}
