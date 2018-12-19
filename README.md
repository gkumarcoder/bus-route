# Bus route application


## Build Process(How to run this application)

First use below command to download dependencies and compile the program
      
      $mvn clean install
      eg:
![capture1](https://user-images.githubusercontent.com/25560217/49988433-ab605b80-ff9c-11e8-8b4f-989d5bfaa9f4.JPG) 
      
Then use below command to run the program
       Note(program will take three command-line argument i.e route,stop,direction)

       $java -jar bus-route-0.0.1-SNAPSHOT.jar "BUS ROUTE" "BUS STOP NAME" "DIRECTION"
       eg:java -jar bus-route-0.0.1-SNAPSHOT.jar 5 77PO north
![image](https://user-images.githubusercontent.com/25560217/49987996-52dc8e80-ff9b-11e8-8640-b9cdf1d52092.png)

## External libraries:

The libraries and imports used are as follows: 

         import java.io.*;

         import java.net.HttpURLConnection;

         import java.net.URL;

         import com.google.gson.*;

## Test case result

  ### positive case
![image](https://user-images.githubusercontent.com/25560217/50099349-e7691a00-0243-11e9-9f35-ae41a51dbe81.png)

  ### Negative case

![image](https://user-images.githubusercontent.com/25560217/50239394-0f917e00-03e8-11e9-9495-84aaa25379c9.png)
        
