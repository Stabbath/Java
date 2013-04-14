package client;

//note for map generation: base structures on calculus functions! e.g. a área contida entre y=x^2 e y=sqrt(x), com possivelmente algumas variaçoes e massas geradas da forma actual. funcoes, funcoes, funcoes!

/*todo:
	add product name to the product class or add decoding function to return string based on product ID
	list players who have a presence in the sector on the header
	list attackable players based on sector on the menu
		maybe keep an array of booleans for players who are present at each sector, and update during move commands
	list trainable units based on the units and buildings present in the sector
	list buildable structures based on the units and buildings present in the sector
	
	make move command read the amount of each unit to move - create an int array on sector selection that has the size of the sector's unit arraylist, initialize all to zero, then update them when the document for their respective textarea is changed
		to keep track of that, might have to create an array of documents on sector selection as well. or ignore the int array and just fetch text from these when the command is issued, obey the command, and then reload the unit list
		also: maybe use slider instead? or a slider with insertable text. should try and find a way to stop players from trying to send too many units or negative numbers or having characters in the middle of the integer
	
	create and add art for other terrain types as compounds, drawn on top of already exiting tile types
	add selection square to map, automatically drawn every time the player selects a new sector - make it a medium-thickness yellow outline
	add player presence square fillings to map - leave it at green for your own units, blue for allies, grey for neutral, red for enemies

	adjust the increment in scrolling from the different alternatives: clicking on the arrow buttons, clicking on empty space, dragging the bar, ...

	add login interface after socket connection is established but before the world is generated - player name, player password, world id

	refine client-server interactions

	need a clientside config file that stores:
		the hostname of the server - maybe run game through hamachi?
		starting window size
*/

