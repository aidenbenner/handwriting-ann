import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class ImagePanel extends JPanel {	
	public ImagePanel(){
		bufImg = null;
	}
	
	public ImagePanel(String path){
		grabImage(path);
	}
	
	private BufferedImage bufImg;
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if(bufImg != null){
			g.drawImage(bufImg, 0, 0, null);
		}
	}
	
	public void grabImage(String path){
		try{
			bufImg = ImageIO.read(new File(path));
		} catch(IOException e){
			
		}
	}
	
}
