package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Launcher {
    public static void main(String[] args) throws IOException {
        final Map<String, String> gameContext = new HashMap<String, String>();
        if (args.length < 1)
            return;
        int port = get_port(args);
        UUID id = UUID.randomUUID();
        gameContext.put("my_id", id.toString());
        gameContext.put("my_port", String.valueOf(port));
        StartServer(port, gameContext);

    }

    private static void StartServer(int port, Map<String, String> gameContext) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        ExecutorService executorService = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
        server.createContext("/ping", new PingHandler());
        server.createContext("/api/game/start", new StartHandler(gameContext));
//        server.createContext("/api/game/fire", new FireHandler());
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
