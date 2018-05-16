
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 *
 * @author tsoglani
 */
public class Info {

    public static void main(String[] args) {
        System.out.println(new Info().getWeather("Amsterdam"));
    }

    String getTime(String info) {
        String respondHour = null;
        info=info.toLowerCase();
        try {
            String a = "https://www.timeanddate.com/worldclock/?query=";
            String[] infoArray = info.split(" ");
            String extraInfo = "";
            for (int i = 0; i < infoArray.length; i++) {
                if (i < (infoArray.length - 1)) {
                    extraInfo += infoArray[i] + "+";
                } else {
                    extraInfo += infoArray[i];
                }
            }
            if (infoArray.length == 0) {
                extraInfo = info;
            }
            a += extraInfo;

            URLConnection connection = new URL(a).openConnection();
            connection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();

            BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                    Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }

            String[] respondArray = sb.toString().split("<td id=p0 class=rbi>");
            respondHour = respondArray[1].split("</td>")[0];
            respondHour = respondHour.split(" ")[1] + " " + respondHour.split(" ")[2];
            respondHour = respondHour.replace("μμ", "pm");
            respondHour = respondHour.replace("πμ", "am");


        } catch (Exception e) {
           e.printStackTrace();

            try {
                String a = "https://www.timeanddate.com/worldclock/?query=";
                String[] infoArray = info.split(" ");
                String extraInfo = "";
                for (int i = 0; i < infoArray.length; i++) {
                    if (i < (infoArray.length - 1)) {
                        extraInfo += infoArray[i] + "+";
                    } else {
                        extraInfo += infoArray[i];
                    }
                }
                if (infoArray.length == 0) {
                    extraInfo = info;
                }
                a += extraInfo;

                URLConnection connection = new URL(a).openConnection();
                connection
                        .setRequestProperty("User-Agent",
                                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
                connection.connect();

                BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                        Charset.forName("UTF-8")));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }

                String[] respondArray = sb.toString().split("<span id=ct class=h1>");
                respondHour = respondArray[1].split("</span>")[0];
                respondHour = respondHour.replace("μμ", "pm");
                respondHour = respondHour.replace("πμ", "am");

            } catch (Exception ee) {
                ee.printStackTrace();
            }

        }
        return respondHour;
    }

    String getWeather(String info) {
        String output = null;
        info=info.toLowerCase();
        try {

            String a = "http://www.timeanddate.com/weather/?query=";
            String[] infoArray = info.split(" ");
            String extraInfo = "";
            for (int i = 0; i < infoArray.length; i++) {
                if (i < (infoArray.length - 1)) {
                    extraInfo += infoArray[i] + "-";
                } else {
                    extraInfo += infoArray[i];
                }
            }
            if (infoArray.length == 0) {
                extraInfo = info;
            }
            a += extraInfo;

            URLConnection connection = new URL(a).openConnection();
            connection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();

            BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                    Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }

            String[] list = sb.toString().split("<a href=\"");
            for (int i = 0; i < list.length; i++) {

                if (list[i].split("\"")[0].contains("/" + extraInfo) && list[i].split("\"")[0].contains("/weather")) {
                    String url = "http://www.timeanddate.com/" + list[i].split("\"")[0];
                    output = getMoreInfo(url, false);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    String getForcastWeather(String info) {
        String output = null;
        try {
        info=info.toLowerCase();
            String a = "http://www.timeanddate.com/weather/?query=";
            String[] infoArray = info.split(" ");
            String extraInfo = "";
            for (int i = 0; i < infoArray.length; i++) {
                if (i < (infoArray.length - 1)) {
                    extraInfo += infoArray[i] + "-";
                } else {
                    extraInfo += infoArray[i];
                }
            }
            if (infoArray.length == 0) {
                extraInfo = info;
            }
            a += extraInfo;

            URLConnection connection = new URL(a).openConnection();
            connection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();

            BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                    Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }

            String[] list = sb.toString().split("<a href=\"");
            for (int i = 0; i < list.length; i++) {

                if (list[i].split("\"")[0].contains("/" + extraInfo) && list[i].split("\"")[0].contains("/weather")) {
                    String url = "http://www.timeanddate.com/" + list[i].split("\"")[0];
                    output = getMoreInfo(url, true);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    String getMoreInfo(String url, boolean wantsForcast) {
        String output = null;
        try {

            URLConnection connection = new URL(url).openConnection();
            connection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();

            BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                    Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }

            if (wantsForcast) {
                String fps = sb.toString().split("Forecast: ")[1].split("&nbsp;°C<br>")[0];
                String forcastWeather = "Tomorrow the range of temperature will be from " + fps.split("/")[0] + " till " + fps.split("/")[1] + " celsius degrees.";
                //
                return forcastWeather;
            }

            String feelsLike = sb.toString().split("Feels Like:")[1].split("&nbsp;°C<br>")[0];
            feelsLike += " celsius degrees ";
            double bofour = -1;
            String[] windList = sb.toString().split("Wind:");
            for (int i = 0; i < windList.length; i++) {
                String windString = windList[i].split("km/h")[0];
                try {
                    bofour = (Integer.parseInt((sb.toString().split("Wind:")[1].split("km/h")[0]).replaceAll(" ", "")) / 3.6f);
                    break;
                } catch (Exception e) {

                }
            }
            bofour = Math.round(bofour * 100.0) / 100.0;
            String wind = "wind speed at " + bofour + " Beaufort, ";

try{
            String direction = sb.toString().split("Wind:")[1].split("title=\"")[1].split("\">")[0];
            wind += direction;
        }catch(Exception e){
        wind="";
        
        }
        
        
            String title = sb.toString().split("<img class=mtt title=\"")[1].split("\"")[0];
            String temp = sb.toString().split("<div class=h2>")[1].split("&nbsp;")[0];
            temp += "°C";

            String humidity = sb.toString().split("Humidity: </span>")[1].split("</p>")[0];
            String pressure = sb.toString().split("Pressure: </span>")[1].split("mbar")[0] + "mbar";
            String[] infosFromUrl = url.split("/");
            String city = infosFromUrl[infosFromUrl.length - 1];

            output = "in " + city + " we currently have \"" + title + "\" The temperature is " + temp + " and feels like " + feelsLike + " with " + wind + ". Humidity is " + humidity + " and pressure is at " + pressure;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

}