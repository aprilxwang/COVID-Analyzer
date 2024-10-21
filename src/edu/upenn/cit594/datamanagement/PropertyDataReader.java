package edu.upenn.proj1.datamanagement;

import edu.upenn.proj1.util.Property;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PropertyDataReader{

    public List<Property> readPropertyData() throws IOException;


}
