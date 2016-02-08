
public class Utils {
	

	
	public static double sigmoid(double in){
		return  (1 / (1 + Math.exp(-(in))));
	}
	
	public static void main(String args[]){
		System.out.println(sigmoid(5));
		System.out.println(sigmoid(-5));
		System.out.println(sigmoid(3));
		System.out.println(sigmoid(0));
		System.out.println(sigmoid(0.5));
	}
	
	public static double[] toDoubleArray(double in){
		return new double[]{in};
	}
	
}