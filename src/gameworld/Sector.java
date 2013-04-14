package gameworld;

import java.util.ArrayList;

public class Sector {
    public int[] position = new int[2];
    int terrain;        //hexadecimal terrain code
    int freeSpace;
    public ArrayList<EconUnit> units;
    
    public void setInfo(int newterrain, int newspace) {
    	terrain = newterrain;
    	freeSpace = newspace;
    }
    
    public String getInfo() {
    	return position[0] + " " + position[1] + " " + terrain + " " + freeSpace;
    }
    
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

    public boolean IsFlat()                     	{return (terrain & Terrain.MASK_HEIGHT) == 0;}
    public boolean IsBarren()                   	{return (terrain & Terrain.MASK_RESOURCES) == 0;}
    public void Reset()             				{terrain = 0;}
    public void SetType(int type, int mask)     	{terrain = terrain & ~mask; terrain = terrain | type;}
    public boolean IsOfType(int type, int mask) 	{return (terrain & mask) == type;}
    public boolean HasTypeOfMask(int mask)       	{return (terrain & mask) != 0;}

    public EconUnit GetUnitOfTypeOwnedBy(int type, int owner) {
    	System.out.printf("sector %d %d\n", position[0], position[1]);
    	for (int i = 0; i < units.size(); i++) {
    		if (units.get(i).type == type && units.get(i).owner == owner) {
    			System.out.printf("found unit at position %d\n",i);
    			return units.get(i);
    		}
			System.out.printf("unit at %d: %d %d compared to %d %d\n", i, units.get(i).type, units.get(i).owner, type, owner);
    	}
		System.out.printf("no units at new location\n");
    	return null;
    }

    public Sector(int x, int y) {
        position[0] = x;
        position[1] = y;
        terrain = 0;
        freeSpace = 400;
        units = new ArrayList<>();
    };
}