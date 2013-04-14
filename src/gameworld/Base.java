package gameworld;

import java.util.Random;

/**
 *
 * @author Sr. Francisco
 */
public class Base {
    public static final int[] DIRECTION_LEFT = {0,-1};
    public static final int[] DIRECTION_RIGHT = {0,1};
    public static final int[] DIRECTION_UP = {1,0};
    public static final int[] DIRECTION_DOWN = {-1,0};

    public static final int PERSON_WORKER           = 0;    //general-purpose builder and harvester

    public static final int mapSizeX = 200;
    public static final int mapSizeY = 100;
    public static final int playernum = 4;
    
    static Random rand = new Random();
}

class ArmyUnit extends ArmyProduct {
    int damage, atkSpeed;       //damage dealt per attack and number of attacks per turn	
}

class EconUnit extends EconProduct {
    int[] position = new int[2];     //position on 2d map
    public int size, quantity;   //how much of the terrain tile will each of these units take up, or how much room of the garrison when in a building; how many of this unit are present

    public void AddToSector(Sector sector) { sector.units.add(this); }
    public void AddToStructure(Structure structure) { structure.units.add(this); }
    public void RemoveFromSector(Sector sector) { sector.units.remove(this); }
}

class ArmyProduct extends Product {
    int health, maxhealth, defense;         //damage unit can take before dying, and damage reduction per attack received
}

class EconProduct extends Product {
    int[] products;
    int foodCost;       //food required per turn to keep alive
    int woodCost;       //wood required per turn to produce
}

class Product {
    public int owner;          //player who owns unit
    public int type;               //type according to global PERSON_ or STRUCTURE_ or UPGRADE_ indexes
}

/* to-do later:
 *  add hunter's lodge building to allow food collection in wooded areas as well as leather production
 *  add hunters, tanners and butchers to work at hunters' lodges - to gather animals, produce leather, and produce meat*/

