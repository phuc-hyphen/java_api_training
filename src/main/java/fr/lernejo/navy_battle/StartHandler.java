package fr.lernejo.navy_battle;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class StartHandler implements HttpHandler {
    private final Map<String, String> gameContext;
    private final GameClient client;
    public final BattleField battleField;

    public StartHandler(Map<String, String> gameContext, GameClient client, BattleField battleField) {
        this.gameContext = gameContext;
        this.client = client;
        this.battleField = battleField;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("POST")) {
            CheckAndGetData(exchange);
            Response(exchange, gameContext.get("my_id"), gameContext.get("my_port"));
        } else {
            BadRequest(exchange, true);
        }
        battleField.InitialSea();
        FirstShot();
    }

    private void FirstShot() throws IOException {
        try {
            Thread.sleep(1000);
            Cell firstShot = battleField.RandomShot();
            String pos = getCharForNumber(firstShot.x()) + firstShot.y();
            client.FireClient(gameContext.get("adv_url"), pos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void CheckAndGetData(HttpExchange exchange) throws IOException {
        try {
            InputStream post_reponse = exchange.getRequestBody();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            StartMessage jsonMap = objectMapper.readValue(post_reponse, StartMessage.class);
            String message = jsonMap.message();
            gameContext.put("adv_id", jsonMap.id());
            gameContext.put("adv_url", jsonMap.url());
            Print_Info(message);
        } catch (IOException e) {
            BadRequest(exchange, false);
        }
    }

    private void Print_Info(String message) {
        System.out.println(message);
        System.out.println(gameContext.get("my_id"));
        System.out.println(gameContext.get("my_port"));
        System.out.println(gameContext.get("adv_id"));
        System.out.println(gameContext.get("adv_url"));
    }

    private void Response(HttpExchange exchange, String id, String port) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StartMessage map = new StartMessage(id, "http://localhost:" + port, "May the best code win");
        String json = mapper.writeValueAsString(map);
        exchange.sendResponseHeaders(202, json.length());
        try (OutputStream os = exchange.getResponseBody()) { // (1)
            os.write(json.getBytes());
        }
    }

    private void BadRequest(HttpExchange exchange, boolean notFound) throws IOException {
        int code = 400;
        String body = "Bad Request";
        if (notFound) {
            body = "Not Found";
            code = 404;
        }
        exchange.sendResponseHeaders(code, body.length());
        try (OutputStream os = exchange.getResponseBody()) { // (1)
            os.write(body.getBytes());
        }
    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char) (i + 64)) : null;
    }
}
