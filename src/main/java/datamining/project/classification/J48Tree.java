package datamining.project.classification;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class J48Tree extends AbstractClassifier {
	private J48 tree;
	
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
