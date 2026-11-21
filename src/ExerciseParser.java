import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ExerciseParser {

    public List<Exercise> parseExercises(String json) {

        List<Exercise> exercises = new ArrayList<>();

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonArray results = root.getAsJsonArray("results");

        for (JsonElement element : results) {

            JsonObject exerciseJson = element.getAsJsonObject();

            int id = exerciseJson.get("id").getAsInt();

            // Find the English exercise name
            String name = null;
            JsonArray translations =
                    exerciseJson.getAsJsonArray("translations");

            for (JsonElement translationElement : translations) {
                JsonObject translation =
                        translationElement.getAsJsonObject();

                if (translation.get("language").getAsInt() == 2) {
                    name = translation.get("name").getAsString();
                    break;
                }
            }

            // Skip exercises without an English name
            if (name == null || name.isBlank()) {
                continue;
            }

            // Extract the primary muscle groups
            List<String> primaryMuscles = new ArrayList<>();

            JsonArray muscles = exerciseJson.getAsJsonArray("muscles");

            for (JsonElement muscleElement : muscles) {
                JsonObject muscle = muscleElement.getAsJsonObject();

                String muscleName =
                        muscle.get("name_en").getAsString();

                primaryMuscles.add(muscleName);
            }

            Exercise exercise =
                    new Exercise(id, name, primaryMuscles);

            exercises.add(exercise);
        }

        return exercises;
    }
}
