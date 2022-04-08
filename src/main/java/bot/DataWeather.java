package bot;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class DataWeather {

    private String name;
    private Double temp;
    private Double humidity;
    private String icon;
    private String main;
    private Double feelsLike;
    private String description;
    private double latitude;
    private double longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    //возвращает информацию о погоде
    public String getInformation() {
        return        "City: " + name + "\n" +
                      "Main: " + main + "\n" +
                      "Description: " + description + "\n" +
                      "Temperature: " + temp + "C\n" +
                      "Feels like: " + feelsLike + "C\n" +
                      "Humidity: " + humidity + "%\n" +
                      "http://openweathermap.org/img/wn/" + icon +"@2x.png";
    }

    /** Заполняет поля класса, данные текущей погоды
     * @param cityName название города
     */
    public void setCurrentWeather(String cityName) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + ConstBot.API_ID);
        Scanner in = new Scanner((InputStream) url.getContent());

        StringBuilder dirtyResult = new StringBuilder();
        // считываем данные из JSON
        while(in.hasNext()){
            dirtyResult.append(in.nextLine());
        }
        //обработка данных
        JSONObject object = new JSONObject(dirtyResult.toString()); // в этом объекте все данные о погоде
        // достаём название города
        this.setName(object.getString("name"));
        // достаём координаты города
        JSONObject coord = object.getJSONObject("coord");
        this.setLatitude(coord.getFloat("lat"));
        this.setLongitude(coord.getFloat("lon"));
        //
        System.out.println(this.latitude);
        System.out.println(this.longitude);
        //
        JSONObject main = object.getJSONObject("main"); // в этом JSONObject массив "main"
        // достаем температуру, влажность
        this.setTemp(main.getDouble("temp"));
        this.setHumidity(main.getDouble("humidity"));
        this.setFeelsLike(main.getDouble("feels_like"));
        //Из массива weather достаём данные о погоде и иконку
        JSONArray weather = object.getJSONArray("weather");
        for(int i = 0; i < weather.length(); i++) {
            JSONObject currency = weather.getJSONObject(i);
            this.setMain((String)currency.get("main"));
            this.setIcon((String)currency.get("icon"));
            this.setDescription((String)currency.get("description"));
        }
    }
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
    /** Заполняет поля класса, данные текущей погоды
     * @param location геопозиция
     */
    public void setCurrentWeather(@NotNull Location location) throws IOException {
        URL url = new URL("http://api.openweathermap.org/geo/1.0/reverse?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() +"&limit=5&appid=" + ConstBot.API_ID);
        Scanner in = new Scanner((InputStream) url.getContent());

        StringBuilder dirtyResult = new StringBuilder();
        // считываем данные из JSON
        while(in.hasNext()){
            dirtyResult.append(in.nextLine());
        }

        JSONArray object = new JSONArray(dirtyResult.toString()); // в этом объекте все даныые о городе
        String cityName = object.getJSONObject(0).getString("name");

        setCurrentWeather(cityName);
    }
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
    public void setNextWeather(@NotNull Location location, int n) throws IOException {
        URL url = new URL("https://api.openweathermap.org/data/2.5/onecall?lat=" + location.getLatitude() +"&lon=" + location.getLongitude() + "&exclude=current,minutely,hourly,alerts&units=metric&appid=" + ConstBot.API_ID);
        Scanner in = new Scanner((InputStream) url.getContent());

        StringBuilder dirtyResult = new StringBuilder();
        // считываем данные из JSON
        while(in.hasNext()){
            dirtyResult.append(in.nextLine());
        }
        JSONObject object = new JSONObject(dirtyResult.toString()); // в этом объекте все данные о погоде
        JSONObject day = object.getJSONArray("daily").getJSONObject(n);

        //setWeather(day);

        this.setTemp(day.getJSONObject("temp").getDouble("day"));
        this.setFeelsLike(day.getJSONObject("feels_like").getDouble("day"));


        this.setHumidity(day.getDouble("humidity"));
        //Из массива weather достаём данные о погоде и иконку
        JSONArray weather = day.getJSONArray("weather");
        for(int i = 0; i < weather.length(); i++) {
            JSONObject currency = weather.getJSONObject(i);
            this.setMain((String)currency.get("main"));
            this.setIcon((String)currency.get("icon"));
            this.setDescription((String)currency.get("description"));
        }
    }

    public void setNextWeather(String cityName, int n) throws IOException {
        URL url = new URL("http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&limit=5&appid=" + ConstBot.API_ID);
        Scanner in = new Scanner((InputStream) url.getContent());

        StringBuilder dirtyResult = new StringBuilder();
        // считываем данные из JSON
        while(in.hasNext()){
            dirtyResult.append(in.nextLine());
        }

        JSONArray object = new JSONArray(dirtyResult.toString()); // в этом объекте все даныые о городе
        Location location = new Location();
        location.setLatitude((double) object.getJSONObject(0).getFloat("lat"));
        location.setLongitude((double) object.getJSONObject(0).getFloat("lon"));

        System.out.println(location.getLatitude());
        System.out.println(location.getLongitude());
        this.setName(object.getJSONObject(0).getString("name"));
        setNextWeather(location, n);
    }

}//end of the class