import gameworld.Person;
import gameworld.Sector;
import gameworld.World;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Client {
	public static String hostname = "francisco";
	public static Socket socket = null;
	public static PrintWriter out = null;
    public static BufferedReader in = null;

    public static World world;
    public static int clientPlayerId = 0;
	public static int tileLen = 60;
	public static Sector selectedSector;
	
	public static 	JFrame frame = new JFrame("Untitled Project");
	public static JPanel master	= new JPanel(new BorderLayout(10, 10));		//super-container for everything in the GUI (content pane)
	public static JPanel left		= new JPanel(new BorderLayout(10, 10));				//container for the entire left side of the GUI (canvas.west)
	public static JPanel midRight	= new JPanel();				//container for the unit list and their action menus (right.center)
	public static JPanel topRight	= new JPanel();				//container for the unit list and their action menus (right.center)
	public static JTextArea comText 	= new JTextArea();
	public static JTextArea sectorHeader;
	public static JTextArea cmdText	= new JTextArea();
	public static JPanel map = new JPanel(new BorderLayout(10, 10));						//container for the map's tile images (topLeft)
	public static JPanel right = new JPanel(new BorderLayout(10, 10));			//container for the entire right side of the GUI (canvas.east)
	public static JPanel botRight = new JPanel(new BorderLayout(10, 10));				//container for the command history and buttons (right.south)
	public static JScrollPane unitScroller = null;
	
	public static void updateSectorInfo() {
		right.remove(topRight);
		topRight.removeAll();
		sectorHeader = new JTextArea();
		sectorHeader.setEditable(false);
		sectorHeader.append("Sector: " + selectedSector.position[0] + ", " + selectedSector.position[1] + "; " +  "Players: ");
		topRight.add(new JScrollPane(sectorHeader), BorderLayout.WEST);
		topRight.add(createMenuBar(), BorderLayout.EAST);
		right.add(topRight, BorderLayout.NORTH);

		if (unitScroller != null) {
			right.remove(unitScroller);
		}
		midRight.removeAll();
		midRight.setLayout(new BoxLayout(midRight, BoxLayout.Y_AXIS));
		if (selectedSector.units.size() == 0) {
			midRight.add(new JPanel());
		} else {
			for (int i = 0; i < selectedSector.units.size(); i++) {
				JPanel panel = new JPanel(new BorderLayout());
				JTextArea unitLine = new JTextArea();
				unitLine.setEditable(false);
				panel.add(unitLine, BorderLayout.CENTER);
				JTextArea amount = new JTextArea();
				amount.setEditable(true);
				panel.add(amount, BorderLayout.EAST);
				midRight.add(panel);
				unitLine.append("Unit");
				amount.setText("0");
			}
			JPanel none = new JPanel();
			none.setPreferredSize(new Dimension(0, 2000));
			midRight.add(none);
		}
		unitScroller = new JScrollPane(midRight);
		right.add(unitScroller, BorderLayout.CENTER);
		right.revalidate();
		right.repaint();
	}
	
    public static JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, move;
        JMenuItem menuItem;
 
        //Create the menu bar.
        menuBar = new JMenuBar();
 
        //Build the first menu.
        menu = new JMenu("Orders");
        menuBar.add(menu);
 
        //movement submenu
        move = new JMenu("Move");
        menuItem = new JMenuItem("Northwest");
        move.add(menuItem);
        menuItem = new JMenuItem("North");
        move.add(menuItem);
        menuItem = new JMenuItem("Northeast");
        move.add(menuItem);
        menuItem = new JMenuItem("West");
        move.add(menuItem); 
        menuItem = new JMenuItem("East");
        move.add(menuItem); 
        menuItem = new JMenuItem("Southwest");
        move.add(menuItem); 
        menuItem = new JMenuItem("South");
        move.add(menuItem); 
        menuItem = new JMenuItem("Southeast");
        move.add(menuItem); 
        menu.add(move);
 
        //attack command
        move = new JMenu("Attack");
        menuItem = new JMenuItem("Player 1");
        move.add(menuItem);
        menuItem = new JMenuItem("Player 2");
        move.add(menuItem);
        menu.add(move);
        
        //build
        move = new JMenu("Build");
        menuItem = new JMenuItem("Nothing");
        move.add(menuItem);
        menu.add(move);
        
        //train
        move = new JMenu("Train");
        menuItem = new JMenuItem("Nothing");
        move.add(menuItem);
        menu.add(move);
          
        return menuBar;
    }

    static void processIncomingWorld() throws IOException {
		String[] block = new String[3];
		String[] buffer = new String[8];
		int x = 0, y = 0;
		int stackPtr = -1;

        String line;
        while ((line = in.readLine()) != null) {
        	if (line.equals("-end-")) {
	    		--stackPtr;
	    	} else
		    	if (line.equals("-sectors-")) {
		    		block[++stackPtr] = "sectors";
		    	} else
		    	if (line.equals("-world-")) {
		    		block[++stackPtr] = "world";
		    	} else 
		    	if (line.equals("-units-")) {
		    		block[++stackPtr] = "units";
		    	} else
	    	if (stackPtr >= 0) {
	    		if (block[stackPtr] == "sectors") {
		    		buffer = line.split(" ");
		    		x = Integer.parseInt(buffer[0]);
		    		y = Integer.parseInt(buffer[1]);
		    		world.map[x][y].setInfo(Integer.parseInt(buffer[2]), Integer.parseInt(buffer[3]));
	    		} else
	    		if (block[stackPtr] == "world") {
	    			buffer = line.split(" ");
	    			world = new World(Integer.parseInt(buffer[1]), Integer.parseInt(buffer[2]), Integer.parseInt(buffer[3]));
	    			world.worldId = Integer.parseInt(buffer[0]);
	    		}
	    		if (block[stackPtr] == "units") {
	    			buffer = line.split(" ");
	    			new Person(Integer.parseInt(buffer[1]), world, Integer.parseInt(buffer[0]), x, y, Integer.parseInt(buffer[2]));
	    		}
	    	} else
	    	if (line.equals("done")){
	    		break;
	    	}
        }
    }
    
    public static void main(String[] args) throws IOException {
		//small UI for text input
    	//request login
    	//if success, continue, else say invalid details

        try {
            socket = new Socket(hostname, 3525);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Couldn't find host: " + hostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + hostname);
            System.exit(1);
        }

        out.println("save");

        out.println("need");	//request world

        processIncomingWorld();
        
        selectedSector = world.GetSector(1, 1);

    	map.setPreferredSize(new Dimension(world.dimensions[0]*tileLen, world.dimensions[1]*tileLen));	//so scrollpane will actually do something
        map.add(new MapPainter());
        map.addMouseListener(new MouseListener() {
        	int x, y;
        	public void mousePressed(MouseEvent e) {
        		x = e.getX()/Client.tileLen;
        		y = (map.getHeight() - e.getY())/Client.tileLen;
        	}
        	public void mouseReleased(MouseEvent e) {
        		if (x == e.getX()/Client.tileLen && y == (map.getHeight() - e.getY())/Client.tileLen) {
        			Client.selectedSector = Client.world.GetSector(x, y);
        			Client.updateSectorInfo();
        		}
        	}
			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
        });
		left.add(new JScrollPane(map), BorderLayout.CENTER);	//center so it will freely expand
		
		comText.setPreferredSize(new Dimension(200, 200));
		comText.setEditable(false);
		comText.append("Welcome to Untitled Project!\n");
		left.add(new JScrollPane(comText), BorderLayout.SOUTH);

		cmdText.setEditable(false);
		cmdText.setPreferredSize(new Dimension(350, 200));
		cmdText.append("Command History");
		botRight.add(new JScrollPane(cmdText), BorderLayout.NORTH);
		botRight.add(new JButton("Undo"), BorderLayout.WEST);
		botRight.add(new JButton("Commit"), BorderLayout.EAST);
		right.add(botRight, BorderLayout.SOUTH);

		updateSectorInfo();

		right.setPreferredSize(new Dimension(350, 0));
		master.add(right, BorderLayout.EAST);
		master.add(left, BorderLayout.CENTER);	//center so it will freely expand

    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(master);
    	frame.setSize(800, 600);
    	frame.setVisible(true);
        
        out.println("quit");

         /*        String cmdline = "";
    	while (true) {
    		if (cmdline.equals("done")) {
    			//send unit commands to server
    			//clear command buffer
    		}
    		if (cmdline.equals("quit")) {
    			//if command buffer not empty
    				//warn client, offer 3 options: "Cancel", "Commit & Quit", "Quit"
    				//wait for command
    			if (cmdline.equals("back")) {
    				continue;
    			}
    			if (cmdline.equals("done")) {
        			//send unit commands to server
    			}
    			break;
    		}
    	}*/

    	out.close();
        in.close();
        socket.close();
    }
}