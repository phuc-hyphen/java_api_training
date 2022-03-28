package fr.lernejo.navy_battle.GamePlay;

import fr.lernejo.navy_battle.Recorders.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ship {
    private final Cell startCell;
    private final int size;
    private final String orientation;
    public final Map<Cell, Boolean> mapShip = new HashMap<Cell, Boolean>();


    public Ship(Cell startCell, int size, String orientation) {
        this.startCell = startCell;
        this.size = size;
        this.orientation = orientation;
        MakeShip();
    }

    private void MakeShip() {
        for (int i = 0; i < size; i++) {
            if (orientation.equals("Vertical")) {
                Cell cell = new Cell(startCell.col(), startCell.row() + i);
                mapShip.put(cell, false);
            } else if (orientation.equals("Horizontal")) {
                Cell cell = new Cell(startCell.col() + i, startCell.row());
                mapShip.put(cell, false);
            }
        }
    }

    public boolean IsHit(Cell cell, List<Cell> received) {
        boolean hit = false;
        if (mapShip.containsKey(cell) && !received.contains(cell)) {
            mapShip.replace(cell, false, true);
            hit = true;
            received.add(cell);
        }
        return hit;
    }

    public boolean IfSunk(List<Map<Cell, Boolean>> sunkShips) { // check if the ship is sunk
        if (!sunkShips.contains(mapShip) && !mapShip.containsValue(false)) {
            sunkShips.add(mapShip);
            return true;
        }
        return false;
    }
}
