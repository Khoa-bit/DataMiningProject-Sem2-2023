package datamining.project.Classifier;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class NaiveBayes_Classifier {
	private NaiveBayes nb = null;
	
	public NaiveBayes_Classifier() {
		try {
			nb = new NaiveBayes();
			//nb.setOptions(options);
			//nb.buildClassifier(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void train(Instances data) {
		try {
			nb.buildClassifier(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void prediction(Instances unlabeled) {
		try {
			//use importData(String path) instead when that is complete
			//unlabeled must first be from importData
			
			Instances labeled = new Instances(unlabeled);
			//System.out.println(unlabeled.instance(1));
			//System.out.println(labeled.instance(0));
			System.out.println(labeled.classAttribute());
			
			for (int i = 0; i < unlabeled.numInstances(); i++) {
				   double clsLabel = nb.classifyInstance(unlabeled.instance(i));
				   //System.out.println(clsLabel);
				   //System.out.println(unlabeled.classAttribute().value((int) clsLabel));
				   labeled.instance(i).setClassValue(clsLabel);
				   System.out.println(labeled.instance(i).stringValue(10));
				   
				   
			}
			//System.out.println(labeled.toString());
			//System.out.println(labeled);
			//write to a new csv
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public NaiveBayes getNB() {
		return nb;
	}
}
