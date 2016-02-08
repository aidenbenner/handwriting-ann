import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class SineTest {
	
	static NeuralNetwork ann = new NeuralNetwork(6,7,1,1);
	static final long EPOCHS = 50000000;
	public static void main(String args[]) throws Exception{
		JFrame jf = new JFrame();
		jf.setSize(500,500);
	
		JPanel chart = getChart();
		jf.add(chart);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for(int i = 0; i<EPOCHS; i++){
			for(double k = 0; k<180; k++){
				double rad = (k / 180 ) * Math.PI;
				ann.train(rad, Math.sin(rad));
			
			
			
		
			}
			
//			System.out.println(ann.neurons[ann.neurons.length-1][0].weights[0]);
//			System.out.println(ann.neurons[ann.neurons.length-1][0].weights[1]);
//			System.out.println(ann.neurons[ann.neurons.length-1][0].weights[2]);
//			System.out.println("error " + (ann.getResult(rad)[0] - Math.sin(rad)));
//			
			if(i % 10000 == 0){
				
				refreshData();
				chart.repaint();
				System.out.println("EPOCH " + i);
				Thread.sleep(200);
			}
		}
		
	
		
		
	}
	
	static XYPlot graph = new XYPlot();
	static XYSeriesCollection dataSet = new XYSeriesCollection();
	static XYSeries neuralSet = new XYSeries("ann");
	static XYSeries data = new XYSeries("sin(x)");
	static XYSeries weights = new XYSeries("weights");
	public static XYSeriesCollection refreshData() throws Exception{
		data.clear();
		weights.clear();
		neuralSet.clear();
	
		for(double i = 0; i<180; i++){
			double rads = (i / 180 )* Math.PI;
			data.add(rads, Math.sin(rads));
			neuralSet.add(rads, ann.getResult(rads)[0]);
			//System.out.println(rads);
		}

		return dataSet;
	}
	
	
	public static ChartPanel getChart() throws Exception{
		dataSet.addSeries(data);
		dataSet.addSeries(neuralSet);
		refreshData();
		JFreeChart xyGraph = ChartFactory.createXYLineChart(
			"sin(x) as a function of x", "x (rads)", "sin(x)",
			dataSet);
		return new ChartPanel(xyGraph);
	}
}