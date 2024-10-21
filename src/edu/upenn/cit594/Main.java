package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.CovidDataReader;
import edu.upenn.cit594.datamanagement.PopulationDataReader;
import edu.upenn.cit594.datamanagement.PropertyDataReader;
import edu.upenn.cit594.datamanagement.CSVReader;
import edu.upenn.cit594.datamanagement.JSONReader;
import edu.upenn.cit594.processor.Processor;
import edu.upenn.cit594.ui.UserInterface;
import edu.upenn.cit594.logging.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        // Check if arguments are provided correctly
        if (args.length < 2 || args.length > 4) {
            System.err.println("Usage: java Main [--population=<population_file>] [--covid=<covid_file>] " +
                    "[--properties=<properties_file>] [--log=<log_file>]");
            return;
        }

        // Validate the format of runtime arguments
        Pattern pattern = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$");
        Set<String> argNames = new HashSet<>();
        for (String arg : args) {
            Matcher matcher = pattern.matcher(arg);
            if (!matcher.matches()) {
                System.err.println("Invalid argument format: " + arg);
                return;
            }
            String name = matcher.group("name");
            if (!argNames.add(name)) {
                System.err.println("Argument '" + name + "' used more than once.");
                return;
            }
        }

        // Get the log file name from the arguments
        String logFileName = getValueForArgument(args, "log");
        if (logFileName == null) {
            System.err.println("Error: Log file name not provided.");
            // create a logFileName
            logFileName = "default_log";
            System.out.println("Using default log file name: " + logFileName);
            // return;
        }

        // Initialize the logger
        Logger logger = Logger.getInstance();
        try {
            logger.setDestination(logFileName);
        } catch (IOException e) {
            System.err.println("Error initializing logger: " + e.getMessage());
            return;
        }

        // Declare the data readers outside the try block for proper resource management
        CovidDataReader covidReader = null;
        PopulationDataReader populationReader = null;
        PropertyDataReader propertyReader = null;

        // Initialize readers based on argNames
        if (argNames.contains("covid")) {
            String covidFileName = getValueForArgument(args, "covid");
            if (covidFileName.toLowerCase().endsWith(".json")) {
                covidReader = new JSONReader(covidFileName);
            } else if (covidFileName.toLowerCase().endsWith(".csv")) {
                covidReader = new CSVReader(covidFileName);
            } else {
                try {
                    logger.log("Invalid covid_data file format. Please provide a JSON or csv file.");
                } catch (IOException ex) {
                    System.err.println("Error logging: " + ex.getMessage());
                }
                //return;
            }
        } else {
            try {
                logger.log("Missing covid argument.");
            } catch (IOException ex) {
                System.err.println("Error logging: " + ex.getMessage());
            }
            //return;
        }

        if (argNames.contains("population")) {
            String populationFileName = getValueForArgument(args, "population");
            populationReader = new CSVReader(populationFileName);
        } else {
            try {
                logger.log("Missing population argument.");
            } catch (IOException ex) {
                System.err.println("Error logging: " + ex.getMessage());
            }
            //return;
        }

        if (argNames.contains("properties")) {
            String propertiesFileName = getValueForArgument(args, "properties");
            propertyReader = new CSVReader(propertiesFileName);
        } else {
            try {
                logger.log("Missing properties argument.");
            } catch (IOException ex) {
                System.err.println("Error logging: " + ex.getMessage());
            }
            //return;
        }

        // Instantiate processor with data readers
        Processor processor = new Processor(covidReader, populationReader, propertyReader);

        // Instantiate UI with processor and start the program
        UserInterface ui = new UserInterface(processor);
        try {
            ui.start(args);
        } catch (Exception e) {
            try {
                logger.log("Error starting UI: " + e.getMessage());
            } catch (IOException ex) {
                System.err.println("Error logging: " + ex.getMessage());
            }
        }
    }

    private static String getValueForArgument(String[] args, String argumentName) {
        for (String arg : args) {
            if (arg.startsWith("--" + argumentName + "=")) {
                return arg.substring(argumentName.length() + 3);
            }
        }
        return null;
    }

}
