package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;
import fr.lernejo.navy_battle.Handlers.FireHandler;
import fr.lernejo.navy_battle.Handlers.PingHandler;
import fr.lernejo.navy_battle.Handlers.StartHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        final Map<String, String> gameContext = new HashMap<String, String>();
        final GameClient gameClient = new GameClient(new BattleField());
        if (args.length < 1) {
            System.out.println("Argument missing ! \n For player 1: pass 1st argument as port number \n For player 2: pass 1st argument as port number and 2nd as player url");
            return;
        }
        gameContext.put("my_id", UUID.randomUUID().toString());
        gameContext.put("my_port", args[0]);
        StartServer(get_port(args), gameContext, gameClient);
        if (args.length == 2) {
            gameContext.put("adv_url", args[1]);
            gameClient.StartGame(args[1], gameContext);
        }
    }

    private static void StartServer(int port, Map<String, String> gameContext, GameClient client) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        ExecutorService executorService = new java.util.concurrent.ThreadPoolExecutor(
            1,
            1,
            60L,
            java.util.concurrent.TimeUnit.SECONDS,
            new java.util.concurrent.LinkedBlockingQueue<Runnable>());
        server.createContext("/ping", new PingHandler());
        server.createContext("/api/game/start", new StartHandler(gameContext, client));
        server.createContext("/api/game/fire", new FireHandler(gameContext, client));
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
