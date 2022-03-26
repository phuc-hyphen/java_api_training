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
    void GetNextCellTest() {
        battleField.InitialSea();
        test_cell = battleField.GetNextCell();
        System.out.println(test_cell);

        Assertions.assertThat(test_cell.col()).isLessThan(10);
        Assertions.assertThat(test_cell.row()).isLessThan(10);

        ResponseMessageFire fire = new ResponseMessageFire("hit", true);
        battleField.navalMap.put(battleField.fired.get(battleField.fired.size() - 1), fire);
        Cell testCell2 = battleField.GetNextCell();
        Assertions.assertThat(testCell2.col()).isEqualTo(test_cell.col());
        Assertions.assertThat(testCell2.row()).isEqualTo(test_cell.row() + 1);

    }

    @Test
    void HitCheckTest() {
        battleField.InitialSea();
        Cell hitCell = new Cell(1, 4);
        boolean hit = battleField.HitCheck(hitCell);
        Assertions.assertThat(hit).isTrue();
    }

//    @Test
//    void IfCellHitTest() {
//        battleField.InitialSea();
//        Cell hitCell = new Cell(1, 4);
//        boolean hit = battleField.IfCellHit(hitCell, battleField.croiseurMap);
//        Assertions.assertThat(hit).isTrue();
//    }

    @Test
    void SunkCheckTest() {
        battleField.InitialSea();
        if (battleField.porteAvionMap.isEmpty())
            battleField.InitialSea();
        boolean Sunk = battleField.SunkCheck();
        Assertions.assertThat(Sunk).isFalse();
    }

}
