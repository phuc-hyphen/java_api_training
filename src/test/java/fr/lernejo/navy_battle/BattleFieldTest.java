package fr.lernejo.navy_battle;

import fr.lernejo.navy_battle.GamePlay.BattleField;
import fr.lernejo.navy_battle.Recorders.Cell;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class BattleFieldTest {
    BattleField battleField = new BattleField();
    Cell test_cell;

    @Test
    void InitialSeaTest() {
//        battleField.InitialSea();
        Assertions.assertThat(battleField.croiseur.mapShip.size()).isEqualTo(4);
        Assertions.assertThat(battleField.contreTorpilleur2.mapShip.size()).isEqualTo(3);
        Assertions.assertThat(battleField.torpilleur.mapShip.size()).isEqualTo(2);
        Assertions.assertThat(battleField.porteAvion.mapShip.size()).isEqualTo(5);
        Assertions.assertThat(battleField.contreTorpilleur.mapShip.size()).isEqualTo(3);
    }

    @Test
    void GetNextCellTest() {
//        battleField.InitialSea();
        test_cell = battleField.GetNextShot();
        System.out.println(test_cell);

        Assertions.assertThat(test_cell.col()).isLessThan(10);
        Assertions.assertThat(test_cell.row()).isLessThan(10);
    }

    @Test
    void HitCheckTest() {
//        battleField.InitialSea();
        Cell hitCell = new Cell(1, 4);
        boolean hit = battleField.HitCheck(hitCell);
        Assertions.assertThat(hit).isTrue();
    }

    @Test
    void SunkCheckTest() {
//        battleField.InitialSea();
        boolean Sunk = battleField.SunkCheck();
        Assertions.assertThat(Sunk).isFalse();
        Cell cell1 = new Cell(8, 8);
        battleField.torpilleur.mapShip.replace(cell1, false, true);
        Cell cell2 = new Cell(9, 8);
        battleField.torpilleur.mapShip.replace(cell2, false, true);
        Assertions.assertThat(battleField.SunkCheck()).isFalse();
    }

    @Test
    void ShipLeftTest() {
//        battleField.InitialSea();
        boolean shipLeft = battleField.ShipLeft();
        Assertions.assertThat(shipLeft).isTrue();
    }


}
