/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eternalwar;

import java.util.ArrayList;
import java.util.Random;


/**
 *
 * @author Sr. Francisco
 */
public class BaseEntities {
    /* to-do later:
     *  add hunter's lodge building to allow food collection in wooded areas as well as leather production
     *  add hunters, tanners and butchers to work at hunters' lodges - to gather animals, produce leather, and produce meat
     * 
     * 
     *  
    */
    public static final int STRUCTURE_TOWNCENTRE    = 0;    //main building. Produces workers and brawlers.
    public static final int STRUCTURE_FARM          = 1;    //food production building
    public static final int STRUCTURE_LUMBERMILL    = 2;    //wood production building
    public static final int PERSON_WORKER           = 3;    //general-purpose builder and harvester
    public static final int PERSON_BRAWLER          = 4;    //basic army unit
}

class Unit extends Product {
    int health, maxhealth, defense;         //damage unit can take before dying, and damage reduction per attack received
    int damage, atkSpeed;       //damage dealt per attack and number of attacks per turn
    int[] position = new int[2];     //position on 2d map
    int size;   //how much of the terrain tile this unit will take up, or how much room of the garrison when in a building
}

class Upgrade extends Product {

}

class Product {
    int owner;          //player who owns unit
    int parent;            //identifier of parent product (ie where the upgrade is being researched or where the person is garrisoned, or who is building the structure
    int globalId;           //global id for this product
    int type;               //type according to global PERSON_ or STRUCTURE_ or UPGRADE_ indexes
    int rank;               //higher ranks are stronger or give greater bonuses
    int productionProgress; //number of production turns that have passed
    int productionTime; //number of turns required to produce
    int foodCost;       //food required per turn to keep alive
    int woodCost;       //wood required per turn to produce
    
    public boolean IsInProduction() {
        return (productionProgress < productionTime);
    }
    public void AdvanceProduction() {
        productionProgress++;
    }
}

class Player {
    int id;
    int[] colours = new int[2];
    String name = new String(new char[32]);
    String pass = new String(new char[32]);
    String sigil = new String(new char[32]);
    
    int food;
    int wood;
    
    public Player(int index) {
        id = index;
        name = "";
        food = 0;
        wood = 0;
    }
}

class World {
    int nextId;
    int[] dimensions;
    Sector[][] map;
    Player[] players;
    ArrayList<Structure> structures;
    ArrayList<Person> people;
    Random rand = new Random();                                    

    //maybe create a function to find starting positions on the map according to certain params:
    //within r1 to r2 distance of iron, within r1 to r2 distance of fertile, within r1 to r2 distance of thick woods, etc
    
    
    private void CreatePoint(int x, int y, int type, int mask) {   //creates a single point of tile type at x,y         
        if (!map[x][y].HasTypeOfMask(mask)) {
            map[x][y].SetType(type);
        }
    }
    
    //to-do: add "int max" parameter to determine maximum number of tiles for mass
    //maybe try to have an "int min" again, by saving past tiles in a structure of some sort to then expand from them once the previous expansion has reached unpassable bounds
    //maybe consider limiting the number of times a mass can expand in a single direction
    private void CreateMass(int Cx, int Cy, int type, int mask) {   //creates a mass of tile type starting at Cx,Cy         
        boolean[] bounded = new boolean[4];
        for (int n = 0; n < bounded.length; n++) { bounded[n] = false; }
        
        int x = Cx, y = Cy;
        if (!map[x][y].HasTypeOfMask(mask)) {   //start mass at Cx, Cy
            map[x][y].SetType(type);
        }

        int random;
        while (!(bounded[0] && bounded[1] && bounded[2] && bounded[3])){  //keep expanding until randomness says to stop
            random = rand.nextInt(4);
            if (random == 0) {
                if (x == 0 || map[x-1][y].HasTypeOfMask(mask)) {
                    bounded[0] = true;
                } else {
                    bounded[0] = false;
                    x--;
                    map[x][y].SetType(type);
                }
            } else
            if (random == 1) {
                if (x == dimensions[0] - 1 || map[x+1][y].HasTypeOfMask(mask)) {
                    bounded[1] = true;
                } else {
                    bounded[1] = false;
                    x++;
                    map[x][y].SetType(type);
                }
            } else
            if (random == 2) {
                if (y == dimensions[1] - 1 || map[x][y+1].HasTypeOfMask(mask)) {
                    bounded[2] = true;
                } else {
                    bounded[2] = false;
                    y++;
                    map[x][y].SetType(type);
                }
            } else
            if (random == 3) {
                if (y == 0 || map[x][y-1].HasTypeOfMask(mask)) {
                    bounded[3] = true;
                } else {
                    bounded[3] = false;
                    y--;
                    map[x][y].SetType(type);
                }
            }
        }
    }
    
