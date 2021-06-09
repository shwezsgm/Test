package com.zipcode.weather;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

/**
 * The user-interactive main class for fetching weather information.
 *
 */
public class TestWeatherApp {

	static Scanner input = new Scanner(System.in);

	private static final Logger logger = Logger.getLogger(TestWeatherApp.class);

	public static void main(String[] args) throws Exception {

		// Create the table structure for : Zip Codes table and Zip Codes Weather table:
		// zipcode, zipcode_weather
		setupDatabase();

		int choice;
		do {
			choice = printMenu();
			switch (choice) {
			case 1:
				addZipCodes();
				break;
			case 2:
				viewCurrentWeatherAtZipCode();
				break;
			case 3:
				viewWeatherHistoryAtZipCode();
				break;
			case 4:
				System.out.println("Exiting Program...");
				input.close();
				System.exit(0);
				break;

			default:
				System.out.println(choice + " is not a valid Menu Option! Please Select Another.\n");
			}
		} while (choice != 4 /* Exit loop when choice is 4 */);

	}

	/**
	 * Prints out the menu on the console for the user.
	 * 
	 * @return an int - the choice of user
	 */
	private static int printMenu() {
		System.out.println("\nWelcome to the Weather by ZipCodes App!");
		System.out.println("1 - Add Zip Codes");
		System.out.println("2 - View Current Weather at Zip Code");
		System.out.println("3 - View Weather History at Zip Code");
		System.out.println("4 - Quit");
		System.out.print("\nEnter Your Menu Choice: ");

		int choice = input.nextInt();
		input.nextLine();
		return choice;
	}

	/**
	 * does the basic database setup for storing and fetching weather data for a woe
	 * id.
	 */
	public static void setupDatabase() {
		WeatherDAO dao = new WeatherDAO();
		dao.createTables();
		logger.info("Database setup complete");
	}

	/**
	 * Used to add woe ids to the database.
	 */
	public static void addZipCodes() {
		logger.info("Starting method addZipCodes..");
		System.out.println("Enter a comma separated list of zipcodes(6 digits): ");
		WeatherDAO dao = new WeatherDAO();

		String inputCodes = input.nextLine();
		List<String> zipCodes = Stream.of(inputCodes.split(",")).map(String::trim).collect(Collectors.toList());
		logger.debug("Input zipCodes: " + zipCodes);
		List<String> validZipCodes = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();

		for (String zipCode : zipCodes) {
			if (validateZipCode(zipCode)) {
				validZipCodes.add(zipCode);
			} else {
				builder.append(zipCode).append(": Invalid\n");
			}
		}
		logger.debug(builder.toString());
		if (validZipCodes.size() > 0) {
			logger.debug("Valid zipCodes: " + validZipCodes);
			dao.addZipCodes(validZipCodes);
		} else {
			System.out.println("None of the zip codes entered are valid.");
		}
		logger.info("Ending method addZipCodes..");

	}

	/**
	 * Displays the current weather data for a given woe id.
	 */
	public static void viewCurrentWeatherAtZipCode() {		
		logger.info("Starting method viewCurrentWeatherAtZipCode..");
		System.out.println("Enter zipcode: ");
		String zipCode = input.next();
		WeatherInfoService service = new WeatherInfoService();
		WeatherDAO dao = new WeatherDAO();

		double temp = service.getWeatherInfo(zipCode);
		System.out.println("At zipCode: " + zipCode + ", temperature: " + temp + "\n");

		dao.saveCurrentWeatherData(zipCode, temp);
		logger.info("Finished saving current weather data.");
	}

	/**
	 * View the weather history for a woeid from the database.
	 */
	public static void viewWeatherHistoryAtZipCode() {
		logger.info("viewWeatherHistoryAtZipCode: ");
		System.out.println("Enter zipcode: ");

		String zipCode = input.next();

		WeatherDAO dao = new WeatherDAO();
		Map<Date, String> map = dao.getWeatherHistoryData(zipCode);
		System.out.println("Weather history data for zipCode: " + map);
		
		logger.info("Weather history data for zipCode: " + map);
	}

	/**
	 * Validates the user entered woe code. must be all digits with length ranging
	 * from 1 to 32.
	 * 
	 * @param zipCode the woe code
	 * 
	 * @return boolean: true if code is valid else false.
	 */
	public static boolean validateZipCode(String zipCode) {
		return zipCode.matches("\\d{1,32}");
	}
}
