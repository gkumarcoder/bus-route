package com.target.busroute;

import java.io.*;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gkumar3 This Application returns the time in minutes for a bus on
 *         "BUS ROUTE" leaving from "BUS STOP NAME" going "DIRECTION" using the
 *         API.
 */

public class BusRouteApplication {

	private final static Logger logger = LoggerFactory.getLogger(BusRouteApplication.class);
	
	public static int time;
	public static String routeID;
	public static String directionID;
	public static String dir;
	public static String urlString;
	public static String stopID = StringUtils.EMPTY;
	public static String timeStamp = StringUtils.EMPTY;

	/**
	 * The main function of the program will take three command-line argument at the
	 * time of running the java program. i.e route,stop,direction
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		if (args.length != 3) {
			logger.info("entered argument is may be greater or less than three {}",ApplicationConstant.ERR_MSG);
			throw new BusApplicationGenericException(ApplicationConstant.ERR_MSG);
		}

		String route = Optional.ofNullable(args[0]).orElseGet(()->StringUtils.EMPTY);
		
		if (route == null || route.trim().length() == 0) {
			logger.info("entered route id is null {}",ApplicationConstant.ERR_IN_ROUTE);
			throw new BusApplicationGenericException(ApplicationConstant.ERR_IN_ROUTE);
		}
		
		String stop = Optional.ofNullable(args[1]).orElseGet(()->StringUtils.EMPTY);

		if (stop == null || stop.trim().length() == 0) {
			logger.info("entered stop id is null {}",ApplicationConstant.ERR_IN_STOP);
			throw new BusApplicationGenericException(ApplicationConstant.ERR_IN_STOP);
		}
		
		String direction = Optional.ofNullable(args[2]).orElse(StringUtils.EMPTY);
		
		if (direction == null || direction.trim().length() == 0) {
			logger.info("direction is null {}",ApplicationConstant.ERR_IN_DIRECTION);
			throw new BusApplicationGenericException(ApplicationConstant.ERR_IN_DIRECTION);
		}
		
		// Convert direction id
		dir = BusRouteInfoService.validateDirection(direction);
		if (Direction.ERROR.getDirection().equals(dir)) {
			logger.info(" invalid direction {}",dir);
			throw new BusApplicationGenericException(dir);
		}

		// Verifies the route ID for the specific route that was entered and return the ID
		routeID = Optional.ofNullable(BusRouteInfoService.getBusRouteId(ApplicationConstant.ENDPOINT + "Routes?format=json",
				"Description", "Route", route)).orElseGet(()->StringUtils.EMPTY);
		if (routeID.equalsIgnoreCase(ApplicationConstant.DEFAULT_VALUE)) {
			logger.info("invalid route id  {} : {}",route ,ApplicationConstant.ERR_NOTE_FOUND);
			throw new BusApplicationGenericException(route + ApplicationConstant.ERR_NOTE_FOUND);
		}

		// Verifies the directionID for the specific direction and if found matched direction return the valid ID
		
		directionID =  Optional.ofNullable(BusRouteInfoService.getBusRouteDirection(
				ApplicationConstant.ENDPOINT + "Directions/" + routeID + "?format=json", "Text", "Value", dir)).orElseGet(()->StringUtils.EMPTY);
		
		if (directionID.equalsIgnoreCase(ApplicationConstant.DEFAULT_VALUE)) {
			 logger.info("invalid direction ID {} : {}",direction ,ApplicationConstant.ERR_IN_DIR);
			 throw new BusApplicationGenericException(direction + ApplicationConstant.ERR_IN_DIR);
		}

		// Verifies the stop ID String and if found matched stop return the stop ID
		
		stopID =  Optional.ofNullable(BusRouteInfoService.getBusRouteStop(
				ApplicationConstant.ENDPOINT + "Stops/" + routeID + ApplicationConstant.SLASH + directionID + "?format=json", "Text",
				"Value", stop)).orElseGet(()->StringUtils.EMPTY);
		
		if (stopID.equals(StringUtils.EMPTY)) {
			  logger.info(" invalid stop id {} {}",stop , ApplicationConstant.ERR_NOTE_FOUND);
			 throw new BusApplicationGenericException(stop + ApplicationConstant.ERR_NOTE_FOUND);
		}

		// Verifies the timeStamp String and if found matched timeStamp return the departureTime.
		
		timeStamp =  Optional.ofNullable(BusRouteInfoService.getBusRouteTimeStamp(
				ApplicationConstant.ENDPOINT + routeID + ApplicationConstant.SLASH + directionID + ApplicationConstant.SLASH + stopID + "?format=json",
				"RouteDirection", "DepartureTime", timeStamp)).orElseGet(()->StringUtils.EMPTY);
		
		if (timeStamp.equals(StringUtils.EMPTY)) {
			// The specification says that if the last bus of the day has already left to not return anything. So I exit clean here.
			System.exit(0);
		}

		// compute the time and print the result
		BusRouteInfoService.computeTime(timeStamp);
	}

}
