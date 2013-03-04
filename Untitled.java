/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eternalwar;

import java.util.Random;

/**
 *
 * @author Sr. Francisco
 */
public class Untitled {
    public static final int mapSize[] = new int[] {200, 100};
    public static final int playernum = 4;
    
    static Random rand = new Random();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        World world = new World(mapSize, playernum);
        int[]buffer = new int[] {0,0};
        Person person1 = new Person(BaseEntities.PERSON_BRAWLER, world, 0, 0, buffer, 0);
        Person person2 = new Person(BaseEntities.PERSON_BRAWLER, world, 0, 1, buffer, 0);
        buffer[0] = 1; buffer[1] = 1;
        Person person3 = new Person(BaseEntities.PERSON_BRAWLER, world, 0, 0, buffer, 0);
        Person person4 = new Person(BaseEntities.PERSON_WORKER, world, 0, 1, buffer, 0);
        
        int x, y;
        Sector bufLoc;
/*        for (y = world.dimensions[1] - 1; y >= 0; y--) {                
           for (x = 0; x < world.dimensions[0]; x++) {
                bufLoc = world.GetSector(x, y);
                if (!bufLoc.people.isEmpty()) {
                    System.out.printf("%d %d", bufLoc.people.get(0).type, bufLoc.people.get(1).type);
                }
            }
            System.out.printf("\n");
        }
*/
        for (y = world.dimensions[1] - 1; y >= 0; y--) {
           for (x = 0; x < world.dimensions[0]; x++) {
                bufLoc = world.GetSector(x, y);
                System.out.printf("%s", bufLoc.IsSea() ? "~" :
                        bufLoc.IsBarren() ? " " :
                        bufLoc.IsLightlyWooded() ? "t" :
                        bufLoc.IsThicklyWooded() ? "T" :
                        bufLoc.IsFertile() ? "f" :
                        bufLoc.IsIronRich() ? "i" :
                        "?");
            }
            System.out.printf("\n");
        }
            System.out.printf("\n");
            System.out.printf("\n");
            System.out.printf("\n");
            System.out.printf("\n");

        for (y = world.dimensions[1] - 1; y >= 0; y--) {                
           for (x = 0; x < world.dimensions[0]; x++) {
                bufLoc = world.GetSector(x, y);
                System.out.printf("%s", bufLoc.IsFlat() ? " " :
                        bufLoc.IsSea() ? "~" :
                        bufLoc.IsHilly() ? "n" :
                        bufLoc.IsMountainous() ? "M" :
                        "?");
            }
            System.out.printf("\n");
        }

    }
}


