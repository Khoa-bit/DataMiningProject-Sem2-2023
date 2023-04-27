package datamining.project.classification;

import weka.core.Instances;
import weka.classifiers.Classifier;

public abstract class AbstractClassification {
    private Classifier classifier;
    private Instances instances;

    public AbstractClassification(Classifier a, Instances b) {
        this.classifier = a;
        this.instances = b;
    }

    public abstract Instances importData(String path);
    public abstract Classifier train(Classifier c, Instances data);
    public abstract void predict(Classifier c, Instances unlabeledData);

    public Classifier getClassifier() { return this.classifier; }
    public Instances getInstances() { return this.instances; }
}
