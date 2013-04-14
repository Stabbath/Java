package server;

import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class ServerThread extends Thread {
    private Socket socket = null;

    public ServerThread(Socket socket) {
		super("ServerThread");
		this.socket = socket;
    }
    
    public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		    String cmdline;
		    
		    while ((cmdline = in.readLine()) != null) {
				String line;
    	    	if (cmdline.equals("need")) {		//need: client is requesting world data => send it
   			    	Charset charset = Charset.forName("UTF-8");
	    			try (BufferedReader reader = Files.newBufferedReader(Paths.get("/home/francisco/eclipse/world0"), charset)) {
	    			    while ((line = reader.readLine()) != null) {
	    			    	out.println(line);
	    			    }
	    			} catch (IOException e) {
	    			    System.err.format("IOException: %s%n", e);
	    			}
	    			out.println("done");
				} else
    	    	if (cmdline.equals("save")) {
    	    		Server.cmdSave();
    	    	} else
				if (cmdline.equals("quit")) {		//quit
					break;
			    } else {
			    	
			    }
			}
		    out.close();
		    in.close();
		    socket.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}