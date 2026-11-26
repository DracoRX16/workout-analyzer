import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MuscleAnalyzerTest {

    private final MuscleAnalyzer analyzer = new MuscleAnalyzer();

    @Test
    void countsSetsForMultiplePrimaryMuscles() {
        Exercise swing = new Exercise(
                9,
                "Kettlebell Swing",
                List.of("Hamstrings", "Glutes")
        );

        Workout workout = new Workout(
                LocalDate.of(2026, 11, 18),
                swing, 3, 10, 35
        );

        Map<String, Integer> result = analyzer.analyzeWeek(
                List.of(workout),
                LocalDate.of(2026, 11, 18)
        );

        assertEquals(3, result.get("Hamstrings"));
        assertEquals(3, result.get("Glutes"));
        assertEquals(2, result.size());
    }

    @Test
    void excludesWorkoutsOutsideSelectedWeek() {
        Exercise curl = new Exercise(
                100,
                "Test Curl",
                List.of("Biceps")
        );

        Workout thisWeek = new Workout(
                LocalDate.of(2026, 11, 18),
                curl, 3, 10, 20
        );

        Workout nextWeek = new Workout(
                LocalDate.of(2026, 11, 23),
                curl, 5, 10, 20
        );

        Map<String, Integer> result = analyzer.analyzeWeek(
                List.of(thisWeek, nextWeek),
                LocalDate.of(2026, 11, 18)
        );

        assertEquals(3, result.get("Biceps"));
    }

    @Test
    void returnsEmptyMapWhenNoWorkoutsExist() {
        Map<String, Integer> result = analyzer.analyzeWeek(
                List.of(),
                LocalDate.of(2026, 11, 18)
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void combinesSetsFromMultipleWorkouts() {
        Exercise curl = new Exercise(
                100,
                "Test Curl",
                List.of("Biceps")
        );

        Workout first = new Workout(
                LocalDate.of(2026, 11, 16),
                curl, 3, 10, 20
        );

        Workout second = new Workout(
                LocalDate.of(2026, 11, 20),
                curl, 4, 12, 25
        );

        Map<String, Integer> result = analyzer.analyzeWeek(
                List.of(first, second),
                LocalDate.of(2026, 11, 18)
        );

        assertEquals(7, result.get("Biceps"));
    }
}
