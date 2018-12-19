package com.target.busroute;
/**
 * Enumeration of all possible direction types.
 * @author gkumar3
 * 
 */
public enum Direction {
	SOUTH("1"), 
	EAST("2"), 
	WEST("3"), 
	NORTH("4"),
	ERROR("You have entered an invalid direction");
	private String dir;

	private Direction(String dir) {
		this.dir = dir;
	}
	public String getDirection() {
		return this.dir;
	}
}
