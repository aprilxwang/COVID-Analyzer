package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.Property;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PropertyDataReader{

    public List<Property> readPropertyData() throws IOException;


}
