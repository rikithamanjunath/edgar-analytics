package com.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WriteFile {

	private static Logger logger = LogManager.getLogger(WriteFile.class);

	private String fileName;
	BufferedWriter bufferedWriter;

	public WriteFile(String fileName) {
		this.fileName = fileName;
		setupBufferedReader(this.fileName);
		logger.info("File Writer setup Sucessfully");
	}

	public void setupBufferedReader(String fileName) {
		// FileReader reads text files in the default encoding.
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName);
			bufferedWriter = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error while setting up" + e);
		}

	}

	public void writeString(String line) {
		try {
			bufferedWriter.write(line+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(" error while writing", e);
		}
	}

	public void close() {
		try {
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error reading file '" + fileName + "'");
		}
	}

}
