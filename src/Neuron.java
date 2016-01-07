import java.util.Random;


public class Neuron {
	public float[] weights;
	public float bias;
	public float lastOutput;
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
	public void adjustWeights(float delta, int weight, float h){
		//brings output from forward, delta from last 
		System.out.println("changing weights init " + weights[weight]);
		this.bias = this.bias + delta * Utils.sigmoid(this.bias) * this.currDelta;
		weights[weight] = weights[weight] + delta * h * lastOutput;
		System.out.println(" " + weights[weight]);
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






