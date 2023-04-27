package datamining.project.Classifier;

import datamining.project.Classification;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class J48_Classifier extends Classification{
	private J48 tree = null;
	
	public J48_Classifier() {
		String[] options = new String[4];
		options[0] = "-C";
		options[1] = "0.25";
		options[2] = "-M";
		options[3] = "2";
		//J48 tree = null;
		try {
			tree = new J48();
			tree.setOptions(options);
			//tree.buildClassifier(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("J48");
		}		
	}
	
	public void train(Instances data) {
		try {
			tree.buildClassifier(data);
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
				   double clsLabel = tree.classifyInstance(unlabeled.instance(i));
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
	
	public J48 getJ48() {
		return tree;
	}
}
