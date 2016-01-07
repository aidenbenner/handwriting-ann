import javax.swing.JFrame;


public class Trainer {
	public static void main(String args[]) throws Exception{
		NeuralNetwork n = new NeuralNetwork(3,2,2,1);
		
//		JFrame jf = new JFrame();
//		jf.setSize(500, 500);
//		n.setSize(500,500);
//		jf.add(n);
//		jf.setVisible(true);
//		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		float[] inputs = new float[]{0,0};
		float[] target = new float[]{1};
		
		float[] inputs2 = new float[]{1,0};
		float[] target2 = new float[]{1};
		
		float[] inputs3 = new float[]{1,1};
		float[] target3 = new float[]{0};

		for(int i = 0; i<10000; i++){


			n.train(inputs, target);
			n.train(inputs3, target3);
		}
		
		//TESTING WITH XOR
		//1 1 0 0 
		//1 0 1 1
		//0 1 1 1
		//0 0 0 -
		System.out.println(n.getResult(inputs)[0]);
		System.out.println(n.getResult(inputs2)[0]);
		System.out.println(n.getResult(inputs3)[0]);
	}
}
