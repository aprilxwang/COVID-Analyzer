package edu.upenn.proj1.datamanagement;

import edu.upenn.proj1.util.CovidData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONReader extends Reader implements CovidDataReader{

    public JSONReader(String filename) throws IOException {
        super(filename);
    }

    @Override
    public List<CovidData> readCovidData() throws IOException {
        JSONParser parser = new JSONParser();
        List<CovidData> data = new ArrayList<>();
        try {
            JSONArray jsonArray = (JSONArray) parser.parse(this.reader);
            for (Object obj : jsonArray) {
                int zipCode;
                int partialVaccination;
                int fullVaccination;
                String timeStamp;
                try {
                    String zipCodeString = String.valueOf((Long) ((JSONObject) obj).get("zip_code"));
                    if (zipCodeString.length() != 5) {
                        continue;
                    }
                    zipCode = Integer.parseInt(zipCodeString);
                } catch (NullPointerException e) {
                    continue;
                }
                try {
                    long partialVaccinationLong = (Long) ((JSONObject) obj).get("partially_vaccinated");
                    partialVaccination = Long.valueOf(partialVaccinationLong).intValue();
                } catch (NullPointerException e) {
                    partialVaccination = 0;
                }

                try {
                    long fullVaccinationLong = (Long) ((JSONObject) obj).get("fully_vaccinated");
                    fullVaccination = Long.valueOf(fullVaccinationLong).intValue();
                } catch (NullPointerException e) {
                    fullVaccination = 0;
                }
                timeStamp = ((String) ((JSONObject) obj).get("etl_timestamp"));

                try {
                    timeStamp = timeStamp.substring(0, 10);
                    if (!CovidDataReader.verifyTimeStamp(timeStamp)) {
                        continue;
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    continue;
                }

                CovidData covidData = new CovidData(zipCode, partialVaccination, fullVaccination, timeStamp);
                data.add(covidData);
            }
        } catch (ParseException e) {
            return null;
        }
        this.reader.close();
        return data;
    }




    public static void main(String[] args) throws IOException {
        int count = 0;
        CovidDataReader csvReader = new JSONReader("CIT594-Group-Project/covid_data.json");
        List<CovidData> covidData = csvReader.readCovidData();
        for (CovidData data : covidData) {
            System.out.println(data.getZipCode() + " " + data.getPartialVaccination() + " " + data.getFullVaccination() + " " + data.getTimeStamp());
            count++;

        }
        System.out.println(count);

    }
}
