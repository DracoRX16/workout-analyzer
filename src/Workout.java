
import java.time.LocalDate;

public class Workout {

    private final LocalDate date;
    private final Exercise exercise;
    private final int sets;
    private final int reps;
    private final double weight;

    public Workout(LocalDate date, Exercise exercise,
                   int sets, int reps, double weight) {
        this.date = date;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public LocalDate getDate() {
        return date;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }
}
