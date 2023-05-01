package datamining.project;

import weka.classifiers.AbstractClassifier;
import weka.core.*;

public class TestClassifier extends AbstractClassifier {

    private double threshold;
    private Attribute attribute;

    @Override
    public void buildClassifier(Instances data) throws Exception {
        // Set the attribute to be used for classification
        attribute = data.attribute(0); // Assuming the attribute is at index 0

        // Calculate the threshold for classification
        threshold = findThreshold(data);
    }

    @Override
    public double classifyInstance(Instance instance) throws Exception {
        // Get the value of the attribute for the instance
        double value = instance.value(attribute);

        // Compare the attribute value with the threshold and classify accordingly
        if (value > threshold) {
            return 0; // Classify as class 0
        } else {
            return 1; // Classify as class 1
        }
    }

    private double findThreshold(Instances data) {
        // Calculate the threshold as the mean value of the attribute
        double sum = 0;
        int count = data.numInstances();
        for (int i = 0; i < count; i++) {
            Instance instance = data.instance(i);
            sum += instance.value(attribute);
        }
        return sum / count;
    }
}
