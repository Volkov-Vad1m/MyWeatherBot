package bot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {
        try {
            MyBot myBot = new MyBot(new DefaultBotOptions());
            System.out.println("Connection is good");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(myBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println("Telegram error");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection error");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

} // end of the class Main
