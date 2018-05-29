# optional mvn install
ls -ltr .
java -jar target/edgar-analytics.jar ./input/log.csv ./input/inactivity_period.txt ./output/sessionization.txt
cat ./output/sessionization.txt