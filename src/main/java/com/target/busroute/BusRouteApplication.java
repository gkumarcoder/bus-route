package com.target.busroute;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import com.google.gson.*;
/**
 * 
 */

public class BusRouteApplication {

        public static String route;
        public static String stop;
        public static String direction;

        public static int time;
        public static int routeID;
        public static int directionID;
        public static String dir;
        public static String urlString;
        public static String stopID = "";
        public static String timeStamp = "";
        public static URL url = null;

        /**
         * The main function of the program. Used primarily to make quick computations and then send off input to other functions
         * @param args
         * @throws IOException
         */
        public static void main(String[] args) throws IOException
        {
            //Check to make sure the program is run with three arguments
            if (args.length != 3)
            {
                System.out.println("Please enter the correct number of arguments.");
                System.exit(-1);
            }

            //Set the arguments for the route, stop and direction to later fetch the id tags
            route = args[0];
            stop = args[1];
            direction = args[2];

            //Set the direction to the expected output to correctly match the JSON element
            if(direction.equals("north"))
            {
                dir = "NORTHBOUND";
            }
            else if(direction.equals("south"))
            {
                dir = "SOUTHBOUND";
            }
            else if(direction.equals("east"))
            {
                dir = "EASTBOUND";
            }
            else if (direction.equals("west"))
            {
                dir = "WESTBOUND";
            }
            else
            {
                //Bad Input
                System.out.println("The direction that was entered was invalid");
                System.exit(-1);
            }

            //Find the route ID for the specific route that was entered and return the ID
            urlString = "http://svc.metrotransit.org/NexTrip/Routes?format=json";
            routeID = FetchInformation(urlString, "Description", "Route", route);

            //error checking to make sure we should continue
            if(routeID == -1)
            {
                System.out.println(route + " was not found.");
                System.exit(-1);
            }

            //Find the direction ID for the specific direction that was entered and return the ID
            urlString = "http://svc.metrotransit.org/NexTrip/Directions/"+ routeID + "?format=json";
            directionID = FetchInformation(urlString, "Text", "Value", dir);

            //error checking to make sure we should continue
            if(directionID == -1)
            {
                System.out.println(direction + " is incorrect for this route.");
                System.exit(-1);
            }

            //Find the stop ID String and set in global variable
            urlString = "http://svc.metrotransit.org/NexTrip/Stops/" + routeID + "/"+ directionID +"?format=json";
            FetchInformation(urlString, "Text", "Value", stop);

            //error checking to make sure we should continue
            if(stopID.equals(""))
            {
                System.out.println(stop + " was not found.");
                System.exit(-1);
            }

            //Find the timeStamp String and set in global variable
            urlString = "http://svc.metrotransit.org/NexTrip/" + routeID + "/"+ directionID + "/" + stopID +"?format=json";
            FetchInformation(urlString, "RouteDirection", "DepartureTime", timeStamp);

            //error checking to make sure we should continue
            if(timeStamp.equals(""))
            {
                //The specification says that if the last bus of the day has already left to not return anything. So I exit clean here.
                System.exit(0);
            }

            //compute the time and print the result
            ComputeTime();
        }

        /**
         * Computes the time based on the UTC value given through the GetTimepointDepartures function
         */
        public static void ComputeTime()
        {
            //get the portion of the timeStamp we need and convert it to a long
            timeStamp = timeStamp.substring(6,19);
            Long longTime = Long.valueOf(timeStamp).longValue();
            Date currentDate = new Date();

            //take the difference between the current time and the departure time (longTime). Divide by 60000 to account for milliseconds and minutes
            long timeTillBus = (longTime-currentDate.getTime())/60000;
            if(timeTillBus > 1)
            {
                System.out.println(timeTillBus + " minutes");
            }
            else if (timeTillBus == 0)
            {
                //no printout for the time
            }
            else
            {
                System.out.println(timeTillBus + " minute");
            }
        }

        /**
         * @param Url 				the full String url where we are sending the GET request to in order to recieve a Json object
         * @param ElementOne		used to search through the json object: RouteDirection, Text or Description
         * @param ElementTwo		used to search through the json object: DepartureTime, Value, Route
         * @param compareString		used to ensure we return a String when using this method: route, dir, stop, timeStamp
         * @return					the int value we are searching for, -1 if FetchInformation fails and 0 if we return a string
         */
        public static int FetchInformation(String Url, String ElementOne, String ElementTwo, String compareString)
        {
            //set up a connection to get ready to send a GET request to the url that is passed in
            HttpURLConnection request;
            try
            {
                url = new URL(Url);
                request = (HttpURLConnection)url.openConnection();
                request.setDoOutput(true);
                request.setRequestMethod("GET");

                //we will recieve a Json Array and extract the element from it
                request.connect();
                JsonParser jp = new JsonParser();
                JsonElement element = jp.parse(new InputStreamReader((InputStream)request.getInputStream()));

                //check to make sure our data will be valid before parsing commences
                if (request.getResponseCode() != HttpURLConnection.HTTP_OK)
                {
                    System.out.println(request.getErrorStream());
                }

                JsonArray jsonArrayObj = element.getAsJsonArray();

                //parse the elements in the array and return either an int or a string depending on input
                for (JsonElement obj : jsonArrayObj)
                {
                    if(obj.getAsJsonObject().get(ElementOne).getAsString().contains(compareString))
                    {
                        if(compareString.equals(stop))
                        {
                            stopID = obj.getAsJsonObject().get(ElementTwo).getAsString();
                            return 0;
                        }
                        if(compareString.equals(timeStamp))
                        {
                            timeStamp = obj.getAsJsonObject().get(ElementTwo).getAsString();
                            return 0;
                        }
                        return obj.getAsJsonObject().get(ElementTwo).getAsInt();
                    }
                }
            }
            catch(IOException e)
            {
                System.out.println("Caused an IOException");
                e.printStackTrace();
            }
            return -1;
        }
    }
