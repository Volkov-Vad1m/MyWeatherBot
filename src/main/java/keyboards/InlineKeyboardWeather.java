package keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardWeather extends InlineKeyboardMarkup{

    public InlineKeyboardWeather() {
        // все кнопки
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        // add your buttons
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("↩️ Закрыть");
        button1.setCallbackData("close");
        //add your buttons in row
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(button1);
        //add rows in list
        rowList.add(keyboardButtonsRow1);

        this.setKeyboard(rowList);
        }
}
