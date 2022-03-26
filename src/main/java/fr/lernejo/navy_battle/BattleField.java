package fr.lernejo.navy_battle;

import java.util.*;


public class BattleField {
    public final Map<Cell, Boolean> porteAvionMap = new HashMap<Cell, Boolean>();
    public final Map<Cell, Boolean> croiseurMap = new HashMap<Cell, Boolean>();
    public final Map<Cell, Boolean> torpilleurMap = new HashMap<Cell, Boolean>();
    public final Map<Cell, Boolean> contreTorpilleurMap = new HashMap<Cell, Boolean>();
    public final Map<Cell, Boolean> contreTorpilleur2Map = new HashMap<Cell, Boolean>();
    public final Map<Cell, ResponseMessageFire> navalMap = new HashMap<Cell, ResponseMessageFire>();
    public final List<Cell> fired = new ArrayList<Cell>();
    final List<Cell> received = new ArrayList<Cell>();
    final List<Map<Cell, Boolean>> sunkShips = new ArrayList<Map<Cell, Boolean>>();
    //    public final List<ResponseMessageFire> responses = new ArrayList<ResponseMessageFire>();
    final Utils utils = new Utils();

    public void InitialSea() {
        Ship porteAvion = new Ship(new Cell(3, 1), 5, "Vertical");
        porteAvion.AddLocations(porteAvionMap);
        Ship croiseur = new Ship(new Cell(1, 1), 4, "Vertical");
        croiseur.AddLocations(croiseurMap);
        Ship torpilleur = new Ship(new Cell(8, 8), 2, "Vertical");
        torpilleur.AddLocations(torpilleurMap);
        Ship contreTorpilleur = new Ship(new Cell(0, 9), 3, "Horizontal");
        contreTorpilleur.AddLocations(contreTorpilleurMap);
        Ship contreTorpilleur2 = new Ship(new Cell(5, 5), 3, "Horizontal");
        contreTorpilleur2.AddLocations(contreTorpilleur2Map);
    }

    public boolean ShipLeft() {
        return sunkShips.size() != 5;
    }

    private Cell GetLastHitCell() {
        Cell cell = null;
        for (Map.Entry<Cell, ResponseMessageFire> it : navalMap.entrySet()) {
            if (Objects.equals(it.getValue().consequence(), "hit")) {
                cell = it.getKey();
            }
        }
        return cell;
    }

    public Cell GetNextCell() {
        if (!navalMap.isEmpty()) {
            Cell lastCell = GetLastHitCell();
            if (lastCell != null) {
                Cell nextCell = new Cell(lastCell.col(), lastCell.row() + 1);
                if (!fired.contains(nextCell) && nextCell.row() > 0) {
                    fired.add(nextCell);
                    return nextCell;
                }
            }
        }
        return utils.GetRandomCell(fired);
    }

    //true : hit
    //false: miss
    public boolean HitCheck(Cell cell) { // check if the cell hit any ship
        if (IfCellHit(cell, porteAvionMap))
            return true;
        else if (IfCellHit(cell, croiseurMap))
            return true;
        else if (IfCellHit(cell, torpilleurMap))
            return true;
        else if (IfCellHit(cell, contreTorpilleurMap))
            return true;
        return IfCellHit(cell, contreTorpilleur2Map);
    }

    private boolean IfCellHit(Cell cell, Map<Cell, Boolean> ship) { // check if the cell hit the ship and get the received cell
        boolean hit = false;
        if (ship.containsKey(cell) && !received.contains(cell)) {
            ship.replace(cell, false, true);
            hit = true;
            received.add(cell);
        }
        return hit;
    }

    public boolean SunkCheck() { // check if any ship is sunk
        if (IfShipSunk(porteAvionMap))
            return true;
        if (IfShipSunk(croiseurMap))
            return true;
        if (IfShipSunk(torpilleurMap))
            return true;
        if (IfShipSunk(contreTorpilleurMap))
            return true;
        else return IfShipSunk(contreTorpilleur2Map);
    }

    private boolean IfShipSunk(Map<Cell, Boolean> ship) { // check if the ship is sunk
        if (!sunkShips.contains(ship) && !ship.containsValue(false)) {
            sunkShips.add(ship);
            return true;
        }
        return false;
    }
}
