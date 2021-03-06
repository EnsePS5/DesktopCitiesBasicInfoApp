package tpo.tpo02_gk_s23161;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JSONReader {

    private static final String API_KEY = "3224f4c32eab6bb02f132e940a5a69b1";

    private final String urlWeatherString;
    private final String urlCurrencyString;
    private final String urlCurrencyPLNString;
    private String iconID;

    public Map<String, Object> resultMap, mainMap, windMap, iconMap,
            currencyResultMap, currencyRateInfoMap, currencyPLNResultMap, currencyPLNInfoMap,
            fluctuationResultMap, fluctuationInfoMap, fluctuationRateInfoMap, fluctuationPLNInfoMap;

    //TODO maybe lat lon geolocation
    public JSONReader(String[] cityData,String currency){
        urlWeatherString = "https://api.openweathermap.org/data/2.5/weather?q="
                + cityData[0] + "," + cityData[1] + "&appid=" + API_KEY + "&units=metric";
        urlCurrencyString = "https://api.exchangerate.host/convert?from="
                + getCurrency(cityData[1]) + "&to=" + currency;
        urlCurrencyPLNString = "https://api.exchangerate.host/convert?from=PLN&to=" + currency;
    }
    public JSONReader(String cityData){
        urlWeatherString = "https://api.openweathermap.org/data/2.5/weather?q="
                + cityData + "&appid=" + API_KEY + "&units=metric";
        urlCurrencyString = "https://api.exchangerate.host/convert?from=PLN&to=PLN";
        urlCurrencyPLNString = "https://api.exchangerate.host/convert?from=PLN&to=PLN";
    }

    public static Map<String, Object> jsonToMap(String record){
        return new Gson().fromJson(
                record, new TypeToken<HashMap<String, Object>>() {}.getType()
        );
    }

    //converts data from JSON to string and packs it into map variable
    public void readDataWeather() throws IOException {

        StringBuilder result = readJSON(urlWeatherString,false);

        System.out.println(result);

        resultMap = jsonToMap(result.toString());
        mainMap = jsonToMap(resultMap.get("main").toString());
        windMap = jsonToMap(resultMap.get("wind").toString());

        ArrayList<Map<String, Object>> iconArr = ((ArrayList<Map<String, Object>>) resultMap.get("weather"));
        iconMap = iconArr.get(iconArr.size()-1);
    }

    //converts data from JSON to string and packs it into map variable
    public void readDataRateCurrency() throws IOException {

        StringBuilder result = readJSON(urlCurrencyString,true);

        currencyResultMap = jsonToMap(result.toString());
        currencyRateInfoMap = jsonToMap(currencyResultMap.get("query").toString());

    }

    //converts data from JSON to string and packs it into map variable
    public void readDataPLNCurrency() throws IOException{

        StringBuilder result = readJSON(urlCurrencyPLNString,true);

        currencyPLNResultMap = jsonToMap(result.toString());
        currencyPLNInfoMap = jsonToMap(currencyPLNResultMap.get("query").toString());

    }

    //converts data from JSON to string and packs it into map variable
    public void readDataRateImage() throws IOException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime yesterday = LocalDateTime.now();

        yesterday = yesterday.minusDays(1);

        LocalDateTime beforeYesterday = yesterday.minusDays(1);
        System.out.println(dtf.format(yesterday) + " " + dtf.format(beforeYesterday));

        StringBuilder result = readJSON("https://api.exchangerate.host/fluctuation?start_date=" +
                dtf.format(yesterday) +"&end_date=" + dtf.format(beforeYesterday),true);

        System.out.println(result);

        fluctuationResultMap = jsonToMap(result.toString());
        fluctuationInfoMap = jsonToMap(fluctuationResultMap.get("rates").toString());
        fluctuationRateInfoMap = jsonToMap(fluctuationInfoMap.get(currencyRateInfoMap.get("from").toString()).toString());
        fluctuationPLNInfoMap = jsonToMap(fluctuationInfoMap.get(currencyPLNInfoMap.get("from").toString()).toString());
    }
    public String fluctuationRateStatusInfo(){

        DecimalFormat df = new DecimalFormat("0.0000");

        return df.format(Double.parseDouble(fluctuationRateInfoMap.get("change_pct").toString())) + "%";
    }

    public String fluctuationPLNStatusInfo(){

        DecimalFormat df = new DecimalFormat("0.0000");

        return df.format(Double.parseDouble(fluctuationPLNInfoMap.get("change_pct").toString())) + "%";
    }

    public String fluctuationImageRateStatusImage(){
        if (Double.parseDouble(fluctuationRateInfoMap.get("change_pct").toString()) >= 0)
            return "UpArrow.png";

        return "DownArrow.png";
    }

    public String fluctuationImagePLNStatusImage(){
        if (Double.parseDouble(fluctuationPLNInfoMap.get("change_pct").toString()) >= 0)
            return "UpArrow.png";

        return "DownArrow.png";
    }

    //basic info about the currency Rate in current location
    public String currencyRateStatusInfo(){
        return currencyRateInfoMap.get("from").toString() + " -> " +
                currencyRateInfoMap.get("to").toString() + "\n" + currencyResultMap.get("result").toString();
    }

    //basic info about the PLN rate to given currency
    public String currencyPLNStatusInfo(){
        return currencyPLNInfoMap.get("from").toString() + " -> " +
                currencyPLNInfoMap.get("to").toString() + "\n" + currencyPLNResultMap.get("result").toString();
    }
    // depends on "icon" id returns certain file name to load
    public String weatherStatusImage(){

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
            case "50d", "50n" -> "Mist.png";
            default -> "SnowAndRain.png";
        };
    }
    // basic info about the weather in current location
    public String weatherStatusInfo(){
        return iconMap.get("description").toString() + "\n" + mainMap.get("pressure").toString() + " HPa\n" +
                mainMap.get("humidity").toString() + "%\n" + windMap.get("speed").toString() + "km/h";
    }
    // basic info about the temperature in current location
    public String tempStatusInfo(){
        return "temperature\n" + (int)Double.parseDouble(mainMap.get("temp").toString()) + "??C\nfeels like\n"
                + (int)Double.parseDouble(mainMap.get("feels_like").toString()) + "??C";
    }
    // depends on "temp" value returns certain file name to load
    public String tempStatusImage(){

        iconID = mainMap.get("temp").toString();

        if (Double.parseDouble(iconID) < 5)
            return "LowTemp.png";
        if (Double.parseDouble(iconID) >= 5 && Double.parseDouble(iconID) < 25)
            return "NormalTemp.png";

        return "HighTemp.png";
    }
    // returns currency that is obligatory in this country
    public static String getCurrency(String country){
        Locale[] availableLocales = Locale.getAvailableLocales();
        for (Locale availableLocale : availableLocales) {
            if (availableLocale.getCountry().equalsIgnoreCase(country))
                return Currency.getInstance(availableLocale).getCurrencyCode();
        }
        return "";
    }
    // reads JSON files and coverts them
    private static StringBuilder readJSON(String urlVar, boolean isHttp) throws IOException {

        StringBuilder result = new StringBuilder();
        URL url = new URL(urlVar);
        BufferedReader bufferedReader;

        if (isHttp){
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            bufferedReader = new BufferedReader(new InputStreamReader((InputStream) request.getContent()));
        }else {
            URLConnection conn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }

        String line;
        while ((line = bufferedReader.readLine()) != null){
            result.append(line);
        }
        System.out.println(result);

        bufferedReader.close();

        return result;
    }
}
