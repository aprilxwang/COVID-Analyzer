package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.Property;
import edu.upenn.cit594.util.Population;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;


public class CSVReader extends Reader implements CovidDataReader, PopulationDataReader, PropertyDataReader {
    CharacterReader cr;

    private enum State {
        NORMAL,
        ESCAPED,
        DOUBLE_QUOTED,
        CARRIAGERETURN,
        EXCEPTION,
    }
    public CSVReader(String filename) throws IOException {
        super(filename);
        BufferedReader br = new BufferedReader(this.reader);
        this.cr = new CharacterReader(br);
    }

    @Override
    public List<CovidData> readCovidData() throws IOException {
        List<CovidData> data = new ArrayList<>();
        String [] row;
        // read each row of csv file
        while ((row = readRow()) != null) {
            int zipCode;
            int partialVaccination;
            int fullVaccination;
            String timeStamp;

            // check for 5 digit ZipCode
            if (row[0].length() != 5){
                continue;
            };
            // convert Zip Code to integer
            try {
                zipCode = Integer.parseInt(row[0]);
            } catch (NumberFormatException e) {
                continue;
            }
            // convert Partial Vaccination to integer
            try {
                if (row[5].isEmpty()) {
                    partialVaccination = 0;
                } else {
                    partialVaccination = Integer.parseInt(row[5]);
                }
            } catch (NumberFormatException e) {
                continue;
            }

            // convert Full Vaccination to integer
            try {
                if (row[6].isEmpty()) {
                    fullVaccination = 0;
                } else {
                    fullVaccination = Integer.parseInt(row[6]);
                }
            } catch (NumberFormatException e) {
                continue;
            }

            // get Time Stamp (only YYYY-MM-DD)

            try {
                timeStamp = row[8].substring(0, 10);
//                 check if Time Stamp is in correct format. If not, skip this row
                if (!CovidDataReader.verifyTimeStamp(timeStamp)) {
                    continue;
                }
            } catch (StringIndexOutOfBoundsException e) {
                continue;
            }


            data.add(new CovidData(zipCode, partialVaccination, fullVaccination, timeStamp));
        }
        return data;
    }

    @Override
    public List<Population> readPopulationData() throws IOException {
        List<Population> data = new ArrayList<>();
        String[] row;

        while ((row = readRow()) != null) {
            int zipCode;
            int population;
            // check for 5 digit ZipCode
            if (row[0].length() != 5) continue;
            // convert Zip Code to integer
            try {
                zipCode = Integer.parseInt(row[0]);
            } catch (NumberFormatException e) {
                continue;
            }
            // convert Population to integer
            try {
                population = Integer.parseInt(row[1]);
            } catch (NumberFormatException e) {
                continue;
            }
            data.add(new Population(zipCode, population));
        }
        return data;
    }

    @Override
    public List<Property> readPropertyData() throws IOException {
        List<Property> data = new ArrayList<>();
        String[] row;

        // Map to store the column indices
        Map<String, Integer> columnIndexMap = new HashMap<>();

        // Read the header row
        String[] headers = readRow();
        if (headers == null) {
            // Handle case where no header row is found
            return data;
        }

        // Populate the column index map
        for (int i = 0; i < headers.length; i++) {
            columnIndexMap.put(headers[i].toLowerCase().trim(), i);
        }

        // Check if required columns exist in the header
        if (!columnIndexMap.containsKey("zip_code") || !columnIndexMap.containsKey("market_value") ||
                !columnIndexMap.containsKey("total_livable_area")) {
            // Handle case where required columns are missing
            return data;
        }

        // Process the data rows
        while ((row = readRow()) != null) {
            // Extract data using column indices from the map
            String zipCodeStr = row[columnIndexMap.get("zip_code")];
            String marketValueStr = row[columnIndexMap.get("market_value")];
            String livableAreaStr = row[columnIndexMap.get("total_livable_area")];

            // Validate and convert data types
            int zipCode;
            double marketValue;
            double totalLivableArea;

            try {
                zipCode = Integer.parseInt(zipCodeStr.substring(0, 5));
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                continue; // Skip invalid data
            }

            try {
                marketValue = Double.parseDouble(marketValueStr);
            } catch (NumberFormatException e) {
                marketValue = Double.NaN;
            }

            try {
                totalLivableArea = Double.parseDouble(livableAreaStr);
            } catch (NumberFormatException e) {
                totalLivableArea = Double.NaN;
            }

            // Add validated data to the list
            data.add(new Property(zipCode, marketValue, totalLivableArea));
        }

        return data;
    }

    public static void main(String[] args) throws IOException {
//        int count = 0;
//        CSVReader csvReader = new CSVReader("CIT594-Group-Project/covid_data.csv");
//        List<CovidData> covidData = csvReader.readCovidData();
//        for (CovidData data : covidData) {
//            System.out.println(data.getZipCode() + " " + data.getPartialVaccination() + " " + data.getFullVaccination() + " " + data.getTimeStamp());
//            count++;
//
//        }
        CSVReader csvReader = new CSVReader("CIT594-Group-Project/properties_test.csv");
        List<Property> propertyData = csvReader.readPropertyData();
        System.out.println(propertyData.size());
        int count = 0;
        for (Property p: propertyData) {
            System.out.println(p.getZipCode() + " " + p.getMarketValue() + " " + p.getTotalLivableArea());

        }


    }

    // private helper method

    private String[] readRow() throws IOException {
        Queue<String> row = new ArrayDeque<>();
        StringBuilder field = new StringBuilder();
        State state = State.NORMAL;

        int ch;

        while ((ch = cr.read()) != -1) {
            char c = (char) ch;
            switch (state) {
                case NORMAL:
                    switch (c) {
                        case ',':
                            row.add(field.toString());
                            field.setLength(0);
                            break;
                        case '"':
                            if (field.isEmpty()) {
                                state = State.ESCAPED;
                            } else {
                                state = State.EXCEPTION;
                            }
                            break;
                        case '\n':
                            row.add(field.toString());
                            return row.toArray(new String[0]);
                        case '\r':
                            state = State.CARRIAGERETURN;
                            break;
                        default:
                            field.append(c);
                    }
                    break;
                case ESCAPED:
                    if (c == '"') {
                        state = State.DOUBLE_QUOTED;
                    }
                    else {
                        field.append(c);
                    }
                    break;
                case DOUBLE_QUOTED:
                    switch (c) {
                        case ',':
                            row.add(field.toString());
                            field.setLength(0);
                            state = State.NORMAL;
                            break;
                        case '"':
                            field.append(c);
                            state = State.ESCAPED;
                            break;
                        case '\n':
                            row.add(field.toString());
                            return row.toArray(new String[0]);
                        case '\r':
                            state = State.CARRIAGERETURN;
                            break;
                        default:
                            state = State.EXCEPTION;
                    }
                    break;
                case CARRIAGERETURN:
                    if (c == '\n') {
                        row.add(field.toString());
                        return row.toArray(new String[0]);
                    } else {
                        throw new IOException();
                    }
                case EXCEPTION:
                    if (c == '\n') {
                        throw new IOException();
                    }
            }
        }
        if (field.length() > 0) {
            row.add(field.toString());
        }
        if (state == State.EXCEPTION || state == State.CARRIAGERETURN) {
            throw new IOException();
        }
        return row.isEmpty() ? null : row.toArray(new String[0]);
    }


}

