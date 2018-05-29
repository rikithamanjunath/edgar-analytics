package com.challenge.insight;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.DO.Row;
import com.file.ReadFile;
import com.file.WriteFile;

public class ProcessFile {
	
	
	private static Logger logger = LogManager.getLogger(ProcessFile.class);

	private void processFile(String inputFile, String inactivityFile, String outputFile) {
		long inactivityTime = getInactivityTime(inactivityFile);
		ReadFile inputReader = new ReadFile(inputFile);
		WriteFile outputWriter = new WriteFile(outputFile);
		int lineNumber = 0;
		ProcessEvent  processEvent = new ProcessEvent(inactivityTime);
		while(inputReader.hasNext()){
			// ignore the header
			if(lineNumber==0){
				inputReader.next();
				lineNumber++;
				continue;
			}
			Row row = new Row(inputReader.next());
			logger.debug(" Input row {}" , row.toString());
			lineNumber++;
			processEvent.addEvent(row);
			if (processEvent.getCurrentMaxTime() != -1 && processEvent.getBuckets().size()>inactivityTime) {
				processEvent.flushAllPrevious(outputWriter);
			}
			
			
			
		}
		processEvent.flushAll(outputWriter);
		
		outputWriter.close();
		inputReader.close();
	} 

	private static long getInactivityTime(String inactivityFile) {
		ReadFile inputReader = new ReadFile(inactivityFile);
		long inactivityTime = Long.parseLong(inputReader.next());
		inputReader.close();
		logger.info("Inactivity Time {}",inactivityTime);
		return inactivityTime;
	}

	/**
	 * java -jar <name> ./input/log.csv ./input/inactivity_period.txt
	 * ./output/sessionization.txt
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			logger.warn("Wrong number of arguments, Kindly provide three agruments {},{},{}", "./input/log.csv ",
					"./input/inactivity_period.txt", " ./output/sessionization.txt");
		}
		String inputFile = args[0];
		String inactivityFile = args[1];
		String outputFile = args[2];
		logger.info("Recived Files {},{},{}", inputFile, inactivityFile, outputFile);
		ProcessFile processFile = new ProcessFile();
		processFile.processFile(inputFile, inactivityFile, outputFile);
		logger.info("Processed Successfully, output present in {}", outputFile);
	}

}
