import com.google.gson.JsonParseException;
import java.io.IOException;
import java.util.List;

public class Main {

    void main() {
        boolean running = true;

        IO.println("Welcome!");

        while (running) {
            IO.println("\nWorkout Analyzer");
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

                case "2" -> IO.println("Workout logging coming soon!");

                case "3" -> IO.println("Weekly report coming soon!");

                case "4" -> {
                    IO.println("Goodbye!");
                    running = false;
                }

                default -> IO.println("Invalid option. Try again.");
            }
        }
    }
}