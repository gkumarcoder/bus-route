package com.target.busroute;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 
 * @author gkumar3
 *
 */
public class BusRouteInfoService {
	
	private final static Logger logger = LoggerFactory.getLogger(BusRouteInfoService.class);
	
	public static URL url = null;
	
    public static String timeStamp = StringUtils.EMPTY;
    
    public static String stop;
    
    public static HttpURLConnection request;
    
    /**
     * This method will receive a Json Array and extract the element from it
     * 
     * @param Url
     * @return
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    public static JsonElement getHttpConnection(String Url) throws MalformedURLException, ProtocolException,IOException {
            url = new URL(Url);
            request = (HttpURLConnection)url.openConnection();
            request.setDoOutput(true);
            request.setRequestMethod("GET");
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement element = jp.parse(new InputStreamReader((InputStream)request.getInputStream()));
            if (request.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                System.out.println(request.getErrorStream());
            }
      return element;
}
    
    /**
     * Set the direction to the expected output to correctly match the JSON element
     *     
     * @param dir
     * @return
     */
	public  static String validateDirection(String dir) {
		if(dir.equalsIgnoreCase(Direction.EAST.name()))
			return Direction.EAST.getDirection();
		else if(dir.equalsIgnoreCase(Direction.WEST.name()))
			return Direction.WEST.getDirection();
		else if(dir.equalsIgnoreCase(Direction.NORTH.name()))
			return Direction.NORTH.getDirection();
		else if(dir.equalsIgnoreCase(Direction.SOUTH.name()))
			return Direction.SOUTH.getDirection();
		return Direction.ERROR.getDirection();
	}
	
	/**
	 * 
	 * @param Url
	 * @param ElementOne
	 * @param ElementTwo
	 * @param compareString
	 * @return
	 */
    public static String getBusRouteStop(String Url, String argOne, String argTwo, String compareString)
    {
    	try {
    	JsonElement jsonElement=getHttpConnection(Url);
     
            JsonArray jsonArrayObj = jsonElement.getAsJsonArray();

            for (JsonElement obj : jsonArrayObj)
            {
                if(obj.getAsJsonObject().get(argTwo).getAsString().contains(compareString))
                {
                    if(compareString.equals(stop))
                    {
                    	return obj.getAsJsonObject().get(argTwo).getAsString();
                        
                    }
                    return obj.getAsJsonObject().get(argTwo).getAsString();
                }
            }
        }
        catch(IOException e)
        {
        	logger.error("Caused an IOException {}" , e.getMessage());
        }
        return "-1";
    }

    public static String  getBusRouteTimeStamp(String Url, String argOne, String argTwo, String compareString)
    {
    	try {
    	JsonElement jsonElement=getHttpConnection(Url);
     
            JsonArray jsonArrayObj = jsonElement.getAsJsonArray();

            for (JsonElement obj : jsonArrayObj)
            {
                if(obj.getAsJsonObject().get(argOne).getAsString().contains(compareString))
                {
                	if(compareString.equals(timeStamp))
		    		{
                		return obj.getAsJsonObject().get(argTwo).getAsString();
		    		}
		    		return obj.getAsJsonObject().get(argTwo).getAsString();
                }
            }
        }
        catch(IOException e)
        {
        	logger.error("Caused an IOException {}" , e.getMessage());
        }
        return "-1";
    }
    
    public static int getBusRouteId(String Url, String argOne, String argTwo, String compareString)
    {
    	try {
    	JsonElement jsonElement=getHttpConnection(Url);
     
            JsonArray jsonArrayObj = jsonElement.getAsJsonArray();

            for (JsonElement obj : jsonArrayObj)
            {
                if(obj.getAsJsonObject().get(argOne).getAsString().contains(compareString))
                {
                    return obj.getAsJsonObject().get(argTwo).getAsInt();
                }
            }
        }
        catch(IOException e)
        {
        	logger.error("Caused an IOException {}" , e.getMessage());
        }
       return -1;
    }
    /**
     * Verifies the direction and if found matched direction return the valid ID
     * 
     * @param Url
     * @param argOne
     * @param argTwo
     * @param compareString
     * @return
     */
    public static int getBusRouteDirection(String Url, String argOne, String argTwo, String compareString)
    {
    	try {
    	JsonElement jsonElement=getHttpConnection(Url);
     
            JsonArray jsonArrayObj = jsonElement.getAsJsonArray();

            for (JsonElement obj : jsonArrayObj)
            {
                if(obj.getAsJsonObject().get(argTwo).getAsString().contains(compareString))
                {
                    return obj.getAsJsonObject().get(argTwo).getAsInt();
                }
            }
        }
        catch(IOException e)
        {
        	logger.error("Caused an IOException {}" , e.getMessage());
        }
        return -1;
    }
    /**
     * Computes the time based on the UTC value given through the GetTimepointDepartures function
     */
	public static void computeTime(String timeStamp) {
		try {
			timeStamp = timeStamp.substring(6, 19);

			Long longTime = Long.valueOf(timeStamp).longValue();
			Date currentDate = new Date();

			// compute the difference between the current time and the departure time
			// (longTime). Divide by 60000 to account for milliseconds and minutes
			long timeTillBus = (longTime - currentDate.getTime()) / 60000;
			if (timeTillBus > 1) {
				System.out.println(timeTillBus + " minutes");
			} else if (timeTillBus == 0) {
				// no printout for the time
			} else {
				System.out.println(timeTillBus + " minute");
			}
		} catch (Exception e) {
			logger.error("timeStamp is not properly mapped {}" , e.getMessage());
		}
	}


}
