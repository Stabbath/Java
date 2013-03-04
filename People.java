/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eternalwar;

/**
 *
 * @author Sr. Francisco
 */
public class People {
    public static final int[] DIRECTION_LEFT = {0,-1};
    public static final int[] DIRECTION_RIGHT = {0,1};
    public static final int[] DIRECTION_UP = {1,0};
    public static final int[] DIRECTION_DOWN = {-1,0};
}

class Person extends Unit {
    int movSpeed, movesDone;           //moves per turn; how many moves have been used this turn
    int atkRange, sightRange;     //how far away can the unit attack
    
    public boolean Move(World world, int[] direction) {
        int x = position[0] + direction[0];
        int y = position[1] + direction[1];
        if (world.IsInMap(x, y)) {
            Sector bufLoc = world.GetSector(x, y);
            if (bufLoc.freeSpace > size && movesDone < movSpeed) {
                bufLoc.freeSpace += size;   //block space in new location
                world.GetSector(position[0], position[1]).freeSpace -= size;    // free space in previous location
                position[0] = x;              //move unit
                position[1] = y;              //
                movesDone++;                //increment number of moves done this turn
                return true;
            }
        }
        return false;
    }

    public boolean CanAttack(World world) {
        if (world.GetSector(position[0], position[1]).HasUnitsNotOwnedBy(owner)) {
            return true;
        }
        return false;
    }

    private void AddToSector(Sector sector) { sector.people.add(this); }
    private void AddToStructure(Structure structure) { structure.people.add(this); }

    public Person(int itype, World world, int irank, int player, int[] coords, int parent) {
        health = 1;     maxhealth = 1;      defense = 0;
        damage = 0;     atkSpeed = 0;
        movSpeed = 1;
        foodCost = 1;   woodCost = 0;
        productionTime = 0;
        productionProgress = 0;
        size = 1;
        
        owner = player;
        position = coords;
        rank=irank;
        globalId=world.nextId++;
        type = itype;

        switch (itype) {
            case BaseEntities.PERSON_WORKER: {
                health = 3;     maxhealth = 3;      defense = 0;
                damage = 1;     atkSpeed = 1;
                size = 1;
                foodCost = 1;   woodCost = 0;
                productionTime = 1;
                movSpeed = 2;
            }
            case BaseEntities.PERSON_BRAWLER: {
                health = 5;     maxhealth = 5;      defense = 0;
                damage = 1;     atkSpeed = 1;
                size = 1;
                foodCost = 1;   woodCost = 0;
                productionTime = 2;
                movSpeed = 2;
            }
        }
        
        this.AddToSector(world.GetSector(coords[0], coords[1]));
    }
}