package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Population;

import java.io.IOException;

import java.util.List;

public interface PopulationDataReader {
    public List<Population> readPopulationData() throws IOException;
}
