# Workout Analyzer

## Project Goal

This is a Java-based workout tracking application designed to help users understand how evenly they train different muscle groups.

The application uses the wger Exercise API to retrieve exercise information, allows users to log their workouts, and analyzes weekly training patterns.

## MVP Features

- Search for exercises using the wger API
- View exercise names and primary muscles
- Log workouts with sets, repetitions, and weight
- Save workout history locally in CSV format
- Generate weekly muscle-group training reports

## Technology Stack

- **Language:** Java
- **API:** wger API
- **HTTP:** Java HttpClient
- **JSON Parsing:** Gson
- **Storage:** CSV files
- **Version Control:** Git and GitHub

## Future Features

- Graphical dashboard
- Strength progression tracking
- Monthly training comparisons
- Customizable muscle-group targets

## Project Status

In development

## Running the Project

Compile:

`javac src/Main.java`

Run:

`java -cp src Main`

## External API

Exercise information is provided by the wger API:

https://wger.de/api/v2/