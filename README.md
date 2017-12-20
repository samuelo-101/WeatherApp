# WeatherApp

GENERAL INFORMATION
------------------------------------------------------------
This program generates weather information for various locations, including:
- Location name
- Latitude, Longitude, Elevation
- Weather Condition
- Pressure
- Humidity
- Temperature

NOTE: The data generated is NOT intended to be correct/accurate with respect to meteorology. The intention is to generate data that is plausible in the eyes of a layman. 

USAGE INFORMATION
------------------------------------------------------------
- This program requires Java version 1.8+ to build and run
- The code can be built and modified on any OS. however, to run the built jar, you need any popular version of Linux or Mac
- In the 'scripts' folder there is a file called 'run.sh' and also a jar file 'weatherapp.jar'
- To run, navigate into the scripts folder using your terminal, and type './run.sh' (without the single quotes)
- The program will, by default, generate 20 rows of data. To specify the number of records to be generated, provide an integer value after the file name. Eg: './run.sh 45' will generate 45 records