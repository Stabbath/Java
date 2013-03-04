/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eternalwar;

import java.util.ArrayList;

/**
 *
 * @author Sr. Francisco
 */
public class Structures {

}

class Structure extends Unit {
    int productionSpeed;      //number of units/upgrades that can be produced per turn
    int garrison;             //number of units that can be held inside of this building for defensive purposes (or rather total size that can be taken up by units)
    int foodProd, woodProd;
    ArrayList<Person> people;
    
    private void AddToSector(Sector sector) { sector.structures.add(this); }

    public Structure(int itype, World world, int irank, int player, int[] coords) {
        foodCost = 0;   woodCost = 0;
        foodProd = 0;   woodProd = 0;
        productionSpeed = 0;
        productionTime = 0;
        productionProgress = 0;
        
        damage = 0;     atkSpeed = 0;
        owner = player;
        position = coords;
        rank=irank;
        globalId=world.nextId++;
        type = itype;
        people = new ArrayList<>();
        
        switch (itype)
        {
            case BaseEntities.STRUCTURE_TOWNCENTRE: {
                health = 100+30*irank;  maxhealth = health;     defense = 2+1*irank;
                woodCost = 40+30*irank;
                productionTime = 10+2*irank;
                productionSpeed = 2+5*irank/3;
                garrison = 10+3*irank;
                size = 50+5*irank;
            }
            case BaseEntities.STRUCTURE_FARM: {
                health = 18+6*irank;  maxhealth = health;       defense = 0+1*irank/4;
                woodCost = 25+15*irank;
                foodProd = 6+3*irank;
                productionTime = 5+2*irank;
                garrison = 6+2*irank;
                size = 30+5*irank;
            }
            case BaseEntities.STRUCTURE_LUMBERMILL: {
                health = 18+6*irank;  maxhealth = health;       defense = 0+1*irank/4;
                woodCost = 15+10*irank;
                woodProd = 6+3*irank;
                productionTime = 4+2*irank;
                garrison = 4+2*irank;
                size = 15+2*irank;
            }
            
            this.AddToSector(world.GetSector(coords[0], coords[1]));
        }
    }
}