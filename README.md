# Table of Contents
1. [Understanding the challenge](README.md#challenge)
2. [Technology](README.md#technology)
3. [Implementation details](README.md#logic)
4. [Running Time and Memory](README.md#big0)
5. [Example](README.md#example)
6. [Scalable Architecture which can fit to Real Streaming ](README.md#scalable)

# Understanding the challenge
	https://github.com/InsightDataScience/edgar-analytics/blob/master/README.md#example
# Technology
	Java, Maven
# Implementation details
	We read line by line, not everything. This strucutre will allow to scale in real time.
	ReadFile is implementing Iterator
	WriteFile has a writeString to allow writing of output line by line, so that we use memory efficiently
	ProcessEvent has the main Logic
	currentTIme = CurrentLog file's time
	Main DataStructure 
		Map<Long, Map<String, PriorityQueue<Row>>> buckets;
	Each Bucket's key is timeStamp, Map <ipaddress, Row>
		We never allow bucket size > inactivity Time +1 
		If We have any key whose value is less than currentTIme-inactivityTime we flush them to file and remove bucket 
		We read line by line from input and add them into bucket which has a priorityqueue sorted by time
		At Last after reading all lines, from the buckets we compute the subResult for each bucket in order and then sort them since there can be an entry which is in latest bucket but has earliest timestamp.
		
# Example
	```
		java -jar target/edgar-analytics.jar ./input/log.csv ./input/inactivity_period.txt ./output/sessionization.txt
		cat ./output/sessionization.txt
	```
		
#	Running Time and Memory
	Running Time, we read line by line
		Minimum running time  is o(n)
		Since we assume Hashing is good and we dont have more than inactivity+1 buckets and in the second leg it is max of distinct ipaddress it uses good memory but running time should be o(1) for lookup
		Each stage we might have to remove all elements from all buckets and write to file in worst case
		o(n) * o(k+1) 
		k<<<n
		o(k*logk) since we do sorting at last but just assuming n is large
		we do achieve close to o(n)
		
		LinkedHashMap does take additional memory compared to hasmap but since our k is small we read line by line we should be able to efficiently work this out.
		

# Scalable Architecture which can fit to Real Streaming
	Since we read line by line, we can either from file or from Kafka

NOTE : I have not used gitignore to allow target folders so that you can directly use for testing

		
		


