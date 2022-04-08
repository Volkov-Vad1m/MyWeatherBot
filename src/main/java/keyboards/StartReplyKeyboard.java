package keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class StartReplyKeyboard extends ReplyKeyboardMarkup {

    public StartReplyKeyboard ()  {
        List<KeyboardRow> keyboardRowList = new ArrayList<>(); // создание кнопок
        KeyboardRow FirstRow = new KeyboardRow(); // слой кнопок
        // добавляем в строчку кнопки
        KeyboardButton keyboardButton1 = new KeyboardButton("\uD83D\uDCCD Оправить геопозицию.");
        keyboardButton1.setRequestLocation(true);
        FirstRow.add(keyboardButton1);

        KeyboardButton keyboardButton2 = new KeyboardButton("⭐ Избранное");
        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(keyboardButton2);
        keyboardRowList.add(FirstRow); // добавляем срочки кнопок в клавиатуру
        keyboardRowList.add(secondRow);

        this.setInputFieldPlaceholder("Выбери действие");
        this.setKeyboard(keyboardRowList);// устанавливаем список кнопок на клавиатуре

        this.setSelective(false); // показывать всем пользователям
        this.setResizeKeyboard(true); // подгонка по размеру
        this.setOneTimeKeyboard(false); // скрывать клавиатуру или нет
    }


//    public MyKeyboard(MyKeyboardOptions options) {
//        // настройка
//        this.setSelective(options.selective); // показывать всем пользователям
//        this.setResizeKeyboard(options.resizeKeyboard); // подгонка по размеру
//        this.setOneTimeKeyboard(options.oneTimeKeyboard); // скрывать клавиатуру или нет
//
//    }

}

