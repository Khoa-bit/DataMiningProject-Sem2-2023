package datamining.project.betterclassification;

import weka.classifiers.Classifier;
import weka.classifiers.rules.DecisionTable;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class DecisionTableModel {
    private Instances dataset;
    private Classifier classifier;
    private DataSource dataSource;

    public DecisionTableModel(String path) {
        try {
            this.dataSource = new DataSource(path);
            this.dataset = dataSource.getDataSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.classifier = new DecisionTable();
    }

    public Classifier BuildModel() {
        this.dataset.setClassIndex(this.dataset.numAttributes() - 1);
        try {
            this.classifier.buildClassifier(this.dataset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.classifier;
    }

    public Classifier getClassifier() {
        return this.classifier;
    }
}