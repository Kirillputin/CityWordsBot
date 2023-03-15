package ru.genby.genbycitywordsbotwh.Buttons;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.genby.genbycitywordsbotwh.service.ReplyMessagesService;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class MenuKeyboard {
    private final ReplyMessagesService messagesService;

    public ReplyKeyboardMarkup getEndMenuKeyboard() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);//?? за что отвечает

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();

        row1.add(new KeyboardButton("Закончить игру"));
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = getKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        row1.add(new KeyboardButton(messagesService.getReplyText("reply.yes")));
        row1.add(new KeyboardButton(messagesService.getReplyText("reply.no")));
        row2.add(new KeyboardButton(messagesService.getReplyText("reply.leaders")));
        row2.add(new KeyboardButton(messagesService.getReplyText("reply.help")));
        keyboard.add(row1);
        keyboard.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getConformKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = getKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        row1.add(new KeyboardButton(messagesService.getReplyText("reply.conformYes")));
        row2.add(new KeyboardButton(messagesService.getReplyText("reply.conformNo")));

        keyboard.add(row1);
        keyboard.add(row2);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup getKeyboardMarkup() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }
}
