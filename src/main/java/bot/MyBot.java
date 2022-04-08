package bot;
import keyboards.*;
import database.MyDataBase;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;

public class MyBot extends TelegramLongPollingBot {

    private static final String TOKEN = ConstBot.TOKEN;
    private static final String USERNAME = ConstBot.USERNAME;

    MyDataBase myDataBase;
    // эта клавиатура прикрепляет к каждому сообщению с погодой
    InlineKeyboardWeather inlineKeyboardWeather;
    ReplyKeyboardWeather replyKeyboardWeather;

    public MyBot(DefaultBotOptions options) throws SQLException, ClassNotFoundException {
        super(options);
        this.myDataBase = new MyDataBase();
        this.replyKeyboardWeather = new ReplyKeyboardWeather();
        this.inlineKeyboardWeather = new InlineKeyboardWeather();
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    // отправляет в чат ответное сообщение с заданным текстом
    private void sendResponseMessage(@NotNull Message message, String text) {
        String chatId = Long.toString(message.getChatId());
        SendMessage sendMessage = new SendMessage(chatId, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendResponseMessage(@NotNull Message message, String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
        String chatId = Long.toString(message.getChatId());
        SendMessage sendMessage = new SendMessage(chatId, text);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // отправляет в чат информацию о погоде. Класс DataWeather должен быть заполнен.
    private void sendWeather(@NotNull Message message, @NotNull DataWeather dataWeather) {
        String chatId = Long.toString(message.getChatId());
        SendMessage sendMessage = new SendMessage(chatId, dataWeather.getInformation());
        try {
            sendMessage.setReplyMarkup(replyKeyboardWeather);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendWeather(@NotNull Message message, @NotNull DataWeather dataWeather, InlineKeyboardMarkup inlineKeyboardMarkup) {
        String chatId = Long.toString(message.getChatId());
        SendMessage sendMessage = new SendMessage(chatId, dataWeather.getInformation());

        try {
            sendMessage.setReplyMarkup(replyKeyboardWeather);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup); // прикрепляем клавиутуру к сообщению
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /** Удаляет сообщений
     * @param chatId  ID чата
     * @param messageId  ID удаляемого сообщения
     */
    private void deleteMessage(String chatId, Integer messageId) {
        DeleteMessage dltMessage = new DeleteMessage();
        dltMessage.setChatId(chatId);
        dltMessage.setMessageId(messageId);
        try {
            execute(dltMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    // Сообщение с InlineKeyboardMarkup
    // Удаляет сообщение если произошло нажатие на кнопку
    private void deleteMessage(@NotNull CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(Long.toString(callbackQuery.getMessage().getChatId()));
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override // когда что то присходит в чате, в этот метод приходит update
    public void onUpdateReceived(@NotNull Update update) {
        Message message = update.getMessage();

        if (update.hasMessage()) {
            if (message.hasText()) {
                commandHandler(message);
            } else if (message.hasLocation()) {
                locationHandler(message);
            }

        // произошло нажатие на кнопку
        } else if (update.hasCallbackQuery()) {
            callBackQueryHandler(update.getCallbackQuery());
        }

    }  // end onUpdateReceived


    private void commandHandler(@NotNull Message message) {
        DataWeather dataWeather = new DataWeather();

        System.out.println(message.getChatId().toString() + ": " + message.getText());
        System.out.println(message.getMessageId());

        switch (message.getText()) {
            case "/start":  //good
                // Происходит запрос к базе данных. Если пользователя там нет - будет добавлен
                if(!myDataBase.hasUser(message.getChatId())) { myDataBase.signUpUser(message.getChatId(), message.getMessageId()); }
                else { myDataBase.setMessageId(message.getChatId(), message.getMessageId()); }
                sendResponseMessage(message, "Привет, я подскажу тебе погоду в любом городе.\nВведи город или отправь свою геолокацию.", new StartReplyKeyboard());
                break;
            case "\uD83C\uDF1E Погода сейчас": //good
                try {
                    dataWeather.setCurrentWeather(myDataBase.getCurrentCity(message.getChatId()));
                    sendWeather(message, dataWeather);
                    myDataBase.setMessageId(message.getChatId(), message.getMessageId());
                }
                catch (IOException e)  { e.printStackTrace(); }
                catch (SQLException e) { e.printStackTrace();
                    sendResponseMessage(message, "Не выбран город", new StartReplyKeyboard());
                }
                break;
            case "\uD83C\uDF05 На завтра": // good
                try {
                    // танцы с бубном, так как open weather api не умеет определять погоду на завтра по названию города (только платно)
                    Location location = new Location();
                    location.setLatitude((double) myDataBase.getLatitude(message.getChatId()));
                    location.setLongitude((double) myDataBase.getLongitude(message.getChatId()));
                    dataWeather.setNextWeather(myDataBase.getCurrentCity(message.getChatId()), 1);
                    sendWeather(message, dataWeather);

                    myDataBase.setMessageId(message.getChatId(), message.getMessageId());
                }
                catch (IOException e)  { e.printStackTrace(); }
                catch (SQLException e) { e.printStackTrace();
                    sendResponseMessage(message, "Не выбран город", new StartReplyKeyboard());
                }
                break;
            case "↩️ Назад":
                sendResponseMessage(message, "Узнай погоду!", new StartReplyKeyboard());
                break;
            case "⭐ Избранное":

                break;
            default:
                try {
                    dataWeather.setCurrentWeather(message.getText()); // заполняем данные в классе DataWeather
                    sendWeather(message, dataWeather);
                    myDataBase.updateCity(message, dataWeather);
                    myDataBase.setMessageId(message.getChatId(), message.getMessageId());
                } catch (IOException e) {
                    sendResponseMessage(message, "Город " + message.getText() + " не найден.");
                }
        }
    }

    private void locationHandler(@NotNull Message message) { //good
        DataWeather dataWeather = new DataWeather();
        try {
            dataWeather.setCurrentWeather(message.getLocation());
            sendWeather(message, dataWeather);
            myDataBase.updateCity(message, dataWeather);
        } catch (IOException e) {
            sendResponseMessage(message, "По этой метке нет конкретного города, попробуй снова");
            e.printStackTrace();
        }
    }

    private void callBackQueryHandler(CallbackQuery callbackQuery) {
        switch (callbackQuery.getData()) {
            case "close":
                deleteMessage(callbackQuery);
                break;
            // TODO

        } // end switch
    }

} // end of the class
