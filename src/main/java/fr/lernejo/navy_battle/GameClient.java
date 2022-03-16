package fr.lernejo.navy_battle;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class GameClient {
    public static void StartClient(String adv_url, Map<String, String> gameContext) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requetePost = HttpRequest.newBuilder()
            .uri(URI.create(adv_url + "/api/game/start"))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"" + gameContext.get("my_id") + "\", \"url\":\"http://localhost:" + gameContext.get("my_port") + "\", \"message\":\"hello\"}"))
            .build();
        HttpResponse<String> response = client.send(requetePost, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StartMessage jsonMap = objectMapper.readValue(response.body(), StartMessage.class);
        gameContext.put("adv_id", jsonMap.id());
    }

    public static void FireClient(String adv_url, String pos) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
//        System.out.println(adv_url);
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(URI.create(adv_url + "/api/game/fire?cell=" + pos))
            .setHeader("Content-Type", "application/json")
            .GET()
            .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }

}
