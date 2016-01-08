import java.util.Arrays;
import java.util.Random;


public class Neuron {
	public double[] weights;
	public double bias;
	public double lastOutput;
	public double lastInputs[]; 
	public double currDelta;
	
	public static double WEIGHT_INIT = (double) 100;
	
	Neuron(int inputs){
		weights = new double[inputs];
		Random r = new Random();
		for(int i = 0; i<inputs; i++){
			weights[i] = (double) (-1 * r.nextDouble() + 2);
		}
		this.bias = (double) (-1 * r.nextDouble() + 2);
	}
	
	
	public double output(double[] input) throws Exception{
		lastInputs = Arrays.copyOf(input, input.length);
		double sum = 0;
		if(input.length != weights.length){
			throw new Exception("Wrong amount of inputs");
		}
		for(int i = 0; i<weights.length; i++){
			sum += input[i] * weights[i];
		}
		this.lastOutput = Utils.sigmoid(sum + this.bias);
		return lastOutput;
		//return Utils.sigmoid(sum,bias);
	}
	
	
	//BACKPROP
	public void adjustWeights( double h){
		//brings output from forward, delta from last 
		for(int i = 0; i<weights.length; i++){
			weights[i] = weights[i] + currDelta * h * lastInputs[i];
		}
		this.bias = this.bias + currDelta * h;
		//System.out.println(delta + " " + weight + " ");
	}
	
	public double getDelta(double[] error, Neuron[] fwdLayer, int index){
		double sum = 0;
		for(int i = 0; i<error.length; i++){
			sum += error[i] * fwdLayer[i].weights[index];
		}
		this.currDelta = (lastOutput * (1 - lastOutput) * (sum));
		return this.currDelta;
	}

}

