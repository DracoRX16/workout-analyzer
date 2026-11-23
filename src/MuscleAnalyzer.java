import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MuscleAnalyzer {

    /**
     * Counts how many sets were performed for each primary muscle during the
     * Monday-to-Sunday week containing the given date.
     *
     * What is a Map<String, Integer>?
     * The "Key" (String) is the unique label you look up; here, a muscle name like "Chest".
     * The "Value" (Integer) is the data stored next to that label; here, the total sets like 12.
     * Unlike a List (where you look things up by index 0, 1, 2...), a Map lets you look up
     * a count directly by the muscle's name, and it prevents duplicate muscle names.
     */
    public Map<String, Integer> analyzeWeek(List<Workout> workouts, LocalDate dateInWeek) {
        // Find the Monday at the start of the target week
        LocalDate startOfWeek = dateInWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // Sunday is always 6 days after Monday
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // A "Stream" lets us pass the list of workouts down an assembly line of steps
        // instead of writing nested for-loops.
        return workouts.stream()
                // Keep only workouts whose date is between and including Monday and Sunday
                // Any workout outside this week gets dropped from the assembly line
                .filter(workout -> !workout.getDate().isBefore(startOfWeek)
                        && !workout.getDate().isAfter(endOfWeek))

                // Each workout can train multiple primary muscles
                // (for example, Bench Press trains ["Chest", "Triceps"]).
                // For every muscle in that list, we create a temporary 2-item pair called a
                // Map.Entry e.g., ("Chest", 3) and ("Triceps", 3) and put all pairs
                // into one single stream.
                .flatMap(workout -> workout.getExercise().getPrimaryMuscles().stream()
                        .map(muscle -> Map.<String, Integer>entry(muscle, workout.getSets())))

                // Gather all those ("Muscle", sets) pairs into
                // our final lookup table. Collectors.toMap takes 4 instructions:
                .collect(Collectors.toMap(
                        Map.Entry::getKey,   // Key: use the muscle name as the label
                        Map.Entry::getValue, // Value: use the set count as the number
                        Integer::sum,        // Merge rule: if "Chest" appears twice, add the numbers together
                        TreeMap::new         // Map type: a TreeMap is a special Map that automatically
                        //keeps all keys sorted in alphabetical order (A to Z)
                ));
    }
}