package com.zipcode.weather;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

public class WeatherInfoService {

	private static final Logger logger = Logger.getLogger(WeatherInfoService.class);

	public int getWeatherInfo(String zipCode) {

		int temp = 0;

		// Host url
		String host = "https://yahoo-weather5.p.rapidapi.com/weather";
		String charSet = "UTF-8";

		// Headers for the request
		String x_rapidapi_host = "yahoo-weather5.p.rapidapi.com";
		String x_rapidapi_key = "dc6b86aa03msh5275f816431d42cp115003jsn0b2935345aca"; // Rapid API Key

		// Parameters
		String woeid = zipCode;// "2295386";
		String format = "json";
		String u = "c";	//'c' for Celsius and 'f' for Fahrenheit

		try {
			// Format query for preventing encoding problems
			String query = String.format("woeid=%s&f=%s&u=%s", URLEncoder.encode(woeid, charSet),
					URLEncoder.encode(format, charSet), URLEncoder.encode(u, charSet));

			GetRequest req = Unirest.get(host + "?" + query);
			System.out.println("Request URL: " + req.getUrl());
			logger.info("Request URL: " + req.getUrl());

			HttpResponse<JsonNode> response = req.header("x-rapidapi-key", x_rapidapi_key)
					.header("x-rapidapi-host", x_rapidapi_host).asJson();

			logger.info(response.getStatus() + "\n" + response.getHeaders().get("Content-Type")
					+ response.getBody().getObject());

			temp = response.getBody().getObject().getJSONObject("current_observation").getJSONObject("condition")
					.getInt("temperature");
			logger.info("Temperature: " + temp);

		} catch (UnsupportedEncodingException un) {
			logger.error(un);
		} catch (UnirestException e) {
			logger.error(e);
		}

		return temp;
	}
}
