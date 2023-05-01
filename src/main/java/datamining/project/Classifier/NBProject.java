package datamining.project.Classifier;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToNominal;

public class NBProject extends AbstractClassifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private NaiveBayes nb = null;
	private Instances data = null;
	
	public NBProject() {
		//J48 tree = null;
		try {
			nb = new NaiveBayes();
			//tree.buildClassifier(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("NB");
		}
	}
	
	@Override
	public void buildClassifier(Instances data) throws Exception {
		// TODO Auto-generated method stub
		nb.buildClassifier(data);
	}
	
	public void buildClassifier() throws Exception {
		// TODO Auto-generated method stub
		nb.buildClassifier(data);
	}
	
	public Instances importData(String path) {
		Instances finalData = null;
		try {
			DataSource source = new DataSource(path);
			Instances data = source.getDataSet();
			
			//System.out.println(data.get(0).toString());
			
			data.setClassIndex(11);
			//System.out.println(data.classAttribute());
			
			String[] options = new String[2];
			options[0] = "-R";
			options[1] = "12";
			
			NumericToNominal nb = new NumericToNominal();
			nb.setOptions(options);
			nb.setInputFormat(data);
			Instances newData = Filter.useFilter(data, nb);
			
			//System.out.println(newData.classAttribute());
			
			//System.out.println(newData.get(0).toString());
			
			
			String[] optionsRemove = new String[2];
			optionsRemove[0] = "-R";
			optionsRemove[1] = "1";
			Remove remove = new Remove();
			remove.setOptions(optionsRemove);
			remove.setInputFormat(newData);
			newData = Filter.useFilter(newData, remove);
			
			String[] optionsRemove2 = new String[2];
			optionsRemove2[0] = "-R";
			optionsRemove2[1] = "9";
			Remove remove2 = new Remove();
			remove2.setOptions(optionsRemove2);
			remove2.setInputFormat(newData);
			newData = Filter.useFilter(newData, remove2);
			
			optionsRemove = new String[2];
			optionsRemove[0] = "-R";
			optionsRemove[1] = "8";
			remove = new Remove();
			remove.setOptions(optionsRemove);
			remove.setInputFormat(newData);
			newData = Filter.useFilter(newData, remove);
			
			optionsRemove = new String[2];
			optionsRemove[0] = "-R";
			optionsRemove[1] = "2";
			remove = new Remove();
			remove.setOptions(optionsRemove);
			remove.setInputFormat(newData);
			newData = Filter.useFilter(newData, remove);
			
			finalData = newData;
			
			//System.out.println(finalData.classAttribute());
			
			//System.out.println(finalData.get(0).toString());
			
			
			//System.out.println(finalData.toString());
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data = finalData;
		return finalData;
	}
	
	public void prediction(Instances unlabeled) {
		try {
			//use importData(String path) instead when that is complete
			//unlabeled must first be from importData
			
			String[] options = new String[2];
			options[0] = "-R";
			options[1] = "10";
			StringToNominal sn = new StringToNominal();
			sn.setOptions(options);
			sn.setInputFormat(unlabeled);
			Instances newUnlabeled = Filter.useFilter(unlabeled, sn);
			
			
			Instances labeled = new Instances(newUnlabeled);
			//System.out.println(unlabeled.instance(1));
			//System.out.println(labeled.instance(0));
			//System.out.println(labeled.classAttribute());
			//System.out.println(newUnlabeled.classAttribute());
			
			for (int i = 0; i < unlabeled.numInstances(); i++) {
				   double clsLabel = classifyInstance(newUnlabeled.instance(i));
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
	
	public double classifyInstance(Instance instance) throws Exception {

	    double[] dist = nb.distributionForInstance(instance);
	    if (dist == null) {
	      throw new Exception("Null distribution predicted");
	    }
	    switch (instance.classAttribute().type()) {
	    case Attribute.NOMINAL:
	      double max = 0;
	      int maxIndex = 0;

	      for (int i = 0; i < dist.length; i++) {
	        if (dist[i] > max) {
	          maxIndex = i;
	          max = dist[i];
	        }
	      }
	      if (max > 0) {
	        return maxIndex;
	      } else {
	        return Utils.missingValue();
	      }
	    case Attribute.NUMERIC:
	    case Attribute.DATE:
	      return dist[0];
	    default:
	      return Utils.missingValue();
	    }
	  }
	
	public NaiveBayes getNB() {
		return nb;
	}

}
