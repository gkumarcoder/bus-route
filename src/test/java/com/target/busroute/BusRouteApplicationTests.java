package com.target.busroute;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

/**
 * This class will test the bus-route application with different different boundary conditions.
 * 
 * @author gkumar3
 */
public class BusRouteApplicationTests {
	
	//test
System.out.printlin("java");
	/**
	 * In this case we are passing the correct route,stop and direction so,this test case will print the ETA (Estimated Arrival Time)
	 * 
	 * @throws IOException
	 */
	@Test
	//@Ignore
	public void testCase1() throws IOException {
		String arg[] = { "5", "77PO", "north" };
		BusRouteApplication.main(arg);
	}
	/**
	 * In this case it will ask user to enter the direction name eg "Please enter correct direction name"
	 * @throws IOException
	 */
	
	@Test
	@Ignore
	public void testCase2() throws IOException {
		String arg[] = { "5", "77PO", "" };
		BusRouteApplication.main(arg);

	}
	/**
	 * In this case it will ask user to enter the stop name eg "Please enter correct stop name"
	 * @throws IOException
	 */
	@Test
	@Ignore
	public void testCase3() throws IOException {
		String arg[] = { "5", "", "north" };
		BusRouteApplication.main(arg);

	}
	
  
}
