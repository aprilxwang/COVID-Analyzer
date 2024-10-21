## Data

### COVID Data

- Data file types: CSV and JSON
- Fields (Bolded fields used in the program. Others can be used in the custom feature):
  - ***Zip Code where the vaccinations were provided***
  - ***timestamp (YYYY-MM-DD hh:mm:ss) at which the vaccination data was reported***
  - ***Number of partially vaccinated people (people who received their first dose)***
  - ***Number of fully vaccinated people (people who received their second dose)***
  - Total number of COVID infection tests
  - Total number of booster doses administered
  - Total number of COVID patients hospitalized
  - Total number of deaths attributed to the disease to date
- Considerations
  - all numbers are cumulative except for partial and full vaccinated numbers
  - should ignore records where ZIP Code is not 5 digits or the timestamp is not in the specified format. Any other empty fields should be interpreted as being 0.
  - Two warnings: First, when a person who is “partially vaccinated” receives their second dose, they are removed from that count and added to the “fully vaccinated” count, which may result in overall decreases in the “partially vaccinated” count. Second, the reporting agencies may have made occasional data corrections or errors which result in one of the other cumulative fields temporarily decreasing in value.
  - We can borrow codes from homework 7 for CSV reading and parsing.

### Properties Data

- Data file type: CSV
- Fields:
  - `market_value`
  - `total_livable_area`
  - `zip_code`
- Considerations
  - the `zip_code` field may use extended form of ZIP Code. Our analysis should use only the first 5 characters.
  - any ZIP Codes that have fewer than 5 characters or non-numeric characters should be ignored
  - suggestions: use `sale_price` from this data set for the free-form activity? we can compare it with `market_value` and see the impact of COVID cases on property values.

### Population Data

- Data file type: CSV
- Fields:
  - `zip_code`
  - `population`
- Considerations
  - ignore any records where ZIP Code is not exactly 5 digits or population figure is not an integer

## Functional Specs

- Four runtime arguments in the form of `--name=value` (regex “`^--(?<name>.+?)=(?<value>.+)$`”
  - `covid`: The name of the COVID data file
  - `properties`: The name of the property values file
  - `population`: The name of the population data file
  - `log`: The name of the log file
- Program should be able to display:
  - Show the available actions
  - Show the total population for all ZIP Codes
  - Show the total vaccinations per capita for each ZIP Code for the specified date
  - Show the average market value for properties in a specified ZIP Code
  - Show the average total livable area for properties in a specified ZIP Code
  - Show the total market value of properties, per capita, for a specified ZIp Code
  - Show the results of the custom feature (must involve all three data sets)

## Design Specs
- Program should use N-tier architecture
- Organization:
  - `edu.upenn.proj1` - should contain `Main.java`
  - `edu.upenn.proj1.datamanagement`
  - `edu.upenn.proj1.logging` 
  - `edu.upenn.proj1.processor`
  - `edu.upenn.proj1.ui`
  - `edu.upenn.proj1.util`
- Design Patterns
  - Logger should be a singleton
  - Computing average market value and average total livable area of properties should be done using the Strategy pattern
- Efficiency
  - Program should use memoization
