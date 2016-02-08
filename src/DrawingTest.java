import javax.swing.JFrame;


public class DrawingTest {
	public static void main(String args[]){
		JFrame jf = new JFrame();
		jf.setSize(500, 500);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(new CharacterTesterPanel());
		
	}
}
