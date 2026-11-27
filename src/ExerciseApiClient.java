import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class ExerciseApiClient {

    private static final String BASE_URL =
            "https://wger.de/api/v2/exerciseinfo/";

    public String searchExercises(String searchTerm)
            throws IOException, InterruptedException {

        // Encode user input so spaces and special characters
        // can be safely included in the API request URL.
        String encodedTerm = URLEncoder.encode(
                searchTerm.trim(), StandardCharsets.UTF_8
        );

        // Search English exercise names and limit results to
        // 20 entries to keep the command-line output manageable.
        String url = BASE_URL
                + "?name__search=" + encodedTerm
                + "&language__code=en"
                + "&limit=20";

        // Avoid waiting indefinitely if the API is slow or unavailable.
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET()
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new IOException(
                        "Server returned HTTP " + response.statusCode()
                );
            }

            return response.body();
        }
    }
}
