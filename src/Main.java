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
                    IO.println("Fetching exercises from wger...");
                    try {
                        ExerciseApiClient apiClient = new ExerciseApiClient();
                        String json = apiClient.getExercises();

                        IO.println("API request successful!");
                        IO.println(json);

                    } catch (java.io.IOException e) {
                        IO.println("Unable to retrieve exercises: " + e.getMessage());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        IO.println("Exercise request interrupted.");
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
