package com.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReadFile implements Iterator<String> {

	private static Logger logger = LogManager.getLogger(ReadFile.class);

	private String fileName;
	BufferedReader bufferedReader;
	private String line;

	public ReadFile(String fileName) {
		this.fileName = fileName;
		setupBufferedReader(this.fileName);
		logger.info("File Reading setup Sucessfully");
	}

	public void setupBufferedReader(String fileName) {
		// FileReader reads text files in the default encoding.
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("Error while setting up" + e);
		}

	}

	public boolean hasNext() {
		return line != null;
	}

	public String next() {
		String prevLine = line;
		try {
			line = bufferedReader.readLine();
		} catch (Exception ex) {
			logger.error("Unable to open file '" + fileName + "'");
		}
		return prevLine;
	}

	public void close() {
		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error reading file '" + fileName + "'");
		}
	}

}
