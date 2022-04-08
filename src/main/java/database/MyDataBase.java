package database;

import bot.DataWeather;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.*;
import java.util.Currency;


public class MyDataBase {

    private Connection connectionDB;

    public MyDataBase() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + Const.dbHOST +":"
                + Const.dbPORT + "/" + Const.dbNAME;
        Class.forName("com.mysql.cj.jdbc.Driver");

        connectionDB = DriverManager.getConnection(connectionString, Const.dbUSER, Const.dbPASS);
    }
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
    // этот метод регестрирует пользователь в базе данных
    public void signUpUser(Long chatId, Integer messageId) {
        String insert = "INSERT INTO " + Const.TABLE + " (" + Const.CHAT_ID + "," + Const.MESSAGE_ID +") VALUES(?,?);";

        try {
            PreparedStatement prST = connectionDB.prepareStatement(insert);
            prST.setLong(1, chatId);
            prST.setLong(2, messageId);
            prST.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
    // проверяте, есть ли такой пользователь в базе данных
    public boolean hasUser(Long chatId) {
        String select = "SELECT " + Const.CHAT_ID + " FROM " + Const.TABLE + " WHERE " + Const.CHAT_ID + "=(?)";
        try {
            PreparedStatement preparedStatement = connectionDB.prepareStatement(select);
            preparedStatement.setLong(1, chatId);
            ResultSet resultSet = preparedStatement.executeQuery();

                return resultSet.next();
                //return resultSet.getLong(Const.CHAT_ID) != 0;


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("inDataBase error");
            return false;
        }
    }
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
    /** Берет данные из поля field
     * @param chatId ID пользователя
     * @param field название поля, из которого будут браться данные
     */
    private ResultSet getResultSet(Long chatId, String field) throws SQLException {
        String select = "SELECT " + field + " FROM " + Const.TABLE + " WHERE " + Const.CHAT_ID + "=?";
        PreparedStatement statement = connectionDB.prepareStatement(select);
        statement.setLong(1, chatId);
        ResultSet resultSet = statement.executeQuery();
        //resultSet.next();
        return resultSet;
    }

    /** Берет все данные
     * @param chatId ID пользователя
     */
    private ResultSet getResultSet(Long chatId) throws SQLException {
        String select = "SELECT * FROM " + Const.TABLE + " WHERE " + Const.CHAT_ID + "=?";
        PreparedStatement statement = connectionDB.prepareStatement(select);
        statement.setLong(1, chatId);
        ResultSet resultSet = statement.executeQuery();
        //resultSet.next();
        return resultSet;
    }
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
    public Integer getMessageId(Long chatId) {
        try {
            ResultSet resultSet = getResultSet(chatId, Const.MESSAGE_ID);
            resultSet.next();
            return resultSet.getInt(Const.MESSAGE_ID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //____________________________________________________________________
    public String getCurrentCity(Long chatId) throws SQLException {

            ResultSet resultSet = getResultSet(chatId, Const.CURRENT_CITY);
            resultSet.next();
            return resultSet.getString(Const.CURRENT_CITY);
    }

    public float getLongitude(Long chatId) throws SQLException{
            ResultSet resultSet = getResultSet(chatId, Const.LONGITUDE);
            resultSet.next();
            return resultSet.getFloat(Const.LONGITUDE);
    }

    public float getLatitude(Long chatId) throws SQLException{
            ResultSet resultSet = getResultSet(chatId, Const.LATITUDE);
            resultSet.next();
            return resultSet.getFloat(Const.LATITUDE);

    }

    public String getFavourCity1(Long chatId) {
        try {
            ResultSet resultSet = getResultSet(chatId, Const.CITY1);
            resultSet.next();
            return resultSet.getString(Const.CITY1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public String getFavourCity2(Long chatId) {
        try {
            ResultSet resultSet = getResultSet(chatId, Const.CITY2);
            resultSet.next();
            return resultSet.getString(Const.CITY2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "error";
    }
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
    public void setMessageId(Long chatId, Integer messageId) {
        String update = "UPDATE " + Const.TABLE + " SET " + Const.MESSAGE_ID + "=? WHERE " + Const.CHAT_ID + "=?";
        try {
            PreparedStatement preparedStatement = connectionDB.prepareStatement(update);
            preparedStatement.setInt(1, messageId);
            preparedStatement.setLong(2, chatId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Пользователь с таким ID не найден");
            e.printStackTrace();
        }
    }

    public void setCurrentCity(Long chatId, String currentCity) {
        String update = "UPDATE " + Const.TABLE + " SET " + Const.CURRENT_CITY + "=? WHERE " + Const.CHAT_ID + "=?";
        try {
            PreparedStatement preparedStatement = connectionDB.prepareStatement(update);
            preparedStatement.setString(1, currentCity);
            preparedStatement.setLong(2, chatId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLongitude(Long chatId, double longitude) {
        String update = "UPDATE " + Const.TABLE + " SET " + Const.LONGITUDE + "=? WHERE " + Const.CHAT_ID + "=?";
        try {
            PreparedStatement preparedStatement = connectionDB.prepareStatement(update);
            preparedStatement.setDouble(1, longitude);
            preparedStatement.setLong(2, chatId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLatitude(Long chatId, double latitude) {
        String update = "UPDATE " + Const.TABLE + " SET " + Const.LATITUDE + "=? WHERE " + Const.CHAT_ID + "=?";
        try {
            PreparedStatement preparedStatement = connectionDB.prepareStatement(update);
            preparedStatement.setDouble(1, latitude);
            preparedStatement.setLong(2, chatId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setFavourCity1(Long chatId, String city1) {
        String update = "UPDATE " + Const.TABLE + " SET " + Const.CITY1 + "=? WHERE " + Const.CHAT_ID + "=?";
        try {
            PreparedStatement preparedStatement = connectionDB.prepareStatement(update);
            preparedStatement.setString(1, city1);
            preparedStatement.setLong(2, chatId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setFavourCity2(Long chatId, String city2) {
        String update = "UPDATE " + Const.TABLE + " SET " + Const.CITY2 + "=? WHERE " + Const.CHAT_ID + "=?";
        try {
            PreparedStatement preparedStatement = connectionDB.prepareStatement(update);
            preparedStatement.setString(1, city2);
            preparedStatement.setLong(2, chatId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateCity(Message message, DataWeather dataWeather) {
        Long chatId = message.getChatId();
        setCurrentCity(message.getChatId(), dataWeather.getName());
        setLongitude(message.getChatId(), dataWeather.getLongitude());
        setLatitude(message.getChatId(), dataWeather.getLatitude());
    }
} //end of the class


