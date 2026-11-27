import java.util.List;

/**
 * Represents a physical exercise with an identifier, name, and targeted muscle groups.
 */

public class Exercise {

    private final int id;
    private final String name;
    private final List<String> primaryMuscles;

    public Exercise(int id, String name, List<String> primaryMuscles) {
        this.id = id;
        this.name = name;
        // Make a defensive copy so external changes to the original
        // list cannot alter the exercise's primary muscle groups.
        this.primaryMuscles = List.copyOf(primaryMuscles);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getPrimaryMuscles() {
        return primaryMuscles;
    }
}
