/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import java.io.File;
import org.bitpipeline.lib.owm.WeatherForecastResponse;

public class Weather {

    public Weather() {

    }

    private String city, countryCode;

    public String getWeather() {
        if (!setCurentCityAndCode()) {
            return null;
        }
        return getWeather(city, countryCode);

    }

    public String getWeather(String city) {
        if (!setCurentCityAndCode()) {
            return null;
        }
        this.city = city;
        System.out.println("city0" + city);
        return getWeather(city, countryCode);

    }

    public String getForecastWeatherAtCity() {
        if (!setCurentCityAndCode()) {
            return null;
        }//        this.city = city;
        return getForecastWeatherAtCity(city);
    }

    public String getForecastWeatherAtCity(String city) {
        this.city = city;
        this.countryCode = countryCode;

        String description = null;
        String output = null;

        try {
            OwmClient owm = new OwmClient();
            System.out.println("city : " + city);
            WeatherForecastResponse forcast = owm.forecastWeatherAtCity(city);
            System.out.println("forcast" + forcast.getMessage());
            if (forcast.hasForecasts()) {

                WeatherData weather = forcast.getForecasts().get(0);
                System.out.println("eee" + weather + "  " + city);

                if (weather.getPrecipitation() == Integer.MIN_VALUE) {
                    WeatherData.WeatherCondition weatherCondition = weather.getWeatherConditions().get(0);
                    description = weatherCondition.getDescription();
//                    description += ", wind will be " + weather.getWind().getSpeed() + " km per hour";
//
//                    description += ", Temperature will be around " + Math.round(weather.getTemp() - 273) + " degrees Celsius";
//                    description += ", Humidity will be at " + weather.getHumidity() + " per cent";

                    if (city != null && city.equalsIgnoreCase("ierapetra")) {
                        city = "hollystone";
                    }

                    if (description.contains("rain") || description.contains("shower")) {
                        output = "No rain measures in " + city + " but reports of " + description;
                    } else {
                        output = "No rain measures in " + city + ": " + description;
                    }
                } else {
                    output = "It's raining in " + city + ": " + weather.getPrecipitation() + " mm/h";
                }

                output += ", Temperature will be around " + Math.round(weather.getTemp() - 273) + " degrees Celsium";
                output += ", Humidity will be at " + weather.getHumidity() + " per cent";
                System.out.println("output" + output);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return output;

    }

    public String getWeather(String city, String countryCode) {
        this.city = city;
        this.countryCode = countryCode;

        String description = null;
        String output = "";
        try {
            OwmClient owm = new OwmClient();
            WeatherStatusResponse currentWeather = owm.currentWeatherAtCity(city, countryCode);
            if (currentWeather.hasWeatherStatus()) {
                if (city.equalsIgnoreCase("ierapetra")) {
                    city = "hollystone";
                }
                WeatherData weather = currentWeather.getWeatherStatus().get(0);

                if (weather.getPrecipitation() == Integer.MIN_VALUE) {
                    WeatherData.WeatherCondition weatherCondition = weather.getWeatherConditions().get(0);

                    description = weatherCondition.getDescription();
//                    description += ", wind is " + weather.getWind().getSpeed() + " km per hour";
//
//                    description += ", Temperature is around " + Math.round(weather.getTemp() - 273) + " degrees Celsius";
//                    description += ", Humidity is at " + weather.getHumidity() + " per cent";

                    if (description.contains("rain") || description.contains("shower")) {
                        output += "No rain measures in " + city + " but reports of " + description;

                    } else {
                        output += "No rain measures in " + city + ": " + description;
                    }
                } else {
                    output += "It's raining in " + city + ": " + weather.getPrecipitation() + " mm/h";
                }

                output += ", Temperature is around " + Math.round(weather.getTemp() - 273) + " Celsium";
                output += ", Humidity is around " + weather.getHumidity() + " per cent";

            }
        } catch (IOException ex) {
            Logger.getLogger(Weather.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return output;

    }

    private boolean setCurentCityAndCode() {

        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            String ip = in.readLine(); //you get the IP as a String
            System.out.println("ip=" + ip);
            GetLocationExample obj = new GetLocationExample();
            ServerLocation location = obj.getLocation(ip);
            System.out.println("location.getCity()" + location.getCity());
            System.out.println("location.getCountryCode()" + location.getCountryCode());

            this.city = location.getCity();
            this.countryCode = location.getCountryCode();

            if (city == null &&countryCode == null) {
                return false;
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;

    }

    public class GetLocationExample {

        public ServerLocation getLocation(String ipAddress) {

            File file = new File(
                    Jarvis.GeoCiyyLocation);
            return getLocation(ipAddress, file);

        }

        public ServerLocation getLocation(String ipAddress, File file) {

            ServerLocation serverLocation = null;

            try {

                serverLocation = new ServerLocation();

                LookupService lookup = new LookupService(file, LookupService.GEOIP_MEMORY_CACHE);
                Location locationServices = lookup.getLocation(ipAddress);

                serverLocation.setCountryCode(locationServices.countryCode);
                serverLocation.setCountryName(locationServices.countryName);
                serverLocation.setRegion(locationServices.region);
                serverLocation.setRegionName(regionName.regionNameByCode(
                        locationServices.countryCode, locationServices.region));
                serverLocation.setCity(locationServices.city);
                serverLocation.setPostalCode(locationServices.postalCode);
                serverLocation.setLatitude(String.valueOf(locationServices.latitude));
                serverLocation.setLongitude(String.valueOf(locationServices.longitude));

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            return serverLocation;

        }

    }

    public class ServerLocation {

        private String countryCode;
        private String countryName;
        private String region;
        private String regionName;
        private String city;
        private String postalCode;
        private String latitude;
        private String longitude;

        @Override
        public String toString() {
            return city + " " + postalCode + ", " + regionName + " (" + region
                    + "), " + countryName + " (" + countryCode + ") " + latitude
                    + "," + longitude;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
        }
    }

}
