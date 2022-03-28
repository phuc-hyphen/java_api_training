package fr.lernejo.navy_battle;

import fr.lernejo.navy_battle.Recorders.Cell;
import fr.lernejo.navy_battle.Recorders.ResponseMessageFire;

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
    final CellGetter cellGetter = new CellGetter();

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
        cellGetter.AddGoodPositions(porteAvionMap, croiseurMap, torpilleurMap, contreTorpilleurMap, contreTorpilleur2Map);
    }

    public boolean ShipLeft() {
        return sunkShips.size() != 5;
    }

    public Cell GetNextShot() {
//        return cellGetter.GetCellStanderWay(navalMap, fired);
        return cellGetter.GetCellTactic(fired);
    }

    //true : hit - false: miss
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
