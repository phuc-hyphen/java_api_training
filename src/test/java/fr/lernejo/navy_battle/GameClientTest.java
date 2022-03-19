package fr.lernejo.navy_battle;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class GameClientTest {
    @Test
    void StartGameTest() throws IOException, InterruptedException {
        // setting output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        String[] args = {"8000"};
        Launcher.main(args);

        final Map<String, String> gameContext = new HashMap<String, String>();
        GameClient client = new GameClient(new BattleField());
        gameContext.put("my_id", "TEQS.IT");
        gameContext.put("my_port", "1234");

        client.StartGame("http://localhost:8000", gameContext);

        // get out put
        System.out.flush();
        System.setOut(old);
        Assertions.assertThat(baos.toString()).contains("TEQS.IT");
        Assertions.assertThat(baos.toString()).contains("8000");
    }
}
