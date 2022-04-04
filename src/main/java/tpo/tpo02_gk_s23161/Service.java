package tpo.tpo02_gk_s23161;

import java.io.IOException;

public class Service {

    private String country;
    private JSONReader reader;

    public Service(String country){
        this.country = country;
    }

    //methods
    public String getWeather(String city) throws IOException {
        reader = new JSONReader(city);
        reader.readDataWeather();
        return reader.mainMap.toString();
    }
    public Double getRateFor(String currencyCode){
        return 0.0;
    }
    public Double getNBPRate(){
        return 0.0;
    }
}
