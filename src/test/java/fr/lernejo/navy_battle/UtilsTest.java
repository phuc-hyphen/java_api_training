package fr.lernejo.navy_battle;

import fr.lernejo.navy_battle.Recorders.Cell;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class UtilsTest {
    Utils utils = new Utils();

    @Test
    void getCharForNumberTest() {
        Assertions.assertThat(utils.getCharForNumber(0)).isEqualTo("A");
        Assertions.assertThat(utils.getCharForNumber(1)).isEqualTo("B");
        Assertions.assertThat(utils.getCharForNumber(2)).isEqualTo("C");
        Assertions.assertThat(utils.getCharForNumber(3)).isEqualTo("D");
        Assertions.assertThat(utils.getCharForNumber(4)).isEqualTo("E");
        Assertions.assertThat(utils.getCharForNumber(5)).isEqualTo("F");
        Assertions.assertThat(utils.getCharForNumber(6)).isEqualTo("G");
        Assertions.assertThat(utils.getCharForNumber(7)).isEqualTo("H");
        Assertions.assertThat(utils.getCharForNumber(8)).isEqualTo("I");
        Assertions.assertThat(utils.getCharForNumber(9)).isEqualTo("J");
    }

    @Test
    void getParamMapTest() {
        Cell cell = utils.getParamMap("/api/game/fire?cell=F5");
        Assertions.assertThat(cell.col()).isEqualTo(5);
        Assertions.assertThat(cell.row()).isEqualTo(5);
        Cell cell2 = utils.getParamMap("");
        Assertions.assertThat(cell2).isNull();
    }

    @Test
    void BadRequestTest() throws IOException, InterruptedException {
        String[] args = {"1256"};
        Launcher.main(args);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest requetePost = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:1256/api/game/start"))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .GET()
            .build();
        HttpResponse<String> response = client.send(requetePost, HttpResponse.BodyHandlers.ofString());
        Assertions.assertThat(response.statusCode()).isEqualTo(404);
        Assertions.assertThat(response.body()).contains("Not Found");

    }

    @Test
    void CheckConsequenceTest() {
        Assertions.assertThat(utils.CheckConsequence("miss")).isTrue();
        Assertions.assertThat(utils.CheckConsequence("Miss")).isFalse();
    }

    @Test
    void PrintShipTest() {
        // setting output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        BattleField battleField = new BattleField();
        battleField.InitialSea();
        utils.Print_Ships(battleField);

        System.out.flush();
        System.setOut(old);
        Assertions.assertThat(baos.toString()).contains("Croiseur");
        Assertions.assertThat(baos.toString()).contains("Torpilleur");
        Assertions.assertThat(baos.toString()).contains("Porte - Avion");
        Assertions.assertThat(baos.toString()).contains("Contre - Torpilleur");
    }

}
