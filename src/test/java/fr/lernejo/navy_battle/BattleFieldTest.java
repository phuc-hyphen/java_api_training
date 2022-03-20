package fr.lernejo.navy_battle;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class BattleFieldTest {
    BattleField battleField = new BattleField();
    Cell test_cell;

    @Test
    void InitialSeaTest() {
        battleField.InitialSea();
        System.out.println(battleField.contreTorpilleurMap);
        Assertions.assertThat(battleField.croiseurMap.size()).isEqualTo(4);
        Assertions.assertThat(battleField.contreTorpilleur2Map.size()).isEqualTo(3);
        Assertions.assertThat(battleField.torpilleurMap.size()).isEqualTo(2);
        Assertions.assertThat(battleField.porteAvionMap.size()).isEqualTo(5);
        Assertions.assertThat(battleField.contreTorpilleurMap.size()).isEqualTo(3);
    }

    @Test
    void GetRandomCellTest() {
        test_cell = battleField.GetRandomCell();
        System.out.println(test_cell);

        Assertions.assertThat(test_cell.col()).isLessThan(10);
        Assertions.assertThat(test_cell.row()).isLessThan(10);
    }

    @Test
    void HitCheckTest() {
        Cell hitCell = new Cell(10, 10);
        boolean hit = battleField.HitCheck(hitCell);
        Assertions.assertThat(hit).isFalse();
    }
    @Test
    void SunkCheckTest() {
        if(battleField.porteAvionMap.isEmpty())
            battleField.InitialSea();
        boolean Sunk = battleField.SunkCheck();
        Assertions.assertThat(Sunk).isFalse();
    }

}
