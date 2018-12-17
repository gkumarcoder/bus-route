Bus route application


1-Build Process(How to run this application)

First use below command to download dependencies and compile the program
      
      $mvn clean install
      eg:
![capture1](https://user-images.githubusercontent.com/25560217/49988433-ab605b80-ff9c-11e8-8b4f-989d5bfaa9f4.JPG) 
      
Then use below command to run the program
       Note(program will take three command-line argument i.e route,stop,direction)

       $java -jar bus-route-0.0.1-SNAPSHOT.jar "BUS ROUTE" "BUS STOP NAME" "DIRECTION"
       eg:
![image](https://user-images.githubusercontent.com/25560217/49987996-52dc8e80-ff9b-11e8-8640-b9cdf1d52092.png)

2-External libraries:

The libraries and imports used are as follows: 

         import java.io.*;

         import java.net.HttpURLConnection;

         import java.net.URL;

         import com.google.gson.*;

3- Test case result

![image](https://user-images.githubusercontent.com/25560217/50099349-e7691a00-0243-11e9-9f35-ae41a51dbe81.png)

         
        
