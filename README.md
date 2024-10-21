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
  - `edu.upenn.cit594` - should contain `Main.java`
  - `edu.upenn.cit594.datamanagement`
  - `edu.upenn.cit594.logging` 
  - `edu.upenn.cit594.processor`
  - `edu.upenn.cit594.ui`
  - `edu.upenn.cit594.util`
- Design Patterns
  - Logger should be a singleton
  - Computing average market value and average total livable area of properties should be done using the Strategy pattern
- Efficiency
  - Program should use memoization

## Things to discuss
- [X] Determine data types 
- [X] Determine method for error handling
- [X] Determine custom feature
- [X] Communication tools (work process)
  - Slack
  - Zoom meetings
- [X] Code version control methods
  - Github private repo
- Meetings
  - [X] Wed 4/10 10PM
  - [X] Thur 4/11 8PM
  - [X] Sun 4/14 8PM
  - [X] Mon 4/15 10PM
  - [X] Wed 4/17 10PM
  - [ ] Tue 4/23 8PM

## April 7, 2024 Meeting Notes

- **Action Items for next meeting:**
  - Read the group project
  - Briana to set up github repo
- **Things to get done during next meeting:**
  - Discuss responsibilities
  - Discuss data types and error handling method
  - Discuss custom feature
 
## April 10, 2024 Meeting Notes

- **Roles & Responsibilities**
  - datamanagement & util - Briana
  - processor & logging - Xinhu
  - ui & main - Mark
- **Exception Handling**
  - try/catch in Main, throw exceptions in class methods
- **Action Items for next meeting:**
  - Thinking about signature lines (parameters & return data types) of your methods

## April 11, 2024 Meeting Notes
- testing - test individual classes separately, then system testing once all classes are written
- went over basic structure of datamanagement, util, and processor.
- **Action items for next meeting**
  - Think about ideas for custom feature
 
## April 14, 2024 Meeting Notes
- **Custom feature**: display average market value and vaccination rate per zipcode
  - Sample output: `Zipcode: 19100, Average Market Value: $123,456, Vaccination Rate: 0.12`
  - Print one line per zipcode to the console for all zipcodes
  - Vaccination rate is calculated by: $$\frac{(Partial Vaccinations + Full Vaccinations)}{Total Population}$$
  - Partial and full vaccination numbers should be gotten from the latest COVID data timepoint for that zipcode
- Save timestamps in "YYYY-MM-DD" format

## April 15, 2024 Meeting Notes
- After merging our work to the main branch, each of us can start testing the whole program
- Xinhu: focus on debugging the calculations for Action 3-6
- Mark: keep developing UI (mainly for Action 2 and taking various arguments)

## April 17, 2024 Meeting Notes
- Updated CovidDataReader to handle empty and invalid cells properly (Double.NaN)
- Finished implementing custom features
- Started working on write-up PDF - Briana

## April 19, 2024 Meeting Notes
- Debug and code review session
- Finalized the output format in UI and passed all BasicTests
- Tested with other provided files (downsampled_properties.csv)
- Need to fill in signoff.txt -> changed name to Team-010.txt as instructed
