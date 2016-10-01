import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;


public class Neuron implements Serializable {
	public double[] weights;
	public double[] deltaWeights;
	public static double momentum = 0.4;
	public double bias;
	public double lastOutput;
	public double lastInputs[]; 
	public double currDelta;
	public boolean inputLayer;
	
	Neuron(int inputs){
		this(inputs, false);
	}
	

	Neuron(int inputs, boolean isIn){
		inputLayer = false;
		weights = new double[inputs];
		Random r = new Random();
		deltaWeights = new double[inputs];
		Arrays.fill(deltaWeights, 0);
		for(int i = 0; i<inputs; i++){
			weights[i] = (double) ( -1 + Math.random() * 2);
			if(inputLayer){
				weights[i] = 1;
			}
		}
		if(!inputLayer){
			this.bias = (double) (-1 + Math.random() * 2);
		}
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
	double biasChange = 0;
	public void adjustWeights( double h){
		//brings output from forward, delta from last 
		if(!inputLayer){
			for(int i = 0; i<weights.length; i++){
				double deltaWeight = currDelta * h * lastInputs[i] + deltaWeights[i] * momentum;
				weights[i] = weights[i] + deltaWeight;
				deltaWeights[i] = deltaWeight;
			}
			biasChange = currDelta * h + biasChange * momentum;
			this.bias = this.bias + biasChange;
			this.bias = this.bias + currDelta * h;
		}
		//System.out.println(delta + " " + weight + " ");
	}
	
	public double getDelta(double[] error, Neuron[] fwdLayer, int index){
		double sum = 0;
		for(int i = 0; i<error.length; i++){
			sum += fwdLayer[i].currDelta * fwdLayer[i].weights[index];
		}
		this.currDelta = (lastOutput * (1 - lastOutput) * (sum));
		return this.currDelta;
	}

}
