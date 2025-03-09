package edu.farmingdale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Weather Data Analyzer Application.
 *
 * This app reads weather data from a CSV file and analyzes it.
 *
 * Features:
 * - Reads date, temperature, humidity, and rain data from a CSV file.
 * - Calculates the average temperature for a given month.
 * - Finds days with temperatures above a certain value.
 * - Counts the number of rainy days.
 * - Classifies weather as "Hot", "Warm", or "Cold" using a switch statement.
 * - Uses pattern matching in switch for an extra method.
 *
 */

public record WeatherDataAnalyzer() {

    /**
     * A simple record to hold weather data.
     *
     * @param date the date of the record
     * @param temperature the temperature in Celsius
     * @param humidity the humidity percentage
     * @param precipitation the rain amount in mm
     */
    public record WeatherData(LocalDate date, double temperature, double humidity, double precipitation) { }

    /**
     * Main method that runs the app.
     * It loads the CSV data, does the analysis, and prints a summary.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        List<WeatherData> records = loadData("weatherdata.csv");

        // Calculate average temperature for August (month 8)
        double avgTemp = averageTemperature(records, 8);

        // Find days with temperature above 30°C
        List<WeatherData> hotDays = daysAboveTemperature(records, 30);

        // Count days with rain (precipitation > 0)
        long rainyDays = countRainyDays(records);

        // Create a summary message using a text block
        String summary = """
            Weather Data Summary:
            ---------------------
            Average Temperature for August: %.2f°C
            Number of days above 30°C: %d
            Number of rainy days: %d
            """.stripIndent().formatted(avgTemp, hotDays.size(), rainyDays);

        System.out.println(summary);
    }

    /**
     * Loads weather data from a CSV file in the resources folder.
     *
     * @param filename the name of the CSV file
     * @return a list of weather records
     */
    public static List<WeatherData> loadData(String filename) {
        try (InputStream is = WeatherDataAnalyzer.class.getResourceAsStream("/" + filename)) {
            if (is == null) {
                System.err.println("File not found: " + filename);
                return Collections.emptyList();
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines()
                        .skip(1) // skip the header line
                        .map(line -> {
                            String[] tokens = line.split(",");
                            return new WeatherData(
                                    LocalDate.parse(tokens[0], DateTimeFormatter.ISO_LOCAL_DATE),
                                    Double.parseDouble(tokens[1]),
                                    Double.parseDouble(tokens[2]),
                                    Double.parseDouble(tokens[3])
                            );
                        })
                        .collect(Collectors.toList());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Calculates the average temperature for a specific month.
     *
     * @param data list of weather records
     * @param month month number (1-12)
     * @return average temperature or NaN if no records exist for the month
     */
    public static double averageTemperature(List<WeatherData> data, int month) {
        return data.stream()
                .filter(w -> w.date().getMonthValue() == month)
                .mapToDouble(WeatherData::temperature)
                .average()
                .orElse(Double.NaN);
    }

    /**
     * Finds the list of days with temperature above a given value.
     *
     * @param data list of weather records
     * @param threshold temperature threshold
     * @return list of records with temperature above the threshold
     */
    public static List<WeatherData> daysAboveTemperature(List<WeatherData> data, double threshold) {
        return data.stream()
                .filter(w -> w.temperature() > threshold)
                .collect(Collectors.toList());
    }

    /**
     * Counts the number of days with rain (precipitation > 0).
     *
     * @param data list of weather records
     * @return count of rainy days
     */
    public static long countRainyDays(List<WeatherData> data) {
        return data.stream()
                .filter(w -> w.precipitation() > 0)
                .count();
    }

    /**
     * Determines the weather category based on temperature using a switch.
     *
     * @param temperature temperature in Celsius
     * @return "Hot" if temperature ≥ 35°C, "Warm" if between 25°C and 35°C, else "Cold"
     */
    public static String weatherCategory(double temperature) {
        return switch ((int) temperature) {
            default -> {
                if (temperature >= 35) yield "Hot";
                else if (temperature >= 25) yield "Warm";
                else yield "Cold";
            }
        };
    }

    /**
     * Uses pattern matching in a switch to return a description if the object is a WeatherData record.
     *
     * @param obj an object that might be a weather record
     * @return a simple description if obj is a WeatherData record, otherwise "Unknown record type"
     */
    public static String getRecordDescription(Object obj) {
        return switch (obj) {
            case WeatherData wd -> "Record for date: " + wd.date();
            default -> "Unknown record type";
        };
    }
}
