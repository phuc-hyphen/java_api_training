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
    private final GameClient client;

    public FireHandler(Map<String, String> gameContext, GameClient client) {
        this.gameContext = gameContext;
        this.client = client;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
//        System.out.println("received");
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            String consequence = getConsequence(exchange);
            Response(exchange, consequence);
        } else {
            Not_Found(exchange);
        }
        NextShot();
    }

    private String getConsequence(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        Cell cell = getParamMap(uri.toString());
//        System.out.println(uri.toString());
        String consequence = "miss";
        if (client.battleField.HitCheck(cell)) {
            consequence = "hit";
            if (client.battleField.SunkCheck()) {
                consequence = "sunk";
            }
        }
        return consequence;
    }

    private void Response(HttpExchange exchange, String consequence) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseMessage map = new ResponseMessage(consequence, client.battleField.ShipLeft());
        String json = mapper.writeValueAsString(map);
        exchange.getResponseHeaders().add("Accept", "application/json");
        exchange.getResponseHeaders().add("Content-type", "application/json");
        exchange.sendResponseHeaders(202, json.length());
        try (OutputStream os = exchange.getResponseBody()) { // (1)
            os.write(json.getBytes());
        }
    }

    private void NextShot() throws IOException {
        try {
            Thread.sleep(1000);
            Cell nextShot = client.battleField.GetRandomCell();
            String pos = client.utils.getCharForNumber(nextShot.x()) + nextShot.y();
            System.out.println(nextShot);
            client.FireClient(gameContext.get("adv_url"), pos);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
