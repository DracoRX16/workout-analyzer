import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkoutStorage {

    private static final Path FILE = Path.of("data", "workouts.csv");
    private static final String HEADER = "date,exerciseId,exerciseName,primaryMuscles,sets,reps,weight";

    public void saveWorkout(Workout workout) throws IOException {
        Objects.requireNonNull(workout, "workout must not be null");

        Path parent = FILE.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        boolean needsHeader = Files.notExists(FILE) || Files.size(FILE) == 0;
        Exercise exercise = workout.getExercise();

        String muscles = String.join("|", exercise.getPrimaryMuscles());
        String row = String.join(",",
                workout.getDate().toString(),
                String.valueOf(exercise.getId()),
                escape(exercise.getName()),
                escape(muscles),
                String.valueOf(workout.getSets()),
                String.valueOf(workout.getReps()),
                String.valueOf(workout.getWeight())
        );

        List<String> linesToWrite = needsHeader ? List.of(HEADER, row) : List.of(row);

        Files.write(FILE, linesToWrite, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public List<Workout> loadWorkouts() throws IOException {
        if (Files.notExists(FILE)) {
            return List.of();
        }

        List<String> lines = Files.readAllLines(FILE);
        List<Workout> workouts = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }

            try {
                workouts.add(parseWorkout(line));
            } catch (RuntimeException e) {
                throw new IOException("Invalid workout CSV at line " + (i + 1) + ": " + e.getMessage(), e);
            }
        }

        return List.copyOf(workouts);
    }

    private Workout parseWorkout(String line) {
        List<String> fields = parseCsvLine(line);
        if (fields.size() != 7) {
            throw new IllegalArgumentException("Expected 7 CSV fields, got " + fields.size());
        }

        LocalDate date = LocalDate.parse(fields.get(0));
        int exerciseId = Integer.parseInt(fields.get(1));
        String exerciseName = fields.get(2);

        List<String> muscles = fields.get(3).isEmpty()
                ? List.of()
                : List.of(fields.get(3).split("\\|"));

        int sets = Integer.parseInt(fields.get(4));
        int reps = Integer.parseInt(fields.get(5));
        double weight = Double.parseDouble(fields.get(6));

        if (sets <= 0 || reps <= 0 || !Double.isFinite(weight) || weight < 0) {
            throw new IllegalArgumentException("Invalid workout values");
        }

        Exercise exercise = new Exercise(exerciseId, exerciseName, muscles);
        return new Workout(date, exercise, sets, reps, weight);
    }

    private String escape(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        if (inQuotes) {
            throw new IllegalArgumentException("Unclosed quoted field");
        }

        fields.add(current.toString());
        return List.copyOf(fields);
    }
}