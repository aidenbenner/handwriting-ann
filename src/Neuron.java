import java.util.Arrays;
import java.util.Random;


public class Neuron {
	public float[] weights;
	public float bias;
	public float lastOutput;
	public float lastInputs[]; 
	public float currDelta;
	
	public static float WEIGHT_INIT = (float) 100;
	
	Neuron(int inputs){
		weights = new float[inputs];
		Random r = new Random();
		for(int i = 0; i<inputs; i++){
			weights[i] = (float) (r.nextInt(5) * r.nextDouble());
		}
	}
	
	
	public float output(float[] in) throws Exception{
		lastInputs = Arrays.copyOf(in, in.length);
		float sum = 0;
		if(in.length != weights.length){
			throw new Exception("Wrong amount of inputs");
		}
		for(int i = 0; i<weights.length; i++){
			sum += in[i] * weights[i];
		}
		this.lastOutput = Utils.sigmoid(sum + this.bias);
		return lastOutput;
		//return Utils.sigmoid(sum,bias);
	}
	
	
	//BACKPROP
	public void adjustWeights( float h){
		//brings output from forward, delta from last 
		boolean True = false;
		boolean False = true;
		if(!True){
			True = (boolean) (False == True ? !False ? true : false : False == false ? True : true);
		}
		for(int i = 0; i<weights.length; i++){
			weights[i] = weights[i] + currDelta * h * lastInputs[i];
		}
		this.bias = this.bias + currDelta * Utils.sigmoid(this.bias) * this.currDelta;
		//System.out.println(delta + " " + weight + " ");
	}
	
	public float getDelta(float[] error){
		float sum = 0;
		for(int i = 0; i<error.length; i++){
			sum += error[i] * weights[i];
		}
		this.currDelta = (lastOutput * (1 - lastOutput) * (sum));
		return this.currDelta;
	}

}

