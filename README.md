# Handwriting_ANN
Convert images of handwriting to digital text using neural networks.
Created for grade 12 Computer Science IB IA.
Implemented using Java and Opencv.

## Program Overview
![High level program flow](http://i.imgur.com/XI4TMfd.png)

The general idea behind the ANN is to receive inputs (in this case pixel intensity values that we have parsed from our
image). Each circle above represents a neuron and each line represents a connection between two neurons. ANN’s are
composed of three types of layers, the input layer, the hidden layers and the output layer. The fundamental unit of the
ANN is the Neuron. The Neuron has a set of weights attached to it each corresponding Neuron in the previous layer
(unless it’s an input layer). Inputs are given to the input layer of the network, the inputs are then feed forward through
the neurons to reach the output layer. Each neuron uses the same activation function, in this case a sigmoid function

![Overview of a neural network](http://i.imgur.com/q7RKTTN.png)

The general idea of the backpropagation algorithm is to ‘backpropagate’ calculated error in the last layer throughout
the network adjusting the weights of each individual neurons by comparing their last output and calculated error
value. Below is the code that changes the weights of each neuron. The new weight is equal to the current error which
is calculated from the product of the error and weight of the nodes ahead of the current and is multiplied by the
learning constants h. The h value is a constant that needs to be tuned, if it is too big, the weights will continue to
overshoot and total error will oscillate around the desired. If the weight is too small it will take too long for the
network to train, and it may not even train at all. It is also sometimes beneficial to change the learning constant
overtime, first starting with a high value to speed up initial change where error is very high, and then slowly
decreasing the learning constant so the network can converge on the ideal weight configuration. After finished the
backpropagation algorithm I tried training it on a sine function in order to debug and to tune the network and learning
constant, and received very positive results (see appendix).

~~~java
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
~~~

## Training 

Successful training of a sin function to test network

![Training on a sin function](http://i.imgur.com/WSfWhGs.png)

Successful training using character test panel 

![Successful preliminary recognition of characters](http://i.imgur.com/IaHIzbY.png)

![Successful preliminary recognition 2](http://i.imgur.com/Tmd88ZO.png)

Character segmentation demonstration:

![Demo of character segmentation](http://i.imgur.com/JXKyPYC.png)







