package fr.lernejo.navy_battle;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

class FireHandlerTest {

    @Test
    void Fire_handlerTest() throws IOException, InterruptedException {
        // setting output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        // launcher server
        String[] args = {"1904"};
        Launcher.main(args);

        // launcher Client
        GameClient client = new GameClient(new BattleField());
        client.FireClient("http://localhost:1904", "G4");

        System.out.flush();
        System.setOut(old);
        Assertions.assertThat(baos.toString()).contains("ResponseMessageFire");
        Assertions.assertThat(baos.toString()).contains("consequence");
        Assertions.assertThat(baos.toString()).contains("Received");
        Assertions.assertThat(baos.toString()).contains("shipLeft");
        Assertions.assertThat(baos.toString()).contains("Send");
        Assertions.assertThat(baos.toString()).contains("Cell");
    }

}
