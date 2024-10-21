package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	
	private FileWriter out;
	private Logger() {}
	
	private static Logger instance = new Logger();
	
	
	/*
	 * Singleton instance logger
	 */
    public static Logger getInstance() {
    	return instance;
    }
    
    
    /*
	 * set filename
	 */
	public void setDestination(String filename) throws IOException {
		if(filename == null) {
			out = null;
		} else {
			out = new FileWriter(filename, true);
		}
    }
	
	
	/*
	 * log for messages
	 */
    public void log(String message) throws IOException {
    	String timeAndMsg = System.currentTimeMillis() + " " + message; 
    	
    	if(out == null) {
    		System.err.println(timeAndMsg);
    		System.err.flush();
    	} else {
    		out.write(timeAndMsg + '\n');
    		out.flush();
    		out.close();
    	}


    }
}
