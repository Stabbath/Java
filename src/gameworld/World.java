package gameworld;

import java.util.Random;

public class World {
	public int worldId;
    public int[] dimensions = new int[2];
    public Sector[][] map;
    public Player[] players;
    Random rand = new Random();

    //maybe create a function to find starting positions on the map according to certain params:
    //within r1 to r2 distance of iron, within r1 to r2 distance of fertile, within r1 to r2 distance of thick woods, etc
        
    private void CreatePoint(int x, int y, int type, int mask) {   //creates a single point of tile type at x,y         
        if (!map[x][y].HasTypeOfMask(mask)) {
            map[x][y].SetType(type, mask);
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
            map[x][y].SetType(type, mask);
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
                    map[x][y].SetType(type, mask);
                }
            } else
            if (random == 1) {
                if (x == dimensions[0] - 1 || map[x+1][y].HasTypeOfMask(mask)) {
                    bounded[1] = true;
                } else {
                    bounded[1] = false;
                    x++;
                    map[x][y].SetType(type, mask);
                }
            } else
            if (random == 2) {
                if (y == dimensions[1] - 1 || map[x][y+1].HasTypeOfMask(mask)) {
                    bounded[2] = true;
                } else {
                    bounded[2] = false;
                    y++;
                    map[x][y].SetType(type, mask);
                }
            } else
            if (random == 3) {
                if (y == 0 || map[x][y-1].HasTypeOfMask(mask)) {
                    bounded[3] = true;
                } else {
                    bounded[3] = false;
                    y--;
                    map[x][y].SetType(type, mask);
                }
            }
        }
    }
    
    public String getInfo() {
    	return worldId + " " + dimensions[0] + " " + dimensions[1] + " " + players.length;
    }

    public World(int x, int y, int playernum) {
        dimensions[0] = x;
        dimensions[1] = y;
        
        map = new Sector[x][y];
        players = new Player[playernum];
        
        int i, j;
        for (i = 0; i < playernum; i++) {
            players[i] = new Player(i);         //create players! to be reserved later and attributed names, passwords and sigils
        }

        for (i = 0; i < x; i++) {
            for (j = 0; j < y; j++) {
                map[i][j] = new Sector(i, j);
            }
        }
    }
    
    public void ShapeWorld() {
    	int i;
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
