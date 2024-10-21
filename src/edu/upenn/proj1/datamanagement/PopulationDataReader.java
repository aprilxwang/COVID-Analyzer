package edu.upenn.proj1.datamanagement;

import edu.upenn.proj1.util.Population;

import java.io.IOException;

import java.util.List;

public interface PopulationDataReader {
    public List<Population> readPopulationData() throws IOException;
}
