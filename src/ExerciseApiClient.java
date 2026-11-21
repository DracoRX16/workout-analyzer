import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ExerciseApiClient {

    private static final URI API_URI =
            URI.create("https://wger.de/api/v2/exerciseinfo/?limit=2");

    public String getExercises() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder(API_URI)
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET()
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException(
                        "Server returned error code: " + response.statusCode()
                );
            }

            return response.body();
        }
    }
}