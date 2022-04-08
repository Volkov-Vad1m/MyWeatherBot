package keyboards;

public class MyKeyboardOptions {

        boolean selective; // показывать всем пользователя
        boolean resizeKeyboard; // подгонка по размеру
        boolean oneTimeKeyboard; // скрывать клавиатуру или нет

        public MyKeyboardOptions(boolean selective, boolean resizeKeyboard, boolean oneTimeKeyboard) {
            this.selective = selective;
            this.resizeKeyboard = resizeKeyboard;
            this.oneTimeKeyboard = oneTimeKeyboard;
        }

        public MyKeyboardOptions() {
            this(false, true, false);
        }


} // end of the class MyKeyboardOptions
