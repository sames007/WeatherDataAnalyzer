🌦️ Weather Data Analyzer
This is a simple Java application that reads weather data from a CSV file and performs basic analysis.

📋 What It Does
This app reads data like date, temperature, humidity, and precipitation from a CSV file and provides the following:

✅ Calculates the average temperature for a specific month

🔥 Lists all days with temperature above a certain value

🌧️ Counts the number of rainy days

🌡️ Classifies each day's weather as Hot, Warm, or Cold

🔍 Uses modern Java features like records, text blocks, and pattern matching in switch statements

📁 Sample CSV Format
The app expects a CSV file like this in the resources folder:
Date,Temperature,Humidity,Precipitation
2023-08-01,32.5,65,0.0
2023-08-02,35.0,60,0.2

🚀 How to Run
Make sure you have Java 17 or later installed.

Place your weatherdata.csv file in the resources directory.

Run the main() method in WeatherDataAnalyzer.java.

🧠 Key Java Features Used
record: To define simple data holders.

Stream API: For filtering and summarizing data.

switch with pattern matching: For cleaner and modern conditional logic.

Text Blocks: For clean and readable multi-line output.

📞 Example Output
Weather Data Summary:
---------------------
Average Temperature for August: 33.75°C
Number of days above 30°C: 2
Number of rainy days: 1
