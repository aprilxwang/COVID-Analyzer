package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.Property;

public class avgTLACalculator implements avgCalculator{

	@Override
	public Double getValue(Property pData) {

		return pData.getTotalLivableArea();
	}
	
}
