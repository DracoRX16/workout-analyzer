public class Main {

    public static void main(String[] args) {

        boolean running = true;

        IO.println("Welcome!");

        while (running) {
            IO.println("\nWorkout Analyzer");
            IO.println("1. Search Exercises");
            IO.println("2. Log Workout");
            IO.println("3. View Weekly Report");
            IO.println("4. Exit");
            IO.print("Choose an option: ");

            String choice = IO.readln();

            switch (choice) {
                case "1" -> {
                    IO.println("\n===== Exercise Search =====");

                    try {
                        ExerciseApiClient apiClient = new ExerciseApiClient();
                        ExerciseParser parser = new ExerciseParser();

                        String json = apiClient.getExercises();

                        java.util.List<Exercise> exercises =
                                parser.parseExercises(json);

                        if (exercises.isEmpty()) {
                            IO.println("No English-language exercises found.");
                        }

                        for (Exercise exercise : exercises) {
                            IO.println("\nID: " + exercise.getId());
                            IO.println("Exercise: " + exercise.getName());
                            IO.println(
                                    "Primary Muscles: " +
                                            (exercise.getPrimaryMuscles().isEmpty()
                                                    ? "None listed"
                                                    : String.join(", ",
                                                    exercise.getPrimaryMuscles()))
                            );
                        }

                    } catch (java.io.IOException e) {
                        IO.println("API error: " + e.getMessage());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        IO.println("Request interrupted.");
                    } catch (com.google.gson.JsonParseException |
                             IllegalStateException e) {
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
