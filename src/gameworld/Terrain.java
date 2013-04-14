package gameworld;

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