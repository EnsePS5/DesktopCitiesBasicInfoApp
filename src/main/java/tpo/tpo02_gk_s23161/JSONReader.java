package tpo.tpo02_gk_s23161;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JSONReader {

    private static final String API_KEY = "3224f4c32eab6bb02f132e940a5a69b1";
    private final String urlString;
    private String iconID;

    public Map<String, Object> resultMap, mainMap, windMap, iconMap;

    //TODO maybe lat lon geolocation
    public JSONReader(String[] cityData){
        urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                + cityData[0] + "," + cityData[1] + "&appid=" + API_KEY + "&units=metric";
    }
    public JSONReader(String cityData){
        urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                + cityData + "&appid=" + API_KEY + "&units=metric";
    }

    public static Map<String, Object> jsonToMap(String record){
        return new Gson().fromJson(
                record, new TypeToken<HashMap<String, Object>>() {}.getType()
        );
    }

    public void readData() throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line;
        while ((line = bufferedReader.readLine()) != null){
            result.append(line);
            System.out.println(line);
        }
        bufferedReader.close();

        System.out.println(result);

        resultMap = jsonToMap(result.toString());
        mainMap = jsonToMap(resultMap.get("main").toString());
        windMap = jsonToMap(resultMap.get("wind").toString());

        ArrayList<Map<String, Object>> iconArr = ((ArrayList<Map<String, Object>>) resultMap.get("weather"));
        iconMap = iconArr.get(iconArr.size()-1);

        System.out.println("temp -> " + mainMap.get("temp"));
        System.out.println("humidity -> " + mainMap.get("humidity"));
        System.out.println("wind -> " + windMap.get("speed"));
        System.out.println("wind Angle -> " + windMap.get("deg"));

    }
    public String weatherStatus(){

        iconID = iconMap.get("icon").toString();

        return switch (iconID) {
            case "01d" -> "Sun.png";
            case "02d", "03d" -> "CoveredSun.png";
            case "01n" -> "Moon.png";
            case "02n", "03n" -> "CoveredMoon.png";
            case "04n", "04d" -> "AllClouds.png";
            case "09d", "09n", "10d", "10n" -> "Rain.png";
            case "11d", "11n" -> "Storm.png";
            case "13d", "13n" -> "Snow.png";
            default -> "SnowAndRain.png";
        };
    }

}
