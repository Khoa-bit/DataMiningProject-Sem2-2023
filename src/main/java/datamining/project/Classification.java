package datamining.project;

import weka.core.converters.ConverterUtils.DataSource;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;

import weka.core.Instances;
import weka.core.WekaPackageManager;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.*;



public class Classification {
	//Instances data;
	//Classifier classifier;
	private String path = "";
	
	public Classification() {
		
		WekaPackageManager.loadPackages(false);
		//Instances data = importData();
		//classifier = buildJ48();
	}
	
	public Instances importData(String path) {
		Instances finalData = null;
		try {
			DataSource source = new DataSource(path);
			Instances data = source.getDataSet();
			
			//System.out.println(data.get(0).toString());
			
			data.setClassIndex(11);
			System.out.println(data.classAttribute());
			
			String[] options = new String[2];
			options[0] = "-R";
			options[1] = "12";
			
			NumericToNominal nb = new NumericToNominal();
			nb.setOptions(options);
			nb.setInputFormat(data);
			Instances newData = Filter.useFilter(data, nb);
			
			System.out.println(newData.classAttribute());
			
			//System.out.println(newData.get(0).toString());
			
			
			String[] optionsRemove = new String[2];
			optionsRemove[0] = "-R";
			optionsRemove[1] = "1";
			Remove remove = new Remove();
			remove.setOptions(optionsRemove);
			remove.setInputFormat(newData);
			finalData = Filter.useFilter(newData, remove);
			
			System.out.println(finalData.classAttribute());
			
			//System.out.println(finalData.get(0).toString());
			
			
			//System.out.println(finalData.toString());
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalData;
	}
	
	public J48 buildJ48() {
		String[] options = new String[4];
		options[0] = "-C";
		options[1] = "0.25";
		options[2] = "-M";
		options[3] = "2";
		J48 tree = null;
		try {
			tree = new J48();
			tree.setOptions(options);
			//tree.buildClassifier(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("J48");
		}
		return tree;
				
	}
	
	public NaiveBayes buildNB() {
		NaiveBayes nb = null;
		try {
			nb = new NaiveBayes();
			//nb.setOptions(options);
			//nb.buildClassifier(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nb;		
	}
	
	public void tenFold(Classifier c, Instances data) {
		try {
			Evaluation eval = new Evaluation(data);
			eval.crossValidateModel(c, data, 10, new Random(1));
			System.out.println(eval.toSummaryString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void prediction(Classifier c, Instances unlabeled) {
		try {
			//use importData(String path) instead when that is complete
			//unlabeled must first be from importData
			
			Instances labeled = new Instances(unlabeled);
			
			for (int i = 0; i < unlabeled.numInstances(); i++) {
				   double clsLabel = c.classifyInstance(unlabeled.instance(i));
				   labeled.instance(i).setClassValue(clsLabel);
			}
			
			//write to a new csv
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
