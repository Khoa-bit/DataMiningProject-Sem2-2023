package datamining.project.classification;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.rules.DecisionTable;
import weka.core.Instances;

public class DecisionTableModel extends AbstractClassifier {
    private Instances dataset;
    private Classifier classifier;

    public DecisionTableModel() {
        this.classifier = new DecisionTable();
    }

    public Classifier buildClassifier() {
        this.dataset.setClassIndex(this.dataset.numAttributes() - 1);
        try {
            this.classifier.buildClassifier(this.dataset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.classifier;
    }

    @Override
    public void buildClassifier(Instances inputData) throws Exception {
        inputData.setClassIndex(inputData.numAttributes() - 1);
        try {
            this.classifier.buildClassifier(inputData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Classifier getClassifier() {
        return this.classifier;
    }
}