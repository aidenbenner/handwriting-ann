import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Trainer {

	public static NeuralNetwork ann = new NeuralNetwork(3, 60, 32 * 32 + 2, 26);

	private static String annDataPath = "ann.data";

	public static void writeANNToDisk(NeuralNetwork out) {
		try {
			FileOutputStream fileoutput = new FileOutputStream(annDataPath);
			ObjectOutputStream ow = new ObjectOutputStream(fileoutput);
			ow.writeObject(out);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error writing ann ");
		}

	}

	public static NeuralNetwork readANNFromDisk() {
		try {
			FileInputStream fin = new FileInputStream(annDataPath);
			ObjectInputStream oi = new ObjectInputStream(fin);
			NeuralNetwork network = (NeuralNetwork) oi.readObject();
			return network;
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	static int ASCII_OFFSET = 96;

	public static double[] convertToOutput(char in) {
		int inVal = ((int) in - ASCII_OFFSET);
		double[] out = new double[126];
		Arrays.fill(out, 0);
		out[inVal] = 1;
		return out;
	}

	public static int outputToChar(double[] out) {
		int max = 0;

		for (int i = 0; i < out.length; i++) {

			if (out[i] > out[max]) {
				// System.out.print(out[i] + "  " );
				max = i;
			}
		}
		// System.out.print((char) (max + ASCII_OFFSET));
		// System.out.println("found " + out[max]);
		return max + ASCII_OFFSET;
	}

	public static double getConfidence(double[] out) {
		int max = 0;

		for (int i = 0; i < out.length; i++) {

			if (out[i] > out[max]) {
				// System.out.print(out[i] + "  " );
				max = i;
			}
		}
		return out[max];
	}

	public static boolean confidenceMode = false;

	public static String getCharString(NeuralNetwork passNN,
			ArrayList<OcrCharacter> chars) throws Exception {

		String out = "";
		for (OcrCharacter c : chars) {
			if (confidenceMode) {
				if (getConfidence(c.data) < 0.9) {
					out += "*" + (char) outputToChar(passNN.getResult(c.data))
							+ "*";
				} else {
					out += (char) outputToChar(passNN.getResult(c.data));
				}
				if (c.returnAfter) {
					out += "\n";
				}
				if (c.spaceAfter) {
					out += " ";
				}
			} else {
				out += (char) outputToChar(passNN.getResult(c.data));
			}
			if (c.returnAfter) {
				out += "\n";
			}
			if (c.spaceAfter) {
				out += " ";
			}
		}
		System.out.println(out);
		return out;
	}

	public static void train() throws Exception {

		ann = readANNFromDisk();

		ann.LEARNING_CONSTANT = 0.6;
		ArrayList<OcrCharacter> chars = CharacterGrabber
				.segmentImage("/home/aiden/Pictures/OCR/training/a-zonce.png");

		String training = "abcdefghijklmnopqrstuvwxyzthequickbrownfoxjumpedoverthelazydogthequickbrownfoxjumpedoverthelazydogthequickbrownfoxjumpedoverthelazydogthequickbrownfoxjumpedoverthelazydogthequickbrownfoxjumpedoverthelazydog";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int correct = 0;
		int incorrect = 0;
		int lastCorrect = 0;
		for (int i = 0; i < 1000; i++) {
			Random r = new Random();
			correct = 0;
			incorrect = 0;
			int charCount = 0;
			ArrayList currCharSet = new ArrayList();
			currCharSet.addAll(chars);
			System.out.println(chars.size() + "  " + training.length());
			for (int k = 0; k < chars.size(); k++) {

				charCount = r.nextInt(currCharSet.size());
				OcrCharacter c = (OcrCharacter) currCharSet.get(charCount);

				double[] desiredOut = convertToOutput(training
						.charAt(charCount));
				double[] actualOut = ann.getResult(c.data);
				// System.out.println(actualOut[50]);
				ann.train(c.data, desiredOut);
				charCount++;

			}

			for (int k = 0; k < chars.size(); k++) {
				OcrCharacter c = chars.get(k);
				if ((int) outputToChar(ann.getResult(c.data)) == (int) training
						.charAt(k)) {
					correct++;
				} else {
					double[] result = ann.getResult(c.data);
					for (int l = 0; l < result.length; l++) {
						// System.out.println((char)(l + ASCII_OFFSET) + "   " +
						// result[l]);
					}

					incorrect++;
				}

			}
			if (lastCorrect > correct) {
				// ann.LEARNING_CONSTANT *= 0.5;
			} else {
				// ann.LEARNING_CONSTANT *= 1.01;
				System.out.println("LC" + ann.LEARNING_CONSTANT);
			}
			lastCorrect = correct;

			getCharString(ann, chars);
			System.out.println("epoch " + i);
			System.out.println(" correct : " + correct);
			System.out.println(" incorrect : " + incorrect);

		}
		writeANNToDisk(ann);

	}
}