package gameworld;

import java.util.ArrayList;

public class Structure extends EconUnit {
    int garrison;             //number of units that can be held inside of this building for defensive purposes (or rather total size that can be taken up by units)
    int foodProd, woodProd;
    public ArrayList<EconUnit> units;
    
    public String getInfo() {
    	return owner + " " + type + " " + quantity + " ";
    }
    
    public Structure(int itype, World world, int player, int[] coords) {
        foodCost = 0;   woodCost = 0;
        foodProd = 0;   woodProd = 0;
        quantity = 1;
        
        owner = player;
        position = coords;
        type = itype;
        units = new ArrayList<>();
        
/*        switch (itype) {
            case Base.STRUCTURE_TOWNCENTRE: {
                woodCost = 40;
                garrison = 10;
                size = 150;
            }
            case Base.STRUCTURE_FARM: {
                woodCost = 25;
                foodProd = 6;
                garrison = 6;
                size = 60;
            }
            case Base.STRUCTURE_LUMBERMILL: {
                woodCost = 15;
                woodProd = 6;
                garrison = 4;
                size = 30;
            }
        }*/
    	this.AddToSector(world.GetSector(coords[0], coords[1]));
    }
}