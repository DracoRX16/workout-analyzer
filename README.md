# Workout Analyzer

A Java command line application that allows users to log workouts and analyze how their weekly training sets are distributed across different muscle groups.

## Project Overview

Workout Analyzer helps users identify which muscle groups they have trained during a given week and how their logged training volume is distributed.

The application retrieves exercise information from the public wger REST API, lets users record workout details, stores their workout history locally in a CSV file, and generates weekly muscle-group reports.

## Features

- **Exercise Search:** Search for exercises by name using the wger REST API.
- **Exercise Information:** View exercise IDs, names, and primary muscle groups.
- **Workout Logging:** Record an exercise, date, sets, repetitions, and weight in pounds.
- **CSV Storage:** Save workout history locally and load it when the application restarts.
- **Weekly Muscle Analysis:** Calculate logged set totals for each primary muscle group.
- **Input Validation:** Handle invalid dates, exercise selections, and workout values.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 27 | Application development |
| Java HttpClient | HTTP requests to the wger API |
| Gson | JSON parsing |
| wger REST API | Exercise information |
| CSV | Persistent local workout storage |
| JUnit 5 | Automated unit testing |
| IntelliJ IDEA | Development and test execution |
| Git and GitHub | Version control |

## Project Structure

```text
workout-analyzer/
├── src/
│   ├── Main.java
│   ├── Exercise.java
│   ├── ExerciseApiClient.java
│   ├── ExerciseParser.java
│   ├── Workout.java
│   ├── WorkoutStorage.java
│   └── MuscleAnalyzer.java
├── test/
│   └── MuscleAnalyzerTest.java
├── lib/
│   └── gson-2.15.0.jar
├── data/
│   └── .gitkeep
├── docs/
│   └── .gitkeep
├── .gitignore
└── README.md
```

## Installation and Setup

### Requirements

- Java 21 or newer
- IntelliJ IDEA
- Internet access for exercise searches
- Gson 2.15.0
- JUnit 5 for running automated tests

### 1. Clone the repository

```bash
git clone https://github.com/DracoRX16/workout-analyzer.git
cd workout-analyzer
```

### 2. Compile the application

From the repository root, run:

```bash
javac -cp "lib/gson-2.15.0.jar" src/*.java
```

### 3. Run Workout Analyzer

On macOS or Linux:

```bash
java -cp "src:lib/gson-2.15.0.jar" Main
```

On Windows:

```bash
java -cp "src;lib/gson-2.15.0.jar" Main
```

Alternatively, open the project in IntelliJ, add the Gson JAR to the module dependencies, and run `Main.java`.

## How to Use

The application displays a command-line menu:

```text
===== Workout Analyzer =====
1. Search Exercises
2. Log Workout
3. View Weekly Report
4. Exit
```

### Search Exercises

Select option 1 and enter an exercise name, such as `squat` or `curl`.

The application searches wger and displays matching exercise names, IDs, and primary muscle groups.

### Log Workout

Select option 2, search for an exercise, and choose one of the results.

Enter the workout date, sets, repetitions, and weight. The workout is saved to `data/workouts.csv`.

### View Weekly Report

Select option 3 to generate a muscle-group report.

Press Enter to analyze the current week or enter a date in `YYYY-MM-DD` format to select another week.

Weeks run from Monday through Sunday.

## How Muscle-Group Analysis Works

Workout Analyzer counts logged sets for each exercise's primary muscle groups.

For example, if an exercise targets both Hamstrings and Glutes and the user logs three sets, the report credits:

- Hamstrings: 3 sets
- Glutes: 3 sets

Set credits are assigned to every listed primary muscle. Secondary muscle groups are excluded.

These values represent logged training volume based on the API's muscle classifications. They are not measurements of muscle activation or training effectiveness.

## Data Storage

Workout history is stored locally in:

```text
data/workouts.csv
```

Each record includes:

- Workout date
- Exercise ID
- Exercise name
- Primary muscle groups
- Sets
- Repetitions
- Weight

Saved muscle-group information also allows the weekly report to run without requesting the exercises again.

## Testing

Workout Analyzer uses **JUnit 5** for automated unit testing.

Tests are located in:

```text
test/MuscleAnalyzerTest.java
```

### Current JUnit Tests

| Test | Purpose |
|---|---|
| `countsSetsForMultiplePrimaryMuscles` | Verifies that every primary muscle receives the correct set credit |
| `excludesWorkoutsOutsideSelectedWeek` | Ensures workouts from another week are excluded |
| `returnsEmptyMapWhenNoWorkoutsExist` | Checks that an empty workout history produces an empty report |
| `combinesSetsFromMultipleWorkouts` | Verifies that sets from multiple workouts are added together |

**Test result:** All four tests passed.


## Limitations

- The application uses a command-line interface.
- Exercise searches require internet access.
- Search results are limited to the first page returned by the API.
- The analysis only considers primary muscle groups.
- Weekly totals are based on logged sets rather than exercise intensity or effectiveness.
- Workout data is stored locally rather than synchronized across devices.

## Future Improvements

- Graphical dashboard and charts
- Monthly muscle group comparisons
- Strength progression tracking
- Customizable training targets
- Exercise search pagination
- Additional automated tests for CSV storage and API parsing

## Project Status

**Core MVP implemented.** Final testing, code cleanup, and documentation are in progress.

## API Acknowledgment

Exercise data is provided by the [wger REST API](https://wger.de/).

## Repository

https://github.com/DracoRX16/workout-analyzer