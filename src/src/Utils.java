public class Utils {
	public static double sigmoid(double in) {
		return (1 / (1 + Math.exp(-(in))));
	}

	public static double[] toDoubleArray(double in) {
		return new double[] { in };
	}

}