package fr.lernejo.navy_battle.GamePlay;

import fr.lernejo.navy_battle.Recorders.Cell;
import fr.lernejo.navy_battle.Recorders.ResponseMessageFire;

import java.util.*;


public class BattleField {
    public final Ship porteAvion = new Ship(new Cell(3, 1), 5, "Vertical");
    public final Ship croiseur = new Ship(new Cell(1, 1), 4, "Vertical");
    public final Ship torpilleur = new Ship(new Cell(8, 8), 2, "Vertical");
    public final Ship contreTorpilleur = new Ship(new Cell(0, 9), 3, "Horizontal");
    public final Ship contreTorpilleur2 = new Ship(new Cell(5, 5), 3, "Horizontal");

    public final Map<Cell, ResponseMessageFire> navalMap = new HashMap<Cell, ResponseMessageFire>();
    public final List<Cell> fired = new ArrayList<Cell>();
    final List<Cell> received = new ArrayList<Cell>();
    final List<Map<Cell, Boolean>> sunkShips = new ArrayList<Map<Cell, Boolean>>();
    public final CellGetter cellGetter = new CellGetter();

    public BattleField() {
        cellGetter.AddGoodPositions(porteAvion.mapShip, croiseur.mapShip, torpilleur.mapShip, contreTorpilleur.mapShip, contreTorpilleur2.mapShip);
    }
    public boolean ShipLeft() {
        return sunkShips.size() != 5;
    }

    public Cell GetNextShot() {
        return cellGetter.GetCellStanderWay(navalMap, fired);
//        return cellGetter.GetCellTactic(fired);
    }

    //true : hit - false: miss
    public boolean HitCheck(Cell cell) { // check if the cell hit any ship
        if (porteAvion.IsHit(cell, received))
            return true;
        else if (croiseur.IsHit(cell, received))
            return true;
        else if (torpilleur.IsHit(cell, received))
            return true;
        else if (contreTorpilleur.IsHit(cell, received))
            return true;
        return contreTorpilleur2.IsHit(cell, received);
    }

    public boolean SunkCheck() { // check if any ship is sunk
        if (porteAvion.IfSunk(sunkShips))
            return true;
        else if (croiseur.IfSunk(sunkShips))
            return true;
        else if (torpilleur.IfSunk(sunkShips))
            return true;
        else if (contreTorpilleur.IfSunk(sunkShips))
            return true;
        return contreTorpilleur2.IfSunk(sunkShips);
    }
}
