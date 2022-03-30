package fr.lernejo.navy_battle.GamePlay;

import fr.lernejo.navy_battle.Recorders.Cell;
import fr.lernejo.navy_battle.Recorders.ResponseMessageFire;

import java.util.*;

public class CellGetter {
    public final List<Cell> myMap = new ArrayList<Cell>();
    public final List<Cell> croixCells = new ArrayList<Cell>();
    public final List<String> consequences = new ArrayList<String>();
    public final List<Cell> hitMap = new ArrayList<Cell>();
    final Random rand = new Random();

    public CellGetter() {
        for (int i = 0; i < 10; i++) {
            croixCells.add(new Cell(i, i));
//            croixCells.add(new Cell(9 - i, i));
        }
        croixCells.add(new Cell(0, 9));
    }

    public void AddGoodPositions(Map<Cell, Boolean> ship1, Map<Cell, Boolean> ship3, Map<Cell, Boolean> ship2, Map<Cell, Boolean> ship4, Map<Cell, Boolean> ship5) {
        for (Map.Entry<Cell, Boolean> it : ship1.entrySet()) {
            myMap.add(it.getKey());
        }
        for (Map.Entry<Cell, Boolean> it : ship2.entrySet()) {
            myMap.add(it.getKey());
        }
        for (Map.Entry<Cell, Boolean> it : ship3.entrySet()) {
            myMap.add(it.getKey());
        }
        for (Map.Entry<Cell, Boolean> it : ship4.entrySet()) {
            myMap.add(it.getKey());
        }
        for (Map.Entry<Cell, Boolean> it : ship5.entrySet()) {
            myMap.add(it.getKey());
        }
    }

    public Cell GetRandomCell(List<Cell> fired) { // get random cell
        int x, y;
        Cell cell;
        do {
            x = rand.nextInt(0, 10);
            y = rand.nextInt(0, 10);
            cell = new Cell(x, y);
        } while (fired.contains(cell) || x < 0 || y < 0);
        fired.add(cell);
        return cell;
    }

    public Cell GetRandomCell2(List<Cell> fired) {
        if (!croixCells.isEmpty()) {
            int index = rand.nextInt(croixCells.size());
//            int index = 0;
            Cell cell = croixCells.get(index);
            croixCells.remove(index);
            fired.add(cell);
            return cell;
        }
        return GetRandomCell(fired);
    }


    public Cell GetCellRandomWay(Map<Cell, ResponseMessageFire> map, List<Cell> fired) { // 90
        if (!map.isEmpty()) {
            if (!hitMap.isEmpty()) {
                Cell nextCell = hitMap.get(hitMap.size() - 1).GetNearbyCell(fired);
                if (nextCell != null) {
                    return nextCell;
                }
            }
        }
        return GetRandomCell2(fired);
    }

    public Cell GetCellTactic(List<Cell> fired) {//17
        for (Cell position : myMap) {
            if (!fired.contains(position)) {
                fired.add(position);
                return position;
            }
        }
        return GetRandomCell2(fired);
    }

}
