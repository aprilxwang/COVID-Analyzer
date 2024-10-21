package edu.upenn.proj1.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;

import edu.upenn.proj1.datamanagement.*;
import edu.upenn.proj1.util.Population;
import edu.upenn.proj1.util.CovidData;
import edu.upenn.proj1.util.Property;





public class Processor {
	// data readers
	private CovidDataReader covidDataReader;
	private PopulationDataReader populationReader;
	private PropertyDataReader propertyReader;
	// data
	private List<CovidData> covidData;
	private List<Property> propertyData;
	private List<Population> populationData;
	// result holders for each action
	private Long totalPopulationResult;
	private HashMap<String, TreeMap<Integer, Double>> partialVaccResult;
    	private HashMap<String, TreeMap<Integer, Double>> fullVaccResult;
	private Map<Integer, Integer> populationMap;
    private TreeMap<Integer, Long> avgMarketValueResult;
    private TreeMap<Integer, Long> avgTotalLivableAreaResult;
    private Map<Integer, Long> totMarketValuePCResult;
    private Map<Integer, Double> vaccRatesPerZip;
    private TreeMap<Integer, List<Double>> vaccRatesResult;

	
	
	// constructor
	public Processor(CovidDataReader covidReader, PopulationDataReader populationReader,
			PropertyDataReader propertyReader) throws Exception {
		this.covidDataReader = covidReader;
		this.populationReader = populationReader;
		this.propertyReader = propertyReader;
		if(covidReader != null) 
			covidData = covidReader.readCovidData();
		if(populationReader != null) 
			populationData = populationReader.readPopulationData();
		if(propertyReader != null) 
			propertyData = propertyReader.readPropertyData();
		partialVaccResult = new HashMap<>();
		fullVaccResult = new HashMap<>();
		avgMarketValueResult = new TreeMap<>();
		avgTotalLivableAreaResult = new TreeMap<>();
		totMarketValuePCResult = new HashMap<>();
		
		
	}
	
	
	/*
	 * Action 2: Calculate total population of all ZIP codes, implement memoization
	 */
	public Long getTotalPopulation() {
		if(totalPopulationResult != null) {
			return totalPopulationResult;
		} else {
			long result = calculateTotalPopulation();
			totalPopulationResult = result;
			return result;
		}	
	}
	
	// original code for calculation
	private long calculateTotalPopulation() {
		long totalPop = 0;
		for(Population oneZipCodePop : populationData) {
			totalPop += oneZipCodePop.getPopulation();
		}
		return totalPop;
	}
	
	
	/*
	 * Helper method to get populationMap
	 */
	public Map<Integer, Integer> getPopulationMap(){
		populationMap = new HashMap<>();
		for(Population popData: populationData) {
			if(popData.getPopulation() == 0) 
				continue;
			populationMap.put(popData.getZipCode(), popData.getPopulation());
		}
		return populationMap;
	}
	
	/*
	 * Action 3: get total partial or full vaccinations per capita, implement memoization
	 * We choose HashMap over other maps because ...
	 * @param vaccOption: choose between "full" or "partial"
	 * @param inputDate: YYYY-MM-DD format
	 */
	public TreeMap<Integer, Double> getVaccPerCapita(String vaccOption, String inputDate){
		if(covidData == null || populationData == null) 
			return null;

		TreeMap<Integer, Double> vaccinationPC = new TreeMap<>();
		
		if (populationMap == null) {
			populationMap  = getPopulationMap();
		}
	
		if(vaccOption.equals("full")) {
			if(fullVaccResult.containsKey(inputDate)) {
				return fullVaccResult.get(inputDate);
			}
			
			for(CovidData c : covidData) {
				String Date = c.getTimeStamp();
				int fullyVacc = c.getFullVaccination();
				int zipCode = c.getZipCode();
				if(Date.equals(inputDate) && populationMap.containsKey(zipCode)) {
					double fullVaccRate = (double) fullyVacc/populationMap.get(zipCode); 
					vaccinationPC.put(zipCode, fullVaccRate);
					fullVaccResult.put(inputDate, vaccinationPC);
				}
			}
		} else if(vaccOption.equals("partial")){
			if(partialVaccResult.containsKey(inputDate)) {
				return partialVaccResult.get(inputDate);
			}
			for(CovidData c : covidData) {
				int partialVacc = c.getPartialVaccination();
				int zipCode = c.getZipCode();
				String Date = c.getTimeStamp();
				if(Date.equals(inputDate) && populationMap.containsKey(zipCode)) {
					double partialVaccRate = (double) partialVacc/populationMap.get(zipCode); 
					vaccinationPC.put(zipCode, partialVaccRate);
					partialVaccResult.put(inputDate, vaccinationPC);
				}
			}
		}
		
		return vaccinationPC;
	}
	
	
	/*
	 * Action 4: get Average Market Value, implement memoization and strategy pattern
	 * @param inputZip
	 */
	public Long getAvgMarketValue(int inputZip) {

		return calcAvgValue(avgMarketValueResult, inputZip, new avgMVCalculator());
		
	}
	
	
	/*
	 * Action 5: get average total livable area, implement strategy pattern
	 * @param inputZip
	 */
	public Long getAvgTotLivableArea(int inputZip) {

		return calcAvgValue(avgTotalLivableAreaResult, inputZip, new avgTLACalculator());
		
	}
	
