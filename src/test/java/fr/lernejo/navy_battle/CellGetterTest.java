package fr.lernejo.navy_battle;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellGetterTest {
    final CellGetter cellGetter = new CellGetter();
    Cell test_cell;
    Cell cellOrigin = new Cell(1, 7);
    BattleField battleField = new BattleField();

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

    @Test
    void AddGoodPositionsTest() {
        BattleField battleField = new BattleField();
        battleField.InitialSea();
        Assertions.assertThat(battleField.cellGetter.goodPositions.size()).isEqualTo(17);
    }

    @Test
    void GetLastHitCellTest() {
        BattleField battleField = new BattleField();

        test_cell = battleField.GetNextShot();
        ResponseMessageFire fire = new ResponseMessageFire("hit", true);
        battleField.navalMap.put(battleField.fired.get(battleField.fired.size() - 1), fire);

        Cell testCell2 = cellGetter.GetLastHitCell(battleField.navalMap);
        Assertions.assertThat(testCell2.col()).isEqualTo(test_cell.col());
        Assertions.assertThat(testCell2.row()).isEqualTo(test_cell.row());
    }

    @Test
    void GetCellStanderWayTest_miss() {
        BattleField battleField = new BattleField();

        test_cell = cellGetter.GetCellStanderWay(battleField.navalMap, battleField.fired);
        Assertions.assertThat(test_cell.col()).isLessThan(10);
        Assertions.assertThat(test_cell.row()).isLessThan(10);
    }

    @Test
    void GetCellStanderWayTest_hit() {
        BattleField battleField = new BattleField();
        Cell cell = new Cell(1, 7);
        battleField.fired.add(cell);
        ResponseMessageFire fire = new ResponseMessageFire("hit", true);
        battleField.navalMap.put(cell, fire);

        test_cell = cellGetter.GetCellStanderWay(battleField.navalMap, battleField.fired);
        Assertions.assertThat(test_cell.col()).isEqualTo(cell.col());
        Assertions.assertThat(test_cell.row()).isEqualTo(cell.row() + 1);
    }

    @Test
    void GetNearbyCellTest() {
        battleField.fired.add(cellOrigin);
        System.out.println(battleField.fired);
        Cell down = cellGetter.GetNearbyCell(cellOrigin, battleField.fired);
        battleField.fired.add(down);
        Assertions.assertThat(down.col()).isEqualTo(cellOrigin.col());
        Assertions.assertThat(down.row()).isEqualTo(cellOrigin.row() + 1);

        System.out.println(battleField.fired);
        Cell up = cellGetter.GetNearbyCell(cellOrigin, battleField.fired);
        Assertions.assertThat(up.col()).isEqualTo(cellOrigin.col());
        Assertions.assertThat(up.row()).isEqualTo(cellOrigin.row() - 1);

        System.out.println(battleField.fired);
        Cell right = cellGetter.GetNearbyCell(cellOrigin, battleField.fired);
        Assertions.assertThat(right.col()).isEqualTo(cellOrigin.col() + 1);
        Assertions.assertThat(right.row()).isEqualTo(cellOrigin.row());

        System.out.println(battleField.fired);
        Cell left = cellGetter.GetNearbyCell(cellOrigin, battleField.fired);
        Assertions.assertThat(left.col()).isEqualTo(cellOrigin.col() - 1);
        Assertions.assertThat(left.row()).isEqualTo(cellOrigin.row());

    }
}
