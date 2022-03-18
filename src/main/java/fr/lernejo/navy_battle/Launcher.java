package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        final Map<String, String> gameContext = new HashMap<String, String>();
        final GameClient gameClient = new GameClient(HttpClient.newHttpClient());
        final BattleField battleField= new BattleField();
        if (args.length < 1)
            return;
        gameContext.put("my_id", UUID.randomUUID().toString());
        gameContext.put("my_port", String.valueOf(get_port(args)));
        StartServer(get_port(args), gameContext, gameClient, battleField);
        if (args.length == 2) {
            gameContext.put("adv_url", args[1]);
            gameClient.StartGame(args[1], gameContext);
        }
    }

    private static void StartServer(int port, Map<String, String> gameContext, GameClient client,BattleField field) throws IOException, InterruptedException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        server.createContext("/ping", new PingHandler());
        server.createContext("/api/game/start", new StartHandler(gameContext, client, field));
        server.createContext("/api/game/fire", new FireHandler(gameContext));
        server.setExecutor(executorService);
        server.start();
    }

    private static int get_port(String[] args) {
        try {
            return Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
