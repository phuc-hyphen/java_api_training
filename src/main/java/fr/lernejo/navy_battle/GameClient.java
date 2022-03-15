package fr.lernejo.navy_battle;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GameClient {
    public static void StartClient(String my_id, String my_port, String adv_url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requetePost = HttpRequest.newBuilder()
            .uri(URI.create(adv_url + "/api/game/start"))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"" + my_id + "\", \"url\":\"http://localhost:" + my_port + "\", \"message\":\"hello\"}"))
            .build();
        HttpResponse<String> response = client.send(requetePost, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }

    public static void FireClient(String adv_url,String pos) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(URI.create(adv_url + "/api/game/fire?cell=" + pos))
            .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}
