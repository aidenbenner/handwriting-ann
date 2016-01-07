import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


public class NeuralNetwork extends JPanel {

	Neuron[][] neurons;
	
	//learning constant
	float LEARNING_CONSTANT  = (float) 0.1;
	
	public NeuralNetwork(int layers, int neuronsPerLayer, int inputs, int outputs){
		//init number of neurons according to the number of layers and neurons per layer
		//Neuron array is held by neuron[row][column]
		neurons = new Neuron[layers][neuronsPerLayer];
		//we need to init all the neurons in these layers 
		for(int i = 0; i<layers; i++){
			if(i == 0){
				for(int k = 0; k<neuronsPerLayer; k++){
					neurons[i][k] = new Neuron(inputs);
				}
			}
			else if(i == layers-1){
				neurons[i] = new Neuron[outputs];
				for(int k = 0; k<outputs; k++){
					neurons[i][k] = new Neuron(neuronsPerLayer);
				}
			}
			else{
				for(int k = 0; k<neuronsPerLayer; k++){
					neurons[i][k] = new Neuron(neuronsPerLayer);
				}
			}
			
		}	
	//	System.out.println("done init");
	}
	
	protected void paintComponent(Graphics g){
		
		g.setColor(Color.RED);
		g.drawLine(0, 0, 20, 20);
		
		int xFlat = 100;
		int yFlat = 100;
		int xOffset = 120;
		int yOffset = 120;
		int width = 80;
		int height = 80;
		for(int i = 0; i<neurons.length; i++){
			for(int k = 0; k<neurons[i].length; k++){
				g.drawRect(xFlat + xOffset*i, yFlat + yOffset*k, width, height);
				for(int j = 0; j<neurons[i][k].weights.length; j++){
					g.drawString(neurons[i][k].weights[j] + " ", xFlat + xOffset*i, yOffset*(k+j));
				}
			}
		}
	}
	
	public float[] getLayerOutput(float[] input, int layer) throws Exception{
		float[] output = new float[neurons[layer].length];
		//System.out.println(neurons[layer].length);
		//System.out.println(output.length);
	
		//System.out.println("LAYER CALLED");
		for(int i = 0; i<output.length; i++){
			output[i] = neurons[layer][i].output(input);
			//System.out.print(output[i] + ", ");
		}
		//System.out.println();
		return output;
	}
	
	public float[] getResult(float[] input) throws Exception{
		//first run the inputs through the first layer 
		int layers = neurons.length;
		int neuronsPerLayer = neurons[0].length;
		float[] currInput = input;
		for(int i = 0; i<layers; i++){
			//for each layer 
			currInput = getLayerOutput(currInput,i);			
		}		
		return currInput;
	}
	
	public void train(float[] input, float[] target) throws Exception{
		//first run the inputs through the first layer 
		float[] actual = getResult(input);
		float[] error = new float[actual.length];
		//System.out.println(actual[0]);
		
		//calculate error for each output node 
		for(int i = 0; i<actual.length; i++){
			error[i] = actual[i] * (1-actual[i]) * (target[i] - actual[i]);
			//System.out.println(error[i]);
		}
		
		
		
		//change middle layer weights 
		for(int i = neurons.length-1; i>=0; i--){
			System.out.println("LAYER " + i);
		//	System.out.println("layer " + i);
			 //calculate error for the next layer
			float[] nextError = new float[neurons[i].length];
			for(int j = 0; j<neurons[i].length; j++){
				nextError[j] = neurons[i][j].getDelta(error);
				System.out.println(nextError[j]);
			}
			
			for(int k = 0; k<error.length; k++){
				for(int j = 0; j<neurons[i].length; j++){
					neurons[i][j].adjustWeights(LEARNING_CONSTANT);
				}
			}

			error = nextError;
		}
	
	}
}