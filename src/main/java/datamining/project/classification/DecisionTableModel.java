package datamining.project.classification;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.rules.DecisionTable;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class DecisionTableModel extends AbstractClassifier {
    private Classifier classifier;

    public DecisionTableModel() {
        this.classifier = new DecisionTable();
    }

    @Override
    public void buildClassifier(Instances inputData) throws Exception {
        try {
            this.classifier.buildClassifier(inputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double classifyInstance(Instance instance) throws Exception {
		double[] dist = this.classifier.distributionForInstance(instance);
	    
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

    public Classifier getClassifier() {
        return this.classifier;
    }
}