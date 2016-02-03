import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Trainer {

	static NeuralNetwork n = new NeuralNetwork(4, 32, 32 * 32, 10);

	int charHeight = 32;
	int charWidth = 32;

	public static double[] grabNextCharacter(BufferedReader br)
			throws IOException {
		double[] character = new double[32 * 32];
		// read the first 32 lines
		for (int i = 0; i < 32; i++) {

			char[] nums = br.readLine().toCharArray();
			for (int k = 0; k < nums.length; k++) {
				character[i * 32 + k] = Double.parseDouble(nums[k] + "");
			}
		}
		return character;
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
		for(int i = 0; i<out.length; i++){
			System.out.print(out[i] + ", ");
			if(out[i] > out[max]){
				max = i;
			}
		}
		return max;
	}

	public static void main(String args[]) throws Exception {
		BufferedReader br = new BufferedReader(
				new FileReader(
						"/home/aiden/Documents/Github/CS_IA/Datasets/optdigits-orig.cv"));

		for (int i = 0; i < 750; i++) {

			double[] letter = grabNextCharacter(br);
			int digit = Integer.parseInt(br.readLine().charAt(1) + "");
			double[] target = convertToOutput(digit);
			
			

//			double angle = Math.random() * 2 * Math.PI;
//			n.train(new double[] { angle }, new double[] { Math.sin(angle) });
			n.train(letter, target);
			System.out.println("target digit : " + digit + " act : " + outputToDigit(n.getResult(letter)));
			if (i % 100 == 0) {
				System.out.println("Epoch " + i + "------------");

			}
		}

		System.out.println("finished");

	}

	public static void sinTest() throws Exception {

		for (int i = 0; i < 180; i++) {
			double increment = i * Math.PI / 180;
			System.out.println(n.getResult(Math.PI - increment)[0] + "\t"
					+ Math.sin(Math.PI - increment));
		}
	}

}
