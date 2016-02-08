import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;

public class Trainer {

	static NeuralNetwork ann = new NeuralNetwork(3, 32, 16 * 16, 10);

	static class Character {
		double[] data;
		double[] ans;
		int num;
	}

	public static Character grabNextCharacter(BufferedReader br)
			throws IOException {
		Character output = new Character();
		output.data = new double[16 * 16];
		// read the first 32 lines
		String[] line = br.readLine().split(" ");
		for (int k = 0; k < 256; k++) {
			output.data[k] = Double.parseDouble(line[k]);
		}

		output.num = 0;
		output.ans = new double[10];
		for (int j = 256; j < line.length; j++) {
			output.ans[j - 256] = Integer.parseInt(line[j]);
			if (Integer.parseInt(line[j]) == 1) {
				output.num = j - 256;
			}
		}

		return output;
	}

	public static double[] convertToOutput(int in) {
		double[] out = new double[10];
		for (int i = 0; i < in - 1; i++) {
			out[i] = 0;
		}
		out[in] = 1;
		for (int i = in; i < out.length; i++) {
			out[i] = 0;
		}
		return out;
	}

	public static int outputToDigit(double[] out) {
		int max = 0;
		System.out.println();
		for (int i = 0; i < out.length; i++) {

			if (out[i] > out[max]) {
				max = i;
			}
		}
		return max;
	}

	public static void main(String args[]) throws Exception {

		
		JFrame jf = new JFrame();
		jf.setSize(500, 500);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CharacterTesterPanel ctp = new CharacterTesterPanel();
		jf.add(ctp);
		for (int k = 0; k < 5000; k++) {
			BufferedReader br = new BufferedReader(new FileReader(
					"src/hw_data.txt"));
			for (int i = 0; i < 1300; i++) {
				
				
				Character current = grabNextCharacter(br);
			
				
				//System.out.println(outputToDigit(ann.getResult(current.data)));
				ctp.getNewState(current.data);
				ann.train(current.data, current.ans);
			}
			System.out.println("predicted " + outputToDigit(ann.getResult(ctp.getCurrentState())));
			System.out.println(k);
			br.close();
		}
		
		

		
		
		while(true){
			System.out.println("predicted " + outputToDigit(ann.getResult(ctp.getCurrentState())));
	

			Thread.sleep(500);
		}

	
	}

}