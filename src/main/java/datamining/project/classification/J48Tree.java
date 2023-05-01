package datamining.project.classification;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.attribute.Remove;

public class J48Tree extends AbstractClassifier {
	private static final long serialVersionUID = 1L;
	private J48 tree = null;
	private Instances dataInstance = null;
	
	public J48Tree() {
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
			e.printStackTrace();
		}
	}
	
	@Override
	public void buildClassifier(Instances a) throws Exception {
		tree.buildClassifier(a);
	}
	
	public void buildClassifier() throws Exception {
		tree.buildClassifier(this.dataInstance);
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
			finalData = Filter.useFilter(newData, remove2);
			
			//System.out.println(finalData.classAttribute());
			
			//System.out.println(finalData.get(0).toString());
			
			
			//System.out.println(finalData.toString());
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dataInstance = finalData;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public double classifyInstance(Instance instance) throws Exception {

	    double[] dist = tree.distributionForInstance(instance);
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
	
	public J48 getJ48() {
		return tree;
	}

}
