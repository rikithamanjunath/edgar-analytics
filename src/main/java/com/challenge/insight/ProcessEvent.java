package com.challenge.insight;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.DO.Row;
import com.file.WriteFile;

public class ProcessEvent {

	private static Logger logger = LogManager.getLogger(ProcessEvent.class);
	private long currentMaxTime;
	private Map<Long, Map<String, PriorityQueue<Row>>> buckets;
	private long inactivityTime;

	public ProcessEvent(long inactivityTime) {
		currentMaxTime = -1;
		buckets = new LinkedHashMap<>();
		this.inactivityTime = inactivityTime + 1;

	}

	public boolean addEvent(Row row) {
		// if descending than error
		if (currentMaxTime - row.getEpochTime() > inactivityTime) {
			logger.error("Wrong Input, INput is not sorted");
			return false;
		}
		long tempMax = addEventToBucket(row);
		currentMaxTime = Math.max(currentMaxTime, tempMax);
		for (long i = currentMaxTime - 1; i > currentMaxTime - inactivityTime; i--) {
			moveFromOlderBuckets(row, i, tempMax);
		}

		return true;

	}

	private void moveFromOlderBuckets(Row row, long oldBucket, long newBucket) {

		for (Entry<Long, Map<String, PriorityQueue<Row>>> ent : buckets.entrySet()) {
			if (ent.getKey() == oldBucket && ent.getValue().containsKey(row.getIp())) {
				logger.info("Moving " + ent.getValue().get(row.getIp()));
				PriorityQueue<Row> temp = ent.getValue().get(row.getIp());
				ent.getValue().remove(row.getIp());
				buckets.get(newBucket).get(row.getIp()).addAll(temp);

			}
		}

	}

	public void flushAllPrevious(WriteFile writeFile) {
		PriorityQueue<Result> resultQueue = new PriorityQueue<>();
		long startIndex = currentMaxTime - inactivityTime - 1;
		while (buckets != null && buckets.size() > inactivityTime) {
			if (buckets.containsKey(startIndex)) {
				Map<String, PriorityQueue<Row>> removedSet = buckets.remove(startIndex);
				for (Entry<String, PriorityQueue<Row>> ent : removedSet.entrySet()) {
					Result result = getStringFromQueue(ent.getValue());
					if (result != null) {
						resultQueue.add(result);
					} else {
						logger.warn("No result" + ent.getKey());
					}
				}

			} else {
				logger.warn("Time not present in bucket {} , current max time is {}", startIndex, currentMaxTime);
			}
			startIndex++;
		}
		while (!resultQueue.isEmpty()) {
			Result result = resultQueue.poll();
			logger.info(" Result for key {} is {}", result.key, result.value);
			writeFile.writeString(result.value);
		}
	}

	public void flushAll(WriteFile writeFile) {
		PriorityQueue<Result> resultQueue = new PriorityQueue<>();
		long startIndex = currentMaxTime - inactivityTime - 1;
		while (buckets != null && buckets.size() > 0) {
			if (buckets.containsKey(startIndex)) {
				Map<String, PriorityQueue<Row>> removedSet = buckets.remove(startIndex);
				for (Entry<String, PriorityQueue<Row>> ent : removedSet.entrySet()) {
					Result result = getStringFromQueue(ent.getValue());
					if (result != null) {
						resultQueue.add(result);
					} else {
						logger.warn("No result" + ent.getKey());
					}
				}

			} else {
				logger.warn("Time not present in bucket {} , current max time is {}", startIndex, currentMaxTime);
			}
			startIndex++;
		}
		while (!resultQueue.isEmpty()) {
			Result result = resultQueue.poll();
			logger.info(" Result for key {} is {}", result.key, result.value);
			writeFile.writeString(result.value);
		}
	}

	// IP address of the user exactly as found in log.csv
	// date and time of the first webpage request in the session (yyyy-mm-dd
	// hh:mm:ss)
	// date and time of the last webpage request in the session (yyyy-mm-dd
	// hh:mm:ss)
	// duration of the session in seconds
	// count of webpage requests during the session
	public static class Result implements Comparable<Result> {
		String value;
		long key;

		public Result(String value, long key) {
			this.value = value;
			this.key = key;
		}

		@Override
		public int compareTo(Result o) {

			return (int) (this.key - o.key);
		}
	}

	private Result getStringFromQueue(PriorityQueue<Row> queue) {
		if (queue.size() == 0) {
			logger.error(" Queue is empty");
			return new Result("", 0);
		} else {

			Row firstRow = queue.poll();
			Row lastRow = firstRow;
			int count = 1;
			while (!queue.isEmpty()) {
				lastRow = queue.poll();
				count++;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(firstRow.getIp());
			sb.append(",");
			sb.append(firstRow.getDate());
			sb.append(" ");
			sb.append(firstRow.getTime());
			sb.append(",");
			sb.append(lastRow.getDate());
			sb.append(" ");
			sb.append(lastRow.getTime());
			sb.append(",");
			sb.append(lastRow.getEpochTime() - firstRow.getEpochTime() + 1);
			sb.append(",");
			sb.append(count);
			return new Result(sb.toString(), firstRow.getEpochTime());
		}
	}

	public long addEventToBucket(Row row) {
		if (!buckets.containsKey(row.getEpochTime())) {
			buckets.put(row.getEpochTime(), new LinkedHashMap<>());
		}
		if (!buckets.get(row.getEpochTime()).containsKey(row.getIp())) {
			buckets.get(row.getEpochTime()).put(row.getIp(), new PriorityQueue<>());
		}
		buckets.get(row.getEpochTime()).get(row.getIp()).add(row);
		return row.getEpochTime();
	}

	public long getCurrentMaxTime() {
		return currentMaxTime;
	}

	public void setCurrentMaxTime(long currentMaxTime) {
		this.currentMaxTime = currentMaxTime;
	}

	public long getInactivityTime() {
		return inactivityTime;
	}

	public void setInactivityTime(long inactivityTime) {
		this.inactivityTime = inactivityTime;
	}

	public Map<Long, Map<String, PriorityQueue<Row>>> getBuckets() {
		return buckets;
	}

	public void setBuckets(Map<Long, Map<String, PriorityQueue<Row>>> buckets) {
		this.buckets = buckets;
	}

	@Override
	public String toString() {
		return "ProcessEvent [currentMaxTime=" + currentMaxTime + ", buckets=" + buckets + ", inactivityTime="
				+ inactivityTime + "]";
	}

}
