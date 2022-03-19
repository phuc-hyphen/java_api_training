package fr.lernejo.navy_battle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

public class FireHandler implements HttpHandler {
    private final Map<String, String> gameContext;
    private final BattleField battleField;

    public FireHandler(Map<String, String> gameContext, BattleField battleField) {
        this.gameContext = gameContext;
        this.battleField = battleField;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            URI uri = exchange.getRequestURI();
            Cell cell = getParamMap(uri.toString());
            String consequence = "miss";
            if (battleField.HitCheck(cell)) {
                consequence = "hit";
                if (battleField.SunkCheck()) {
                    consequence = "sunk";
                }
            }
            Response(exchange, consequence);
        } else
            Not_Found(exchange);
    }

    private void Print_Info() {
        System.out.println(gameContext.get("my_id"));
        System.out.println(gameContext.get("my_port"));
        System.out.println(gameContext.get("adv_id"));
        System.out.println(gameContext.get("adv_url"));
    }

    private void Response(HttpExchange exchange, String consequence) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseMessage map = new ResponseMessage(consequence, battleField.ShipLeft());
        String json = mapper.writeValueAsString(map);
        exchange.getResponseHeaders().add("Content-type", "application/json");
        exchange.sendResponseHeaders(202, json.length());
        try (OutputStream os = exchange.getResponseBody()) { // (1)
            os.write(json.getBytes());
        }
    }

    public Cell getParamMap(String query) { // get cell
        if (query == null || query.isEmpty()) return null;
        String cell = query.substring(query.lastIndexOf("=") + 1, query.length());
        System.out.println(cell);
        return new Cell((int) cell.charAt(0) - 'A' + 1, Integer.parseInt(String.valueOf(cell.charAt(1))) - 1);
    }

    private void Not_Found(HttpExchange exchange) throws IOException {
        String body = "Not Found";
        exchange.sendResponseHeaders(404, body.length());
        try (OutputStream os = exchange.getResponseBody()) { // (1)
            os.write(body.getBytes());
        }
    }
}
