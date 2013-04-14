package client;

import gameworld.Person;
import gameworld.Sector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MapPainter extends JPanel{
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		BufferedImage[] img = new BufferedImage[32];
		try {
			img[0] = ImageIO.read(new File("/home/francisco/eclipse/tilePlain.png"));
			img[1] = ImageIO.read(new File("/home/francisco/eclipse/tileWater.png"));
			img[2] = ImageIO.read(new File("/home/francisco/eclipse/tileHills.png"));
			img[3] = ImageIO.read(new File("/home/francisco/eclipse/tileMountains.png"));
		} catch (IOException e) {
		}
		
		Sector bufLoc;
		for (int y = Client.world.dimensions[1] - 1; y >= 0; y--) {
			for (int x = 0; x <= Client.world.dimensions[0]; x++) {
				bufLoc = Client.world.GetSector(x, y);
				if (bufLoc.IsSea()) {
					g2d.drawImage(img[1], Client.tileLen*x, Client.tileLen*y, null);
				} else 
				if (bufLoc.IsHilly()) {
					g2d.drawImage(img[2], Client.tileLen*x, Client.tileLen*y, null);
				} else
				if (bufLoc.IsMountainous()) {
					g2d.drawImage(img[3], Client.tileLen*x, Client.tileLen*y, null);					
				} else {
					g2d.drawImage(img[0], Client.tileLen*x, Client.tileLen*y, null);
				}
				
				boolean self = false, foe = false, ally = false, neutral = false;
				for (int i = 0; i < bufLoc.units.size(); i++) {
					if (foe) {
						g2d.setColor(new Color(200,50,50,100));
						g2d.fillRect(Client.tileLen*x + 3, Client.tileLen*y + 3, Client.tileLen - 6, Client.tileLen - 6);
						foe = true;
					} else
					if (neutral) {
						g2d.setColor(new Color(100,100,100,100));
						g2d.fillRect(Client.tileLen*x + 3, Client.tileLen*y + 3, Client.tileLen - 6, Client.tileLen - 6);
						neutral = true;
					} else
					if (!self && ((Person) bufLoc.units.get(i)).owner == Client.clientPlayerId) {
						g2d.setColor(new Color(50,200,50,100));
						g2d.fillRect(Client.tileLen*x + 3, (Client.world.dimensions[1] - 1 - y)*Client.tileLen + 3, Client.tileLen - 6, Client.tileLen - 6);
						self = true;
					} else
					if (ally) {
						g2d.setColor(new Color(50,50,200,100));
						g2d.fillRect(Client.tileLen*x + 3, Client.tileLen*y + 3, Client.tileLen - 6, Client.tileLen - 6);
						ally = true;
					}
					//if a mix of more than one of the 4, divide the square into rectangles and smaller squares
				}
				//paint transparent square of the colour of the owning player, or none if no units are present. paint multiple ones if units owned by different players are present?
			}
		}
		
		//paint outline of selection square on the sector currently selected by player
	}
}