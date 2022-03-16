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

    public StartHandler(Map<String, String> gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("POST")) {
            CheckAndGetData(exchange);
            Response(exchange, gameContext.get("my_id"), gameContext.get("my_port"), "May the best code win");
        } else {
            Not_Found(exchange);
        }
        try {
            Thread.sleep(1000);
            GameClient.FireClient(gameContext.get("adv_url"), "F5");
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
            Bad_resquest(exchange);
        }
    }

    private void Print_Info(String message) {
        System.out.println(message);
        System.out.println(gameContext.get("my_id"));
        System.out.println(gameContext.get("my_port"));
        System.out.println(gameContext.get("adv_id"));
        System.out.println(gameContext.get("adv_url"));
    }

    private void Response(HttpExchange exchange, String id, String port, String mess) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StartMessage map = new StartMessage(id, "http://localhost:" + port, mess);
        String json = mapper.writeValueAsString(map);
        exchange.sendResponseHeaders(202, json.length());
        try (OutputStream os = exchange.getResponseBody()) { // (1)
            os.write(json.getBytes());
        }
    }

    private void Bad_resquest(HttpExchange exchange) throws IOException {
        String body = "Bad Request";
        exchange.sendResponseHeaders(400, body.length());
        try (OutputStream os = exchange.getResponseBody()) { // (1)
            os.write(body.getBytes());
        }
    }

    private void Not_Found(HttpExchange exchange) throws IOException {
        String body = "Not Found";
        exchange.sendResponseHeaders(404, body.length());
        try (OutputStream os = exchange.getResponseBody()) { // (1)
            os.write(body.getBytes());
        }
    }
}
