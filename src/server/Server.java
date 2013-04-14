package server;

/*files:
	worlds:	//stores world ids
		#id #x #y #playernum
	
	later on store all client commands into file, to allow replays
*/

/* client cmds:
need - request world data
done - means the server has finished sending info
quit - tell the server client is quitting
make - ask the server to create a new world
load - ask the server to load an inactive world
stop - ask the server to unload an active world
save - ask the server to save all world data to files
*/

/*	game cmds:
* 			gets ID of player with name #name and pass #pass. if no ID is found, returns a player validation error
* 		move #name #pass #sectorX #sectorY #unitType #quantity #directionX #directionY
* 			gets unit object of type #unitType at sector of coordinades #sextorX #sectorY. if none can be got, returns a unit not found error
* 			moves unit
* 		attack #name #pass #sectorX #sectorY									//in an attack action, units owned by the player at position x, y will enter a battle with all other units on that sector
* 			checks if there are both player-owned and unfriendly units in the sector of coordinates #sectorX #sectorY
* 			starts battle
* 		produce #name #pass #sectorX #sectorY #unitType #quantity
*			checks product list for all units in sector of coordinates #sectorX #sectorY, checks how many of #unitType could be produced across all units
*			sees if #quantity is possible based on units already being produced by each building that can produce desired unit, if not errors
*			begins production
*/

import gameworld.*;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/* What is done:
 * Worlds:
 * 		map generation
 * 		saving and loading
 * Network:
 * 		world is sent to client
 * 
 * To-do:
 * 		add 1 unit
 * 		add unit commands
 * 		refine client-server commands
 */

public class Server {
	public static boolean running = true;
	public static String[] adminInfo = {"admin", "default"};
	public static ArrayList<World> worlds = new ArrayList<>();
	
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
    	System.out.printf("Server starting...\n");

        worlds.add(new World(15, 15, 1));
    	worlds.get(0).ShapeWorld();
    	System.out.printf("World created.\n");
    	
    	Person lol = new Person(Base.PERSON_WORKER, worlds.get(0), 0, 4, 4, 1);
    	System.out.printf("Person created: %d %d %d.\n", lol.type, lol.owner, lol.quantity);
    	lol = new Person(Base.PERSON_WORKER, worlds.get(0), 0, 1, 1, 3);
    	System.out.printf("Person created: %d %d %d.\n", lol.type, lol.owner, lol.quantity);
    	
        try {
            serverSocket = new ServerSocket(3525);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 3525.");
            System.exit(-1);
        }
        
    	System.out.printf("Server is ready.\n");
        
        while (running) {
    	    new ServerThread(serverSocket.accept()).start();
	    	System.out.printf("Connection established.\n");
        }
        
        serverSocket.close();
        System.exit(0);
    }

    public static void shutdown() {
    	running = false;
    }
    
    public static void cmdSave() {
    	int n, m, i, j;
    	Person person;

		for (i = 0; i < worlds.size(); i++) {
			try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("/home/francisco/eclipse/" + "world" + i), Charset.forName("UTF-8"))) {
				writer.write("-world-\n");
				writer.write(worlds.get(i).getInfo() + "\n");
				writer.write("-end-\n");
				
				writer.write("-sectors-\n");
				for (n = 0; n < worlds.get(i).map.length; n++) {			//loop through all sectors
					for (m = 0; m < worlds.get(i).map[n].length; m++) {		//
						writer.write(worlds.get(i).map[n][m].getInfo() + "\n");
						writer.write("-units-\n");
						for (j = 0; j < worlds.get(i).map[n][m].units.size(); j++) {
							person = (Person) worlds.get(i).map[n][m].units.get(j);
							writer.write(person.getInfo() + "\n");
						}
						writer.write("-end-\n");
					}
				}
				writer.write("-end-\n");
			} catch (IOException x) {
			    System.err.format("IOException: %s%n", x);
			}
		}
	}

    public static void cmdLoad(String[] cmdbuffer) {
		int id = Integer.parseInt(cmdbuffer[3]);
		String[] block = new String[3];
		String[] buffer = new String[8];
		String line = null;
		int x = 0, y = 0;
		int stackPtr = -1;
		World world = null;
		
		Charset charset = Charset.forName("UTF-8");
		try (BufferedReader reader = Files.newBufferedReader(Paths.get("world" + id), charset)) {
		    while ((line = reader.readLine()) != null) {
		    	if (line.equals("-end-")) {
		    		--stackPtr;
		    	} else
		    	if (block[stackPtr] == "sectors") {
		    		buffer = line.split(" ");
		    		x = Integer.parseInt(buffer[0]);
		    		y = Integer.parseInt(buffer[1]);
		    		world.map[x][y].setInfo(Integer.parseInt(buffer[2]), Integer.parseInt(buffer[3]));
		    	} else
		    	if (block[stackPtr] == "units") {	//owner type quantity
		    		buffer = line.split(" ");
		    		world.map[x][y].units.add(new Person(Integer.parseInt(buffer[1]), world, Integer.parseInt(buffer[0]), x, y, Integer.parseInt(buffer[2])));
		    	} else
		    	if (block[stackPtr] == "world") {
		    		buffer = line.split(" ");
		    		world = new World(Integer.parseInt(buffer[1]), Integer.parseInt(buffer[2]), Integer.parseInt(buffer[3]));
		    		world.worldId = Integer.parseInt(buffer[0]);
		    	} else
		    	if (line.equals("-sectors-")) {
		    		block[++stackPtr] = "sectors";
		    	} else
		    	if (line.equals("-units-")) {
		    		block[++stackPtr] = "units";
		    	} else
		    	if (line.equals("-world-")) {
		    		block[++stackPtr] = "world";
		    	} else {
		    		//error
		    	}
		    }
		} catch (IOException e) {
		    System.err.format("IOException: %s%n", e);
		}
	
		worlds.add(world);
	}
}