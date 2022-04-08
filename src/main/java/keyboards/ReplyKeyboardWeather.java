package keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardWeather extends ReplyKeyboardMarkup {
    {
        List<KeyboardRow> keyboardRowList = new ArrayList<>(); // создание кнопок
        KeyboardRow row1 = new KeyboardRow(); // слой кнопок
        KeyboardRow row2 = new KeyboardRow();
        // добавляем в строчку кнопки
        KeyboardButton keyboardButton1 = new KeyboardButton("\uD83C\uDF1E Погода сейчас");
        KeyboardButton keyboardButton2 = new KeyboardButton("\uD83C\uDF05 На завтра");
        KeyboardButton keyboardButton3 = new KeyboardButton("↩️ Назад");

        row1.add(keyboardButton1);
        row1.add(keyboardButton2);
        row2.add(keyboardButton3);
        // добавляем срочки кнопок в клавиатуру
        keyboardRowList.add(row1);
        keyboardRowList.add(row2);
        // устанавливаем список кнопок на клавиатуре
        this.setKeyboard(keyboardRowList);
        this.setInputFieldPlaceholder("Получи прогноз");
    }


    public ReplyKeyboardWeather(MyKeyboardOptions options) {
        // настройка
        this.setSelective(options.selective); // показывать всем пользователям
        this.setResizeKeyboard(options.resizeKeyboard); // подгонка по размеру
        this.setOneTimeKeyboard(options.oneTimeKeyboard); // скрывать клавиатуру или нет
    }

    public ReplyKeyboardWeather() {
        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);
    }
}
