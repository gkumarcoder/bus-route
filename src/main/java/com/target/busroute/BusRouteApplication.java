package com.target.busroute;

import java.io.*;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

/**
 * @author gkumar3 This Application returns the time in minutes for a bus on
 *         "BUS ROUTE" leaving from "BUS STOP NAME" going "DIRECTION" using the
 *         API.
 */

public class BusRouteApplication {

	public static int time;
	public static int routeID;
	public static int directionID;
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
			System.out.println(ApplicationConstant.ERR_MSG);
			System.exit(-1);
		}

		// check for null value 
		
		String route = Optional.ofNullable(args[0]).orElseGet(()->StringUtils.EMPTY);
		
		if (route == null || route.trim().length() == 0) {
			throw new BusApplicationGenericException(ApplicationConstant.ERR_IN_ROUTE);
		}
		
		String stop = Optional.ofNullable(args[1]).orElseGet(()->StringUtils.EMPTY);

		if (stop == null || stop.trim().length() == 0) {
			throw new BusApplicationGenericException(ApplicationConstant.ERR_IN_STOP);
		}
		
		String direction = Optional.ofNullable(args[2]).orElseGet(()->StringUtils.EMPTY);
		
		if (direction == null || direction.trim().length() == 0) {
			throw new BusApplicationGenericException(ApplicationConstant.ERR_IN_DIRECTION);
		}
		
		// Convert direction id
		dir = BusRouteInfoService.validateDirection(direction);
		if (Direction.ERROR.getDirection().equals(dir)) {
			throw new BusApplicationGenericException(dir);
		}

		// Verifies the route ID for the specific route that was entered and return the ID
		routeID = BusRouteInfoService.getBusRouteId(ApplicationConstant.ENDPOINT + "Routes?format=json",
				"Description", "Route", route);
		if (routeID == -1) {
			throw new BusApplicationGenericException(route + ApplicationConstant.ERR_NOTE_FOUND);
		}

		// Verifies the directionID for the specific direction and if found matched direction return the valid ID
		directionID = BusRouteInfoService.getBusRouteDirection(
				ApplicationConstant.ENDPOINT + "Directions/" + routeID + "?format=json", "Text", "Value", dir);
		if (directionID == -1) {
			 throw new BusApplicationGenericException(direction + ApplicationConstant.ERR_IN_DIR);
		}

		// Verifies the stop ID String and if found matched stop return the stop ID
		stopID = BusRouteInfoService.getBusRouteStop(
				ApplicationConstant.ENDPOINT + "Stops/" + routeID + ApplicationConstant.SLASH + directionID + "?format=json", "Text",
				"Value", stop);
		if (stopID.equals(StringUtils.EMPTY)) {
			 throw new BusApplicationGenericException(stop + ApplicationConstant.ERR_NOTE_FOUND);
		}

		// Verifies the timeStamp String and if found matched timeStamp return the departureTime.
		timeStamp = BusRouteInfoService.getBusRouteTimeStamp(
				ApplicationConstant.ENDPOINT + routeID + ApplicationConstant.SLASH + directionID + ApplicationConstant.SLASH + stopID + "?format=json",
				"RouteDirection", "DepartureTime", timeStamp);
		if (timeStamp.equals(StringUtils.EMPTY)) {
			// The specification says that if the last bus of the day has already left to not return anything. So I exit clean here.
			System.exit(0);
		}

		// compute the time and print the result
		BusRouteInfoService.computeTime(timeStamp);
	}

}
