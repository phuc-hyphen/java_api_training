package fr.lernejo.navy_battle;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellGetterTest {
    final CellGetter cellGetter = new CellGetter();

    @Test
    void GetRandomCellTest() {
        BattleField battleField = new BattleField();
        battleField.InitialSea();
        Cell test_cell = cellGetter.GetRandomCell(battleField.fired);
        System.out.println(test_cell);

        Assertions.assertThat(test_cell.col()).isLessThan(10);
        Assertions.assertThat(test_cell.row()).isLessThan(10);
    }

    @Test
    void GetCellTacticTest() {
        BattleField battleField = new BattleField();
        battleField.InitialSea();
        Cell test_cell = cellGetter.GetCellTactic(battleField.fired);
        System.out.println(test_cell);
        Assertions.assertThat(test_cell.col()).isLessThan(10);
        Assertions.assertThat(test_cell.row()).isLessThan(10);

    }
}
