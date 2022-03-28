package fr.lernejo.navy_battle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lernejo.navy_battle.Recorders.Cell;
import fr.lernejo.navy_battle.Recorders.ResponseMessageFire;
import fr.lernejo.navy_battle.Recorders.StartMessage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GameClient {
    private final HttpClient client = HttpClient.newHttpClient();
    public final BattleField battleField;
    public final Utils utils = new Utils();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameClient(BattleField battleField) {
        this.battleField = battleField;
    }

    public void StartGame(String adv_url, Map<String, String> gameContext) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        HttpRequest requestPost = HttpRequest.newBuilder()
            .uri(URI.create(adv_url + "/api/game/start"))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"" + gameContext.get("my_id") + "\", \"url\":\"http://localhost:" + gameContext.get("my_port") + "\", \"message\":\"Let's play !\"}"))
            .build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(requestPost, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        ExtractData(gameContext, result);
        battleField.InitialSea();
    }

    private void ExtractData(Map<String, String> gameContext, String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StartMessage jsonMap = objectMapper.readValue(response, StartMessage.class);
        gameContext.put("adv_id", jsonMap.id());
        utils.PrintInfo(gameContext, jsonMap.message());
    }

    public void FireClient(String adv_url, String pos) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        System.out.println(battleField.navalMap.size());
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(URI.create(adv_url + "/api/game/fire?cell=" + pos))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .GET()
            .build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(getRequest, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        ResponseAnalyse(result, pos);
    }

    private void ResponseAnalyse(String response, String pos) throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ResponseMessageFire responseMap = objectMapper.readValue(response, ResponseMessageFire.class);
        if (utils.CheckConsequence(responseMap.consequence())) {
            System.out.println(responseMap);
            battleField.navalMap.put(new Cell((int) pos.charAt(0) - 'A', Integer.parseInt(String.valueOf(pos.charAt(1)))), responseMap);
            if (!responseMap.shipLeft() && battleField.ShipLeft()) { // you out of ship ||| me still have some -> i'm win
                System.out.println("I'm win");
                System.exit(0);
            }
        }
    }

}
