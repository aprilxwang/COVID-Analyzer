package edu.upenn.proj1.datamanagement;

import edu.upenn.proj1.util.CovidData;


import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public interface CovidDataReader {
    public List<CovidData> readCovidData() throws IOException;

    public static boolean verifyTimeStamp(String timeStamp) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(timeStamp);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

}
