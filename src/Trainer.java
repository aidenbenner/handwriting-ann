import javax.swing.JFrame;


public class Trainer {
	public static void main(String args[]) throws Exception{
		NeuralNetwork n = new NeuralNetwork(3,5,2,1);
		
//		JFrame jf = new JFrame();
//		jf.setSize(500, 500);
//		n.setSize(500,500);
//		jf.add(n);
//		jf.setVisible(true);
//		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		double[] inputs = new double[]{0,0};
		double[] target = new double[]{1};
		
		double[] inputs2 = new double[]{1,0};
		double[] target2 = new double[]{1};
		
		double[] inputs3 = new double[]{1,1};
		double[] target3 = new double[]{0};

		for(int i = 0; i<900000; i++){


			n.train(inputs, target);
			n.train(inputs3, target3);
			
			if(i % 10000 == 0){
				System.out.println("Epoch " + i + "------------");
				System.out.println(n.getResult(inputs)[0]);
				System.out.println(n.getResult(inputs2)[0]);
				System.out.println(n.getResult(inputs3)[0]);
			}
			
			
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
