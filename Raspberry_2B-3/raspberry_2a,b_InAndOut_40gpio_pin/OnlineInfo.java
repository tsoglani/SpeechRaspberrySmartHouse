

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class OnlineInfo
{
 
    public String getInfoFromPage(String info) {
        String output = "";
        try {
            String a = "https://www.evi.com/q/";
            String[] infoArray = info.split(" ");
            String extraInfo = "";
            for (int i = 0; i < infoArray.length; i++) {
                if (i < (infoArray.length - 1)) {
                    extraInfo += infoArray[i] + "_";
                } else {
                    extraInfo += infoArray[i];
                }
            }
            if (infoArray.length == 0) {
                extraInfo = info;
            }
            a += extraInfo;
System.out.println("url "+a);
//            String a = "https://www.evi.com/q/president_of_usa";
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

            String[] splitStringTxt = sb.toString().split("<div class='tk_text'>");

            for (int i = 1; i < splitStringTxt.length; i++) {
                try {
                    String using = splitStringTxt[i];
                    using = using.split("</div>")[0];
                    int start = getStartingInt(using);
                    if (start > 0) {
                        using = using.substring(start, using.length());
                    }
                    using = using.replaceAll("  ", "");
                    output += using;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            String[] splitString = sb.toString().split("<div class=\"tk_common\">");
//            System.out.println(sb.toString());

            for (int i = 1; i < splitString.length; i++) {
                try {
                    String using = splitString[i];
                    using = using.split("</div>")[0];
                    int start = getStartingInt(using);
                    if (start > 0) {
                        using = using.substring(start, using.length());
                    }
                    using = using.replaceAll("  ", "");
                    if (info.replaceAll(" ", "").equalsIgnoreCase(using.replaceAll(" ", ""))) {
                        continue;
                    }
                    output += using;

                } catch (Exception ex) {
                    ex.printStackTrace();
                    output = "Sorry, I don't have an answer to that question.";
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            output = "Sorry, I don't have an answer to that question.";
        }
        System.out.println(output);
if(output.length()<=1){
                    output = "Sorry, I don't have an answer to that question.";
}
        return output;
    }

    private int getStartingInt(String s) {
        int returning_number = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (Character.isAlphabetic(c)) {
                break;
            } else {
                returning_number = i;
            }
        }
        return returning_number;
    }

}
