import com.google.gson.JsonParseException;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Main {

    void main() {
        boolean running = true;
        List<Workout> workoutHistory = new ArrayList<>();


        WorkoutStorage storage = new WorkoutStorage();

        try {
            workoutHistory.addAll(storage.loadWorkouts());
            IO.println("Loaded " + workoutHistory.size() + " saved workouts.");
        } catch (IOException e) {
            IO.println("Could not load workout history: " + e.getMessage());
            IO.println("Fix the CSV file before logging new workouts.");
            return;
        }


        IO.println("Welcome!");

        while (running) {
            IO.println("\n===== Workout Analyzer =====");
            IO.println("1. Search Exercises");
            IO.println("2. Log Workout");
            IO.println("3. View Weekly Report");
            IO.println("4. Exit");

            String choice = IO.readln("Choose an option: ");

            switch (choice) {
                case "1" -> {
                    IO.println("\n===== Exercise Search =====");

                    String searchTerm = IO.readln("Enter exercise name: ").trim();
                    if (searchTerm.isEmpty()) {
                        IO.println("Please enter an exercise name.");
                        break;
                    }

                    try {
                        ExerciseApiClient apiClient = new ExerciseApiClient();
                        ExerciseParser parser = new ExerciseParser();

                        String json = apiClient.searchExercises(searchTerm);
                        List<Exercise> exercises = parser.parseExercises(json);

                        if (exercises.isEmpty()) {
                            IO.println("No matching exercises found.");
                            break;
                        }

                        IO.println("\n===== Search Results =====");

                        for (Exercise exercise : exercises) {
                            String muscles = exercise.getPrimaryMuscles().isEmpty()
                                    ? "None listed"
                                    : String.join(", ", exercise.getPrimaryMuscles());

                            IO.println("\nID: " + exercise.getId());
                            IO.println("Exercise: " + exercise.getName());
                            IO.println("Primary Muscles: " + muscles);

                        }

                        IO.println("\nShowing %d English-language exercises.".formatted(exercises.size()));

                    } catch (IOException e) {
                        IO.println("API error: " + e.getMessage());
                    } catch (InterruptedException _) {
                        Thread.currentThread().interrupt();
                        IO.println("Search interrupted.");
                    } catch (JsonParseException | IllegalStateException e) {
                        IO.println("Invalid API response: " + e.getMessage());
                    }
                }

                case "2" -> {
                    IO.println("\n===== Log Workout =====");

                    String workoutSearch = IO.readln("Enter exercise name: ").trim();
                    if (workoutSearch.isEmpty()) {
                        IO.println("Exercise name cannot be empty.");
                        break;
                    }

                    try {
                        ExerciseApiClient apiClient = new ExerciseApiClient();
                        ExerciseParser parser = new ExerciseParser();

                        String json = apiClient.searchExercises(workoutSearch);
                        List<Exercise> matches = parser.parseExercises(json);

                        if (matches.isEmpty()) {
                            IO.println("No matching exercises found.");
                            break;
                        }

                        IO.println("\nMatching exercises:");
                        for (int i = 0; i < matches.size(); i++) {
                            Exercise exercise = matches.get(i);
                            IO.println((i + 1) + ". " + exercise.getName() + " (ID: " + exercise.getId() + ")");
                        }

                        int selection = Integer.parseInt(IO.readln("\nSelect exercise number: ").trim());
                        if (selection < 1 || selection > matches.size()) {
                            IO.println("Invalid exercise selection.");
                            break;
                        }

                        Exercise selectedExercise = matches.get(selection - 1);

                        LocalDate date = LocalDate.parse(IO.readln("Date (YYYY-MM-DD): ").trim());
                        int sets = Integer.parseInt(IO.readln("Sets: ").trim());
                        int reps = Integer.parseInt(IO.readln("Reps: ").trim());
                        double weight = Double.parseDouble(IO.readln("Weight (lbs): ").trim());

                        if (sets <= 0 || reps <= 0 || !Double.isFinite(weight) || weight < 0) {
                            IO.println("Invalid workout values.");
                            break;
                        }

                        Workout workout = new Workout(date, selectedExercise, sets, reps, weight);
                        storage.saveWorkout(workout);
                        workoutHistory.add(workout);

                        IO.println("\nWorkout logged successfully!");
                        IO.println("Exercise: " + selectedExercise.getName());
                        IO.println("Date: " + date);
                        IO.println("Sets: " + sets);
                        IO.println("Reps: " + reps);
                        IO.println("Weight: " + weight + " lbs");
                        IO.println("Workouts in current session: " + workoutHistory.size());

                    } catch (NumberFormatException _) {
                        IO.println("Please enter valid numbers.");
                    } catch (DateTimeParseException _) {
                        IO.println("Invalid date. Use YYYY-MM-DD.");
                    } catch (IOException e) {
                        IO.println("API error: " + e.getMessage());
                    } catch (InterruptedException _) {
                        Thread.currentThread().interrupt();
                        IO.println("Request interrupted.");
                    } catch (JsonParseException | IllegalStateException e) {
                        IO.println("Invalid API response: " + e.getMessage());
                    }
                }

                case "3" -> {
                    IO.println("\n===== Weekly Muscle Report =====");

                    String dateInput = IO.readln("Enter a date in the week (YYYY-MM-DD), or press Enter for this week: ").trim();

                    try {
                        // Default to today's date if the user just pressed Enter
                        LocalDate selectedDate = dateInput.isEmpty()
                                ? LocalDate.now()
                                : LocalDate.parse(dateInput);

                        // Use TemporalAdjusters to cleanly find Monday and Sunday of the selected week
                        LocalDate weekStart = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                        LocalDate weekEnd = weekStart.plusDays(6);

                        MuscleAnalyzer analyzer = new MuscleAnalyzer();
                        Map<String, Integer> results = analyzer.analyzeWeek(workoutHistory, selectedDate);

                        IO.println("\nWeek: " + weekStart + " to " + weekEnd);

                        if (results.isEmpty()) {
                            IO.println("No primary muscle sets found for this week.");
                        } else {
                            IO.println("\nMuscle Group     Sets");
                            IO.println("---------------------");

                            results.forEach((muscle, sets) -> IO.println(String.format("%-16s %d", muscle, sets)));
                        }

                    } catch (DateTimeParseException e) {
                        IO.println("Invalid date. Use YYYY-MM-DD.");
                    }
                }

                case "4" -> {
                    IO.println("Goodbye!");
                    running = false;
                }

                default -> IO.println("Invalid option. Try again.");
            }
        }
    }
}