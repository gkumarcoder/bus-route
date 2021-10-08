package com.target.busroute;
/**
Application Exception class

**/
public class BusApplicationGenericException extends RuntimeException  {
	 
		private static final long serialVersionUID = -7203418875731447925L;

		public BusApplicationGenericException() {
	    }

	    public BusApplicationGenericException(String message) {
	        super(message);
	    }

}
