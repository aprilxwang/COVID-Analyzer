package edu.upenn.proj1.ui;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import edu.upenn.cit594.processor.Processor;

public class UserInterface {
    private final Processor processor;
    private final Scanner scanner;

    public UserInterface(Processor processor) {
        this.processor = processor;
        this.scanner = new Scanner(System.in);
    }

    public void start(String[] args) throws ParseException {
        System.out.println("Welcome to the program!");
        displayMenu();
        int choice;
        while (true) {
            System.out.print("> ");
            System.out.flush(); // Ensure the prompt appears
            while (!scanner.hasNextInt()) {
                scanner.nextLine(); // Consume invalid input
                System.out.println("Invalid input. Please enter a number between 0 and 7.");
                System.out.print("> ");
                System.out.flush(); // Ensure the prompt appears
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice >= 0 && choice <= 7) {
                break;
            } else {
                System.out.println("Invalid input. Please enter a number between 0 and 7.");
            }
        }

        // Determine the number of available actions based on the arguments present
        int availableActions = 0;
        boolean hasPopulation = false;
        boolean hasCovid = false;
        boolean hasProperties = false;

        // Check which arguments are present
        for (String arg : args) {
            if (arg.startsWith("--population=")) {
                hasPopulation = true;
            } else if (arg.startsWith("--covid=")) {
                hasCovid = true;
            } else if (arg.startsWith("--properties=")) {
                hasProperties = true;
            }
        }

        if (hasPopulation) {
            availableActions = 2; // Include actions 0, 1, 2
        }
        if (hasCovid) {
            availableActions = 3; // Include action 3
        }
        if (hasProperties) {
            availableActions = 7; // Include actions 4, 5, 6, 7
        }

        while (choice != 0) {
            if (choice > availableActions) {
                System.out.println("Invalid input. Please enter a number between 0 and " + availableActions);
            } else {
                switch (choice) {
                    case 1:
                        displayAvailableActions(args);
                        break;
                    case 2:
                    	System.out.println("");
                        System.out.println("BEGIN OUTPUT");
                        // System.out.println("Total population for all ZIP Codes: " + processor.getTotalPopulation());
                        System.out.println(processor.getTotalPopulation());
                        System.out.println("END OUTPUT");
                        break;
                    case 3:
                        showTotalVaccinationsPerCapita();
                        break;
                    case 4:
                        if (availableActions >= 4) {
                            showAverageMarketValue();
                        } else {
                            System.out.println("Invalid input. Please enter a number between 0 and " + availableActions);
                        }
                        break;
                    case 5:
                        if (availableActions >= 5) {
                            showAverageTotalLivableArea();
                        } else {
                            System.out.println("Invalid input. Please enter a number between 0 and " + availableActions);
                        }
                        break;
                    case 6:
                        if (availableActions >= 6) {
                            showTotalMarketValuePerCapita();
                        } else {
                            System.out.println("Invalid input. Please enter a number between 0 and " + availableActions);
                        }
                        break;
                    case 7:
                        if (availableActions >= 7) {
                            showCustomFeatureResults();
                        } else {
                            System.out.println("Invalid input. Please enter a number between 0 and " + availableActions);
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
            displayMenu();
            choice = getUserChoice();
        }
        System.out.println("Exiting the program. Goodbye!");
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("Choose an action:");
        System.out.println("0. Exit the program.");
        System.out.println("1. Show the available actions.");
        System.out.println("2. Show the total population for all ZIP Codes.");
        System.out.println("3. Show the total vaccinations per capita for each ZIP Code for the specified date.");
        System.out.println("4. Show the average market value for properties in a specified ZIP Code.");
        System.out.println("5. Show the average total livable area for properties in a specified ZIP Code.");
        System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP Code.");
        System.out.println("7. Show the results of your custom feature.");
    }

    private void displayAvailableActions(String[] args) {
        boolean hasPopulation = false;
        boolean hasCovid = false;
        boolean hasProperties = false;

        // Check which arguments are present
        for (String arg : args) {
            if (arg.startsWith("--population=")) {
                hasPopulation = true;
            } else if (arg.startsWith("--covid=")) {
                hasCovid = true;
            } else if (arg.startsWith("--properties=")) {
                hasProperties = true;
            }
        }

        // Determine the number of available actions based on the arguments present
        int availableActions = 0;
        if (hasPopulation) {
            availableActions = 2; // Include actions 0, 1, 2
        }
        if (hasCovid) {
            availableActions = 3; // Include action 3
        }
        if (hasProperties) {
            availableActions = 7; // Include actions 4, 5, 6, 7
        }

        // Display the available actions
        System.out.println("");
        System.out.println("BEGIN OUTPUT");
        for (int i = 0; i <= availableActions; i++) {
            System.out.println(i);
        }
        System.out.println("END OUTPUT");
    }


    private int getUserChoice() {
        int choice;
        System.out.print("> ");
        System.out.flush(); // Ensure the prompt appears
        while (!scanner.hasNextInt()) {
            scanner.nextLine(); // Consume invalid input
            System.out.println("Invalid input. Please enter a number between 0 and 7.");
            System.out.print("> ");
            System.out.flush(); // Ensure the prompt appears
        }
        choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return choice;
    }

    private void showTotalVaccinationsPerCapita() {
        System.out.print("> Enter 'full' or 'partial' for full or partial vaccinations: ");
        System.out.flush(); // Ensure the prompt appears
        String vaccOption = scanner.nextLine();

        System.out.print("> Enter the date in YYYY-MM-DD format: ");
        System.out.flush(); // Ensure the prompt appears
        String inputDate = scanner.nextLine();

        System.out.println("");
        System.out.println("BEGIN OUTPUT");

        TreeMap<Integer, Double> vaccinationsPerCapita = processor.getVaccPerCapita(vaccOption, inputDate);

        for (Map.Entry<Integer, Double> entry : vaccinationsPerCapita.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        System.out.println("END OUTPUT");
    }

    private void showAverageMarketValue() {
        System.out.print("> Enter a ZIP Code: ");
        System.out.flush(); // Ensure the prompt appears
        int inputZip = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("");
        System.out.println("BEGIN OUTPUT");
        //System.out.println("Average market value: $" + processor.getAvgMarketValue(inputZip));
        System.out.println(processor.getAvgMarketValue(inputZip));
        System.out.println("END OUTPUT");
    }

    private void showAverageTotalLivableArea() {
        System.out.print("> Enter a ZIP Code: ");
        System.out.flush(); // Ensure the prompt appears
        int inputZip = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("");
        System.out.println("BEGIN OUTPUT");
        // System.out.println("Average total livable area: " + processor.getAvgTotLivableArea(inputZip) + " sq.ft.");
        System.out.println(processor.getAvgTotLivableArea(inputZip));
        System.out.println("END OUTPUT");
    }

    private void showTotalMarketValuePerCapita() {
        System.out.print("> Enter a ZIP Code: ");
        System.out.flush(); // Ensure the prompt appears
        int inputZip = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("");
        System.out.println("BEGIN OUTPUT");
        //System.out.println("Total market value per capita: $" + processor.getTotMarketValuePC(inputZip));
        System.out.println(processor.getTotMarketValuePC(inputZip));
        System.out.println("END OUTPUT");
    }

    private void showCustomFeatureResults() throws ParseException {
    	System.out.println("");
    	System.out.println("BEGIN OUTPUT");
    	System.out.println("Custom feature results:");

    	// Print header for the table
    	System.out.printf("%-15s %-25s %-20s%n", "Zip Code:", "Avg Market Value:", "Vaccination Rate:");

    	TreeMap<Integer, List<Double>> result = processor.getVaccRates();
    	if (result != null) {
    	    for (Map.Entry<Integer, List<Double>> entry : result.entrySet()) {
    	        Integer zipCode = entry.getKey();
    	        List<Double> data = entry.getValue();
    	        Double avgMarketValue = data.get(0);
    	        Double vaccinationRate = data.get(1);

    	        // Print each row of the table
    	        System.out.printf("%-15s %-25s %-20s%n", zipCode, avgMarketValue, vaccinationRate);
    	    }
    	} else {
    	    System.out.println("No data available.");
    	}

    	System.out.println("END OUTPUT");
    }
}
