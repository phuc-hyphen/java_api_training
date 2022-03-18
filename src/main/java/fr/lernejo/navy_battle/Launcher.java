package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        final Map<String, String> gameContext = new HashMap<String, String>();
        final GameClient gameClient = new GameClient();
        if (args.length < 1)
            return;
        gameContext.put("my_id", UUID.randomUUID().toString());
        gameContext.put("my_port", String.valueOf(get_port(args)));
        StartServer(get_port(args), gameContext, gameClient);
        if (args.length == 2) {
            gameContext.put("adv_url", args[1]);
            gameClient.StartGame(args[1], gameContext);
        }
//        count.await(); // wait until `c.countDown()` is invoked
//        gameClient.FireClient(gameContext.get("adv_url"), "F5");
    }

    private static void StartServer(int port, Map<String, String> gameContext, GameClient client) throws IOException, InterruptedException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        final CountDownLatch count = new CountDownLatch(1); // allows one or more threads to wait until a set of operations being performed in other threads completes.

//        ExecutorService executorService = new ThreadPoolExecutor(1, 1, 2000, TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<Runnable>());
        server.createContext("/ping", new PingHandler());
        server.createContext("/api/game/start", new StartHandler(gameContext, client));
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
