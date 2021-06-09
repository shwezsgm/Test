# Test
1. A simple Java application to fetch weather data.

2. Database used: Oracle 12.2.0
   Java version: 1.8

3. Rapid API used: Yahoo weather api
	As it is free and does not require any details to be shared.
	Fetched accurate weather data for any woe id: where on earth id.
	Gives results for any place whose woe_id is provided.
	Used this url: https://in.news.yahoo.com/weather/india/karnataka/bengaluru-2295420
	for fetching sample woe_ids.
	Can return the temperature data in Celsius or Fahrenheit. 
	All other apps where mostly fetching weather data based on place, latitude and longitude and mostly US 
	zipcodes only.

4. Have used simple JDBC to first setup the database tables, then insert the web service returned current weather data 
   into the database, and finally query weather history data for a given woe_id :
	
	create two tables zipcode and zipcode_weather.
	zipcode: id, zipcode	PK: zipcode

	zipcode_weather: id, zip_id, datetime, temperature	
	PK: zip_id, datetime
	FK: zip_id referring to the zip_code of the zipcode table.

5. Dependencies added in Maven: com.mashape.unirest and log4j

6. ojdbc jar added as an external dependency for JDBC.  


