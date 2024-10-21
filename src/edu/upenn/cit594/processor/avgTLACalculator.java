package edu.upenn.proj1.processor;

import edu.upenn.proj1.util.Property;

public class avgTLACalculator implements avgCalculator{

	@Override
	public Double getValue(Property pData) {

		return pData.getTotalLivableArea();
	}
	
}