    public World(int[] size, int playernum) {
        if (size.length == 2) {
            nextId = 0;
            dimensions = size;
            
            map = new Sector[size[0]][size[1]];
            players = new Player[playernum];
            people = new ArrayList<>();
            structures = new ArrayList<>();
            
            int i, j;
            for (i = 0; i < playernum; i++) {
                players[i] = new Player(i);         //create players! to be reserved later and attributed names, passwords and sigils
            }

            for (i = 0; i < size[0]; i++) {
                for (j = 0; j < size[1]; j++) {     //set all of the land to flat
                    map[i][j] = new Sector(i, j);
                    map[i][j].Reset();
                }
            }

            for (i = 0; i < Math.sqrt(dimensions[0]*dimensions[1]/3); i++) {  //create seas
                CreateMass(rand.nextInt(dimensions[0]), rand.nextInt(dimensions[1]), Terrain.TERRAIN_WATER_SEA, Terrain.MASK_HEIGHT);
            }
            for (i = 0; i < Math.sqrt(dimensions[0]*dimensions[1]/30); i++) {  //create some hills
                CreateMass(rand.nextInt(dimensions[0]), rand.nextInt(dimensions[1]), Terrain.TERRAIN_HILLS, Terrain.MASK_HEIGHT);
            }
            for (i = 0; i < Math.sqrt(dimensions[0]*dimensions[1]/40); i++) {  //create some mountains
                CreateMass(rand.nextInt(dimensions[0]), rand.nextInt(dimensions[1]), Terrain.TERRAIN_MOUNTAINS, Terrain.MASK_HEIGHT);
            }
            for (i = 0; i < Math.sqrt(dimensions[0]*dimensions[1]/30); i++) {  //create some light forests
                CreateMass(rand.nextInt(dimensions[0]), rand.nextInt(dimensions[1]), Terrain.TERRAIN_WOODS_LIGHT, Terrain.MASK_RESOURCES);
            }
            for (i = 0; i < Math.sqrt(dimensions[0]*dimensions[1]/40); i++) {  //create some dense forests
                CreateMass(rand.nextInt(dimensions[0]), rand.nextInt(dimensions[1]), Terrain.TERRAIN_WOODS_THICK, Terrain.MASK_RESOURCES);
            }
            for (i = 0; i < Math.sqrt(dimensions[0]*dimensions[1]/30); i++) {  //create some fertile land
                CreateMass(rand.nextInt(dimensions[0]), rand.nextInt(dimensions[1]), Terrain.TERRAIN_FERTILE, Terrain.MASK_RESOURCES);
            }
            for (i = 0; i < Math.sqrt(2*dimensions[0]*dimensions[1]); i++) {  //create some iron-rich tiles
                CreatePoint(rand.nextInt(dimensions[0]), rand.nextInt(dimensions[1]), Terrain.TERRAIN_IRON, Terrain.MASK_RESOURCES);
            }
        }
    }
    
    public int ReservePlayerSlot(char[] name, char[] pass, char[] sigil) {
        int id;
        for (id = 0; id < players.length; id++) {
            if ("".equals(players[id].name)) {
                break;
            }
        }
        players[id].name = new String(name);
        players[id].pass = new String(pass);
        players[id].sigil = new String(sigil);
        
        return id < players.length ? id : -1;
    }

    public Sector GetSector(int x, int y) {
        if (x < dimensions[0] && y < dimensions[1]) {
            return map[x][y];
        }
        return new Sector(-1, -1);
    }
    
    public boolean IsInMap(int x, int y) {
        return (x >= 0 && x < dimensions[0]) && (y >= 0 && y < dimensions[1]);
    }
}

