package com.DO;

import java.util.StringTokenizer;

public class Row implements Comparable<Row> {
	private String ip;
	private String date;
	private String time;
	private String zone;
	private String cik;
	private String accession;
	private String extention;
	private long epochTime;

	// Constructor
	public Row(String line) {
		if (line != null) {
			StringTokenizer tokenizer = new StringTokenizer(line, ",");
			if (tokenizer.hasMoreTokens()) {
				this.setIp(tokenizer.nextToken());
				this.setDate(tokenizer.nextToken());
				this.setTime(tokenizer.nextToken());
				this.setEpochTime(epochTime);
				this.setZone(tokenizer.nextToken());
				this.setCik(tokenizer.nextToken());
				this.setAccession(tokenizer.nextToken());
				this.setExtention(tokenizer.nextToken());
				// default is UTC
				this.setEpochTime(TimeConverter.getEpochTimeInSeconds(this.getDate(), this.getTime(), "UTC"));

			}
		}

	}

	// code size idx norefer noagent find crawler browser
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getCik() {
		return cik;
	}

	public void setCik(String cik) {
		this.cik = cik;
	}

	public String getAccession() {
		return accession;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}

	public String getExtention() {
		return extention;
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}

	public long getEpochTime() {
		return epochTime;
	}

	public void addEpochTime(long epochTime) {
		this.epochTime = epochTime;
	}

	public void setEpochTime(long epochTime) {
		this.epochTime = epochTime;
	}

	@Override
	public String toString() {
		return "Row [ip=" + ip + ", date=" + date + ", time=" + time + ", zone=" + zone + ", cik=" + cik
				+ ", accession=" + accession + ", extention=" + extention + ", epochTime=" + epochTime + "]";
	}
 

	@Override
	public int compareTo(Row o) {
		// TODO Auto-generated method stub
		return (int)(this.epochTime-o.getEpochTime());
	}

}
