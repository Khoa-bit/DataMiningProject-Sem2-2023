package datamining.project.classification;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

public class NaiveBayesAlgorithm extends AbstractClassifier {

    private NaiveBayes naiveBayes;

    public NaiveBayesAlgorithm() {
        try {
            this.naiveBayes = new NaiveBayes();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void buildClassifier(Instances givenData) throws Exception {
        this.naiveBayes.buildClassifier(givenData);
    }
    
    public double classifyInstance(Instance instance) throws Exception {
        var distribution = this.naiveBayes.distributionForInstance(instance);
	    
        if (distribution == null) {
	        throw new Exception("Null distribution predicted");
	    }

	    switch (instance.classAttribute().type()) {
	        case Attribute.NOMINAL:
	            var max = 0.0;
	            var maxIndex = 0;

                for (var i = 0; i < distribution.length; i++) {
                    if (distribution[i] > max) {
                        maxIndex = i;
                        max = distribution[i];
                    }
                }

                if (max > 0) {
                    return maxIndex;
                } 
                else {
                    return Utils.missingValue();
                }

            case Attribute.NUMERIC:
            case Attribute.DATE:
                return distribution[0];
            
            default:
                return Utils.missingValue();
        }
    }
}
