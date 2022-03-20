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
            client.utils.BadRequest(exchange, true);
        }
        if (client.battleField.ShipLeft()) {
            NextShot();
        } else {
            System.out.println(gameContext.get("adv_url") + "win");
        }
    }

    private String getConsequence(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        Cell cell = client.utils.getParamMap(uri.toString());
        System.out.println("received : " + cell);
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
            Thread.sleep(10);
            Cell nextShot = client.battleField.GetRandomCell();
            System.out.println("send : " + nextShot);
            String pos = client.utils.getCharForNumber(nextShot.col()) + nextShot.row();
            client.FireClient(gameContext.get("adv_url"), pos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
