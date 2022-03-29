package fr.lernejo.navy_battle;

import fr.lernejo.navy_battle.GamePlay.BattleField;
import fr.lernejo.navy_battle.GamePlay.CellGetter;
import fr.lernejo.navy_battle.Recorders.Cell;
import fr.lernejo.navy_battle.Recorders.ResponseMessageFire;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CellGetterTest {
    final CellGetter cellGetter = new CellGetter();
    Cell test_cell;
    Cell cellOrigin = new Cell(1, 7);
    BattleField battleField = new BattleField();

    @Test
    void GetRandomCellTest() {
        Cell test_cell = cellGetter.GetRandomCell(battleField.fired);
        System.out.println(test_cell);

        Assertions.assertThat(test_cell.col()).isLessThan(10);
        Assertions.assertThat(test_cell.row()).isLessThan(10);
    }

    @Test
    void GetCellTacticTest() {
        Cell test_cell = cellGetter.GetCellTactic(battleField.fired);
        System.out.println(test_cell);
        Assertions.assertThat(test_cell.col()).isLessThan(10);
        Assertions.assertThat(test_cell.row()).isLessThan(10);

    }

    @Test
    void AddGoodPositionsTest() {
        Assertions.assertThat(battleField.cellGetter.myMap.size()).isEqualTo(17);
    }

    @Test
    void GetCellStanderWayTest_miss() {
        test_cell = cellGetter.GetCellRandomWay(battleField.navalMap, battleField.fired);
        Assertions.assertThat(test_cell.col()).isLessThan(10);
        Assertions.assertThat(test_cell.row()).isLessThan(10);
    }

    @Test
    void GetCellStanderWayTest_hit() {
        Cell cell = new Cell(1, 7);
        battleField.fired.add(cell);
        ResponseMessageFire fire = new ResponseMessageFire("hit", true);
        battleField.navalMap.put(cell, fire);
        battleField.cellGetter.hitMap.add(cell);
        battleField.cellGetter.consequences.add("hit");

        test_cell = battleField.cellGetter.GetCellRandomWay(battleField.navalMap, battleField.fired);
        System.out.println(test_cell);
        Assertions.assertThat(test_cell.col()).isEqualTo(cell.col());
        Assertions.assertThat(test_cell.row()).isEqualTo(cell.row() + 1);
    }

    @Test
    void GetNearbyCellTest() {
        battleField.fired.add(cellOrigin);
        System.out.println(battleField.fired);
        Cell down = cellOrigin.GetNearbyCell(battleField.fired);
        battleField.fired.add(down);
        Assertions.assertThat(down.col()).isEqualTo(cellOrigin.col());
        Assertions.assertThat(down.row()).isEqualTo(cellOrigin.row() + 1);

        System.out.println(battleField.fired);
        Cell right = cellOrigin.GetNearbyCell(battleField.fired);
        Assertions.assertThat(right.col()).isEqualTo(cellOrigin.col() + 1);
        Assertions.assertThat(right.row()).isEqualTo(cellOrigin.row());

        System.out.println(battleField.fired);
        Cell up = cellOrigin.GetNearbyCell(battleField.fired);
        Assertions.assertThat(up.col()).isEqualTo(cellOrigin.col());
        Assertions.assertThat(up.row()).isEqualTo(cellOrigin.row() - 1);

        System.out.println(battleField.fired);
        Cell left = cellOrigin.GetNearbyCell(battleField.fired);
        Assertions.assertThat(left.col()).isEqualTo(cellOrigin.col() - 1);
        Assertions.assertThat(left.row()).isEqualTo(cellOrigin.row());
    }

    @Test
    void constructorTEEst() {
        System.out.println(cellGetter.croixCells);
        Assertions.assertThat(cellGetter.croixCells.size()).isEqualTo(20);

    }
}
