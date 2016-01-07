
public class Utils {
	
	public static float sigmoid(float in){
		return sigmoid(in, 1);
	}
	
	public static float sigmoid(float in, float bias){
		return (float) (1 / (1 + Math.pow(Math.E, -(bias * in))));
	}
	
	public static void main(String args[]){
		System.out.println(sigmoid(5));
		System.out.println(sigmoid(-5));
		System.out.println(sigmoid(3));
		System.out.println(sigmoid(0));
		System.out.println(sigmoid((float)0.5,1));
	}
	
}
