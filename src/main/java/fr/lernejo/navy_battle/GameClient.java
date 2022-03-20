package fr.lernejo.navy_battle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class GameClient {
    private final HttpClient client = HttpClient.newHttpClient();
    public final BattleField battleField;
    public final Utils utils = new Utils();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public boolean win;

    public GameClient(BattleField battleField) {
        this.battleField = battleField;
    }

    public void StartGame(String adv_url, Map<String, String> gameContext) throws IOException, InterruptedException {
        HttpRequest requestPost = HttpRequest.newBuilder()
            .uri(URI.create(adv_url + "/api/game/start"))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"" + gameContext.get("my_id") + "\", \"url\":\"http://localhost:" + gameContext.get("my_port") + "\", \"message\":\"Let's play !\"}"))
            .build();
        HttpResponse<String> response = client.send(requestPost, HttpResponse.BodyHandlers.ofString());
        ExtractData(gameContext, response);
        battleField.InitialSea();
    }

    private void ExtractData(Map<String, String> gameContext, HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StartMessage jsonMap = objectMapper.readValue(response.body(), StartMessage.class);
        gameContext.put("adv_id", jsonMap.id());
        utils.PrintInfo(gameContext, jsonMap.message());
    }

    public void FireClient(String adv_url, String pos) throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(URI.create(adv_url + "/api/game/fire?cell=" + pos))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .GET()
            .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.statusCode());
//        System.out.println(response.body());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ResponseMessage responseMap = objectMapper.readValue(response.body(), ResponseMessage.class);
        System.out.println(responseMap);
        if (!responseMap.shipLeft()) {
            System.out.println("i'm Win");
        }
    }

}