	/*
	 * Helper method to implement strategy pattern
	 * @param resultsMap - either avgMarketValueResult or avgTotalLivableAreaResult
	 * @param calcType - either avgMVCalculator or avgTLACalculator
	 */
	private Long calcAvgValue(TreeMap<Integer, Long> resultsMap, int inputZip, avgCalculator calcType) {
		// memoization - check existing map
		if(resultsMap.containsKey(inputZip)) {
			return resultsMap.get(inputZip);
		}
		// else, do calculation
		long totalValue = 0;
		int propertyCount = 0;
		for(Property p : propertyData) {
			if(p.getZipCode() == inputZip) {
				double value = calcType.getValue(p);
				if(Double.isNaN(value)) {
					continue;
				}	
				totalValue += value;
				propertyCount++;
			}
		}
		Long avgValue = 0L;
		if(propertyCount != 0) {
			avgValue = (long)totalValue / propertyCount;
		}
		resultsMap.put(inputZip, avgValue);
		return avgValue;
	}
	

	
	/*
	 * Action 6: get Total Market Value Per Capita - the total market value for all properties 
	 * in the ZIP Code divided by the population of that ZIP Code
	 * @param inputZip
	 */
	public Long getTotMarketValuePC(int inputZip) {
		if(totMarketValuePCResult.containsKey(inputZip)) {
			return totMarketValuePCResult.get(inputZip);
		}
		
		// get total population of given inputZip
		long zipTotPop = 0;
		for(Population pop : populationData) {
			if(inputZip == pop.getZipCode()) {
				zipTotPop += pop.getPopulation();
			}
		}
		
		// check if zipTotPop == 0
		if(zipTotPop == 0) {
			totMarketValuePCResult.put(inputZip, 0l);
			return 0L;
		}
		
		// get total market value of given inputZip
		double zipTotMarketValue = 0;
		for(Property property : propertyData) {
			if(inputZip == property.getZipCode()) {
				zipTotMarketValue += property.getMarketValue();
			}
		}
		
		// calculate avg
		Long avgValue = (long) (zipTotMarketValue / zipTotPop);
		totMarketValuePCResult.put(inputZip, avgValue);
		return avgValue;
		
	}
	

	
	/*
	 * Action 7: Custom feature
	 * suggestion: display average market value and vaccination rate (latest) per zipcode
	 * each line: Zipcode: 19100, Average Market Value: $123,456, Full Vaccination Rate: 0.12
	 * no need for user input
	 * 
	 */
	public TreeMap<Integer, List<Double>> getVaccRates() throws ParseException {
	    if (covidData == null || populationData == null) return null;
	    
	    // check if already exist
	    if(vaccRatesResult != null) return vaccRatesResult;
	    
	    if (populationMap == null) {
			populationMap  = getPopulationMap();
		}
	    
	    if(vaccRatesPerZip == null) {
	    	vaccRatesPerZip = calculateVaccRates();
	    }
	    
	    // else do calculation to populate the map
	    vaccRatesResult = new TreeMap<Integer, List<Double>>();
	    for (Entry<Integer, Integer> entry : populationMap.entrySet()) {
	    	int currZip = entry.getKey();
    		List<Double> currData = new ArrayList<Double>();
    		currData.add((double)getAvgMarketValue(currZip));
    		currData.add(vaccRatesPerZip.get(currZip));
    		vaccRatesResult.put(currZip, currData);
    		
    	}
	    
	    return vaccRatesResult;
	}
	
	/*
	 * Helper function to calculate the latest full vaccination rates for each zipcode from CovidData
	 */
	private Map<Integer, Double> calculateVaccRates() {
		if (covidData == null || populationData == null) return null;
		
		
		if (populationMap == null) {
			populationMap  = getPopulationMap();
		}
			
		Map<Integer, Integer> maxVaccMap = covidData.stream()
				.collect(Collectors.toMap(CovidData::getZipCode, CovidData::getFullVaccination,
						BinaryOperator.maxBy(Comparator.comparing(Function.identity()))));
		
		Map<Integer, Double> vaccRate = new HashMap<>();
		
		for (Entry<Integer, Integer> entry : populationMap.entrySet()) {
	        Integer numerator = maxVaccMap.get(entry.getKey());
	        Integer denom = entry.getValue();
	        vaccRate.put(entry.getKey(), numerator != null ? (double) numerator / denom : null);
	    }		
	    
	    return vaccRate;
	}
	
	
}
