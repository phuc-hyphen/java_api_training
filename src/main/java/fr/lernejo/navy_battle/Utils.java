package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Utils {
    public void PrintInfo(Map<String, String> gameContext, String Mess) {
        System.out.println("mess : " + Mess);
        System.out.println("my ID:" + gameContext.get("my_id"));
        System.out.println("my port:" + gameContext.get("my_port"));
        System.out.println("adv ID:" + gameContext.get("adv_id"));
        System.out.println("adv URL:" + gameContext.get("adv_url"));
    }

    public String getCharForNumber(int i) {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        if (i > 25) {
            return null;
        }
        return Character.toString(alphabet[i]);
    }

    public Cell getParamMap(String query) { // get cell
        if (query == null || query.isEmpty()) return null;
        String cell = query.substring(query.lastIndexOf("=") + 1, query.length());
        return new Cell((int) cell.charAt(0) - 'A', Integer.parseInt(String.valueOf(cell.charAt(1))));//C7 ->  7th row, 3rd col
    }

    public void BadRequest(HttpExchange exchange, boolean notFound) throws IOException {
        int code = 400;
        String body = "Bad Request";
        if (notFound) {
            body = "Not Found";
            code = 404;
        }
        exchange.sendResponseHeaders(code, body.length());
        try (OutputStream os = exchange.getResponseBody()) { // important
            os.write(body.getBytes());
        }
    }

    public boolean CheckConsequence(String consequence) {
        for (EnumConsequence con : EnumConsequence.values()) {
            if (con.name().equals(consequence)) {
                return true;
            }
        }
        return false;
    }

    public void Print_Ships(BattleField battleField) {
        System.out.println("Porte - Avion : " + battleField.porteAvionMap);
        System.out.println("Croiseur : " + battleField.croiseurMap);
        System.out.println("Torpilleur : " + battleField.torpilleurMap);
        System.out.println("Contre - Torpilleur : " + battleField.contreTorpilleurMap);
        System.out.println("Contre - Torpilleur2 : " + battleField.contreTorpilleur2Map);
    }


}
