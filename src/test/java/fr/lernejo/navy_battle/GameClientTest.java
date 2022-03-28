package fr.lernejo.navy_battle;

import fr.lernejo.navy_battle.GamePlay.BattleField;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

class GameClientTest {
    @Test
    void StartGameTest() throws IOException, InterruptedException, ExecutionException, TimeoutException {
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
