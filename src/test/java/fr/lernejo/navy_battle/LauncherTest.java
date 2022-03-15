package fr.lernejo.navy_battle;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LauncherTest {

    @Test
    void PingTests() throws IOException, InterruptedException {
        String[] args = {"9876"};
        Launcher.main(args);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9876/ping"))
            .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        Assertions.assertThat(response.statusCode()).isEqualTo(200);
        Assertions.assertThat(response.body()).isEqualTo("OK");
    }

    @Test
    void StartTests() throws IOException, InterruptedException {
        String[] args = {"1234"};
        String my_id = "TEst.IT";
        String my_port = "8765";
        Launcher.main(args);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requetePost = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:1234/api/game/start"))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"" + my_id + "\", \"url\":\"http://localhost:" + my_port + "\", \"message\":\"hello\"}"))
            .build();
        HttpResponse<String> response = client.send(requetePost, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StartMessage jsonMap = objectMapper.readValue(response.body(), StartMessage.class);

        Assertions.assertThat(response.statusCode()).isEqualTo(202);
        Assertions.assertThat(jsonMap.message()).isEqualTo("May the best code win");
        Assertions.assertThat(jsonMap.url()).isEqualTo("http://localhost:1234");
        try{
            UUID uuid = UUID.fromString(jsonMap.id());
        } catch (IllegalArgumentException exception){
            Assertions.assertThat(exception).isEqualTo(null);
        }
    }
}
