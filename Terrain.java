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
public class Terrain {
    //resource/water related
    public static final int MASK_RESOURCES      = 0x000F;   //
    public static final int TERRAIN_FERTILE     = 0x0001;   //normal grassland  +2 food
    public static final int TERRAIN_WOODS_LIGHT = 0x0002;   //lightly forested  +1 food +1 wood -1 sight -20 space
    public static final int TERRAIN_WOODS_THICK = 0x0003;   //thickly forested  +2 wood -2 sight -45 space
    public static final int TERRAIN_IRON        = 0x0004;   //iron-rich terrain +1 iron
    public static final int TERRAIN_STONY       = 0x0005;   //stony terrain     +2 stone -1 food
    //hill and water related
    public static final int MASK_HEIGHT         = 0x00F0;   //
    public static final int TERRAIN_HILLS       = 0x0010;   //hills             +1 ston -10 space
    public static final int TERRAIN_MOUNTAINS   = 0x0020;   //tall mountains    +2 ston -35 space
    public static final int TERRAIN_CHASM       = 0x0030;   //chasm             -100 space
    public static final int TERRAIN_WATER_LAKE  = 0x0040;   //inland water
    public static final int TERRAIN_WATER_COAST = 0x0050;   //coastal water, close to land
    public static final int TERRAIN_WATER_SEA   = 0x0060;   //sea water, distant from land
    //complementary
    public static final int MASK_EXTRA          = 0x0F00;   //
    public static final int TERRAIN_RIVER       = 0x0100;   //river +1 food -30 space or -20 if tile has other space decreasers

    //weather related - leave for later
    //    public static final int TERRAIN_SNOWY       = 0x10000;   //sea water, distant from land
}

class Sector {
    int[] position = new int[2];
    int terrain;        //hexadecimal terrain code
    ArrayList<Structure> structures;
    ArrayList<Person> people;
    int freeSpace;

    public boolean IsFertile()      {return (terrain & Terrain.MASK_RESOURCES) == Terrain.TERRAIN_FERTILE;}
    public boolean IsLightlyWooded(){return (terrain & Terrain.MASK_RESOURCES) == Terrain.TERRAIN_WOODS_LIGHT;}
    public boolean IsThicklyWooded(){return (terrain & Terrain.MASK_RESOURCES) == Terrain.TERRAIN_WOODS_THICK;}
    public boolean IsIronRich()     {return (terrain & Terrain.MASK_RESOURCES) == Terrain.TERRAIN_IRON;}
    public boolean IsStony()        {return (terrain & Terrain.MASK_RESOURCES) == Terrain.TERRAIN_STONY;}
    public boolean IsHilly()        {return (terrain & Terrain.MASK_HEIGHT) == Terrain.TERRAIN_HILLS;}
    public boolean IsMountainous()  {return (terrain & Terrain.MASK_HEIGHT) == Terrain.TERRAIN_MOUNTAINS;}
    public boolean IsChasm()        {return (terrain & Terrain.MASK_HEIGHT) == Terrain.TERRAIN_CHASM;}
    public boolean IsRiver()        {return (terrain & Terrain.MASK_EXTRA) == Terrain.TERRAIN_RIVER;}
    public boolean IsCoast()        {return (terrain & Terrain.MASK_HEIGHT) == Terrain.TERRAIN_WATER_COAST;}
    public boolean IsLake()         {return (terrain & Terrain.MASK_HEIGHT) == Terrain.TERRAIN_WATER_LAKE;}
    public boolean IsSea()          {return (terrain & Terrain.MASK_HEIGHT) == Terrain.TERRAIN_WATER_SEA;}

    public void MakeFertile()        {terrain = terrain | Terrain.TERRAIN_FERTILE;}
    public void MakeLightlyWooded()  {terrain = terrain | Terrain.TERRAIN_WOODS_LIGHT;}
    public void MakeThicklyWooded()  {terrain = terrain | Terrain.TERRAIN_WOODS_THICK;}
    public void MakeIronRich()       {terrain = terrain | Terrain.TERRAIN_IRON;}
    public void MakeStony()          {terrain = terrain | Terrain.TERRAIN_STONY;}
    public void MakeHilly()          {terrain = terrain | Terrain.TERRAIN_HILLS;}
    public void MakeMountainous()    {terrain = terrain | Terrain.TERRAIN_MOUNTAINS;}
    public void MakeChasm()          {terrain = terrain | Terrain.TERRAIN_CHASM;}
    public void MakeCoast()          {terrain = terrain | Terrain.TERRAIN_WATER_COAST;}
    public void MakeLake()           {terrain = terrain | Terrain.TERRAIN_WATER_LAKE;}
    public void MakeSea()            {terrain = terrain | Terrain.TERRAIN_WATER_SEA;}
    public void MakeRiver()          {terrain = terrain | Terrain.TERRAIN_RIVER;}

    public boolean IsFlat()                         {return (terrain & Terrain.MASK_HEIGHT) == 0;}
    public boolean IsBarren()                       {return (terrain & Terrain.MASK_RESOURCES) == 0;}
    public void Reset()                             {terrain = 0;}
    public void SetType(int type)                   {terrain = terrain | type;} //should probably ask for mask and reset previous value for that mask
    public boolean IsOfType(int type, int mask)     {return (terrain & mask) == type;}
    public boolean HasTypeOfMask(int mask)          {return (terrain & mask) != 0;}

    public boolean HasUnitsNotOwnedBy(int player) {
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).owner != player) {
                return true;
            }
        }
        return false;
    }
    
    public Sector(int x, int y) {
        position[0] = x;
        position[1] = y;
        structures = new ArrayList<>();
        people = new ArrayList<>();
        terrain = 0;
        freeSpace = 100;
    };
}
