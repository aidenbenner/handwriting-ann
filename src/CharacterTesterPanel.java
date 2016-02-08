import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;


public class CharacterTesterPanel extends JPanel implements MouseListener{
	
	public CharacterTesterPanel(){
		this.addMouseListener(this);
		this.setSize(500, 500);
	}
	

	
	boolean[][] isPainted = new boolean[16][16];
	
    int gridSize = 30;
    int xSize = 16;
    int ySize = 16;
	
	public void paintComponent(Graphics g)
    { 
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        
        
        for(int i = 0; i<xSize; i++){
        	for(int k = 0; k<ySize;k++){
        		g.drawRect(gridSize * i, gridSize * k , gridSize, gridSize);
        		if(isPainted[k][i]){
        			g.fillRect(gridSize * i, gridSize * k, gridSize, gridSize);
        		}
        	}
        }
        
        
        
        
        
    }
	
	
	
	public void getCharData(){
		
	}



	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void switchAtTarget(MouseEvent e){
		int mouseX = e.getY();
		int mouseY = e.getX();
		
		//System.out.println(e.getX());
		isPainted[(int) Math.floor((mouseX / gridSize))][(int) Math.floor((mouseY / gridSize))] 
				= !isPainted[(int) Math.floor((mouseX / gridSize))][(int) Math.floor((mouseY / gridSize))];
		
		
	}
	
	
	
	public double[] getCurrentState(){
		double[] output = new double[256];
		for(int i = 0; i<isPainted.length; i++){
			for(int k = 0; k<isPainted[i].length; k++){
				output[(i * 16) + k] = isPainted[i][k] ? 1 : 0;
			}
		}
		return output;
	}
	
	
	
	public void getNewState(double[] input){
		for(int i = 0; i<isPainted.length; i++){
			for(int k = 0; k<isPainted[i].length; k++){
				isPainted[i][k] = input[(i * 16) + k] > 0.5 ? true : false;
			}
		}
	}
	
	

	@Override
	public void mousePressed(MouseEvent e) {
		switchAtTarget(e);
		this.repaint();
		// TODO Auto-generated method stub

		
		
		
		
		
	}



	
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
