package edu.upenn.cit594.datamanagement;

import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Reader {

    protected final FileReader reader;

    public Reader(String filename) throws IOException {
        this.reader = new FileReader(filename);
    }





}

