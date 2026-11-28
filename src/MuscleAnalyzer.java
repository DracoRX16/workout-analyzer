import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MuscleAnalyzer {

    /**
     * Calculates weekly set totals for each primary muscle group.
     * Weeks run from Monday through Sunday.
     *
     * Each set is credited to every primary muscle listed for
     * an exercise. Secondary muscles are not counted.
     *
     * @param workouts saved workout history
     * @param dateInWeek any date within the week being analyzed
     * @return muscle names mapped to their weekly set totals
     */

    public Map<String, Integer> analyzeWeek(List<Workout> workouts, LocalDate dateInWeek) {
        // Find the Monday at the start of the target week
        LocalDate startOfWeek = dateInWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // Sunday is always 6 days after Monday
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // A Stream lets us pass the list of workouts down an assembly line of steps
        // instead of writing nested for loops.
        return workouts.stream()
                // Keep only workouts whose date is between and including Monday and Sunday
                // Any workout outside this week gets dropped from the assembly line
                .filter(workout -> !workout.getDate().isBefore(startOfWeek)
                        && !workout.getDate().isAfter(endOfWeek))

                // Each workout can train multiple primary muscles
                // (for example, Bench Press trains ["Chest", "Triceps"]).
                // For every muscle in that list, we create a temporary 2 item pair called a
                // Map.Entry, ("Chest", 3) and ("Triceps", 3) and put all pairs
                // into one single stream.
                .flatMap(workout -> workout.getExercise().getPrimaryMuscles().stream()
                        .map(muscle -> Map.<String, Integer>entry(muscle, workout.getSets())))

                // Gather all those ("Muscle", sets) pairs into
                // our final lookup table. Collectors.toMap takes 4 instructions:
                .collect(Collectors.toMap(
                        Map.Entry::getKey,   // use the muscle name as the label
                        Map.Entry::getValue, // use the set count as the number
                        Integer::sum,        // if "Chest" appears twice, add the numbers together
                        TreeMap::new         // a TreeMap is a special Map that automatically
                        // keeps all keys sorted in alphabetical order (A to Z)
                ));
    }
}