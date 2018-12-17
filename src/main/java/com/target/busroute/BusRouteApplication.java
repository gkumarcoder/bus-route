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
	public static String stopID = "";
	public static String timeStamp = "";

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

		String route = Optional.ofNullable(args[0]).orElse(StringUtils.EMPTY);
		if (route == null || route.trim().length() == 0) {
			System.out.println(ApplicationConstant.ERR_IN_ROUTE);
			System.exit(-1);
		}
		String stop = Optional.ofNullable(args[1]).orElse(StringUtils.EMPTY);

		if (stop == null || stop.trim().length() == 0) {
			System.out.println(ApplicationConstant.ERR_IN_STOP);
			System.exit(-1);
		}
		String direction = Optional.ofNullable(args[2]).orElse(StringUtils.EMPTY);
		if (direction == null || direction.trim().length() == 0) {
			System.out.println(ApplicationConstant.ERR_IN_DIRECTION);
			System.exit(-1);
		}
		// Convert direction id
		dir = BusRouteInfoService.getDirection(direction);
		if (Direction.ERROR.getDirection().equals(dir)) {
			System.out.println(dir);
			System.exit(-1);
		}

		// Verifies the route ID for the specific route that was entered and return the ID
		routeID = BusRouteInfoService.getBusRouteId(ApplicationConstant.ENDPOINT + "NexTrip/Routes?format=json",
				"Description", "Route", route);
		if (routeID == -1) {
			System.out.println(route + ApplicationConstant.ERR_NOTE_FOUND);
			System.exit(-1);
		}

		// Verifies the directionID for the specific direction and if found matched direction return the valid ID
		directionID = BusRouteInfoService.getBusRouteDirection(
				ApplicationConstant.ENDPOINT + "NexTrip/Directions/" + routeID + "?format=json", "Text", "Value", dir);
		if (directionID == -1) {
			System.out.println(direction + ApplicationConstant.ERR_IN_DIR);
			System.exit(-1);
		}

		// Verifies the stop ID String and if found matched stop return the stop ID
		stopID = BusRouteInfoService.getBusRouteStop(
				ApplicationConstant.ENDPOINT + "NexTrip/Stops/" + routeID + "/" + directionID + "?format=json", "Text",
				"Value", stop);
		if (stopID.equals("")) {
			System.out.println(stop + ApplicationConstant.ERR_NOTE_FOUND);
			System.exit(-1);
		}

		// Verifies the timeStamp String and if found matched timeStamp return the
		// departureTime.
		timeStamp = BusRouteInfoService.getBusRouteTimeStamp(
				ApplicationConstant.ENDPOINT + "NexTrip/" + routeID + "/" + directionID + "/" + stopID + "?format=json",
				"RouteDirection", "DepartureTime", timeStamp);
		if (timeStamp.equals("")) {
			// The specification says that if the last bus of the day has already left to
			// not return anything. So I exit clean here.
			System.exit(0);
		}

		// compute the time and print the result
		BusRouteInfoService.computeTime(timeStamp);
	}

}
