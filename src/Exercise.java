import java.util.List;

public class Exercise {

    private final int id;
    private final String name;
    private final List<String> primaryMuscles;

    public Exercise(int id, String name, List<String> primaryMuscles) {
        this.id = id;
        this.name = name;
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
