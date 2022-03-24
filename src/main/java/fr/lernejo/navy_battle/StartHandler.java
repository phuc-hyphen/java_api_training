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

    public StartHandler(Map<String, String> gameContext, GameClient client) {
        this.gameContext = gameContext;
        this.client = client;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("POST")) {
            CheckAndGetData(exchange);
            Response(exchange, gameContext.get("my_id"), gameContext.get("my_port"));
            client.battleField.InitialSea();
            FirstShot();
        } else {
            client.utils.BadRequest(exchange, true);
        }
    }

    private void FirstShot() throws IOException {
        try {
            Thread.sleep(10);
            Cell firstShot = client.battleField.GetNextCell();
            String pos = client.utils.getCharForNumber(firstShot.col()) + firstShot.row();
            client.FireClient(gameContext.get("adv_url"), pos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void CheckAndGetData(HttpExchange exchange) throws IOException {
        try {
            InputStream post_response = exchange.getRequestBody();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            StartMessage jsonMap = objectMapper.readValue(post_response, StartMessage.class);
            String message = jsonMap.message();
            gameContext.put("adv_id", jsonMap.id());
            gameContext.put("adv_url", jsonMap.url());
            client.utils.PrintInfo(gameContext, message);
        } catch (IOException e) {
            client.utils.BadRequest(exchange, false);
        }
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


}
