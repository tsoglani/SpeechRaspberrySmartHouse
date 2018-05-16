
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tsoglani
 */
public class Dictionarry {

    public static void main(String[] args) {
        String str = new Dictionarry().getBestDefinition("greenhouse");
        System.out.println(str);
    }

    public String getBestDefinition(String info) {
        String output = getDic2(info);
        if (output == null || output.replaceAll(" ", "").equals("")) {
            output = getDictionaryByWord2(info);
            if (output == null || output.replaceAll(" ", "").equals("")) {
                output = getDic(info);
                if (output == null || output.replaceAll(" ", "").equals("")) {
                    output = getDictionaryByWord(info);

                }
            }
        }

        return removeUrl(output);
    }

    //
    private String getDictionaryByWord2(String info) {
        String output = "";
        try {

            String a = "http://www.yourdictionary.com/";
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

            String allData = sb.toString();
//            System.out.println(allData);
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 1; i < allData.split("<meta name=\"description\" content=\"" + extraInfo + " definition:").length; i++) {
                String result = allData.split("<meta name=\"description\" content=\"" + extraInfo + " definition:")[i].split("...\" />")[0];
                list.add(result);
            }
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    if (list.get(i).replaceAll(" ", "").length() >1) {
                        output += extraInfo.replaceAll("-", " ") + " definition: ";

                    }
                }
                output += list.get(i);

            }

        } catch (Exception e) {
//            e.printStackTrace();
        }

        return output;

    }

    private String getDic(String info) {//
        String output = "";
        try {

            String a = "http://www.merriam-webster.com/dictionary/suggestions/";
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

            String allData = sb.toString();
//            System.out.println(allData);
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 1; i < allData.split("<span class=\"intro-colon\">:</span>").length; i++) {
                String result = allData.split("<span class=\"intro-colon\">:</span>")[i].split("<")[0];
                list.add(result);
            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).contains("a href")) {
                    continue;
                }

                if (i == 0) {
                    output += info + " is";
                    output += " " + list.get(i);
                } else {
                    output += " " + list.get(i) + " ";

                }

                break;
            }

        } catch (Exception e) {
//            e.printStackTrace();
        }

        return output;
    }

    private String getDic2(String info) {//
        String output = "";
        try {

            String a = "http://google-dictionary.so8848.com/meaning?word=";
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

            String allData = sb.toString();
//            System.out.println(allData);
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 1; i < allData.split("<li style=\"list-style:decimal\">").length; i++) {
                String result = allData.split("<li style=\"list-style:decimal\">")[i].split("<")[0];
                list.add(result);
            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).contains("a href")) {
                    continue;
                }

                if (i == 0) {
                    output += info + " is";
                    output += " " + list.get(i);
                } else {

                    if (i >= list.size() - 1 && list.size() > 2) {
                        break;
                    } else {

                    }
                    output += " " + list.get(i) + " ";

                }
                output += ".";

                //  break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    private String getDictionaryByWord(String info) {
        String output = "";
        try {

            String a = "http://dictionary.reverso.net/english-definition/";
            String[] infoArray = info.split(" ");
            String extraInfo = "";
            for (int i = 0; i < infoArray.length; i++) {
                if (i < (infoArray.length - 1)) {
                    extraInfo += infoArray[i] + "%20";
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

            String allData = sb.toString();
//            System.out.println(allData);
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 1; i < allData.split("direction=\"target\">").length; i++) {
                String result = allData.split("direction=\"target\">")[i].split("<")[0];
                list.add(result);
            }
            for (int i = 0; i < list.size(); i++) {
                
                    output += "Definition of " + info + ":\n";
                    output +=  " " + list.get(i);
               
                    break;
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;

    }

    private String removeUrl(String commentstr) {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            commentstr = commentstr.replaceAll(m.group(i), "").trim();
            i++;
        }
        return commentstr;
    }
}