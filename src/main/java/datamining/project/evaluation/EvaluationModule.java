package datamining.project.evaluation;

import weka.core.Instances;
import weka.classifiers.Evaluation;

import java.util.Random;

import datamining.project.classification.AbstractClassification;

public class EvaluationModule {
    private Evaluation evalCore;
    private Instances dataInstance;
    private AbstractClassification targetClassifer;

    public EvaluationModule(AbstractClassification a, Instances b) {
        this.targetClassifer = a;
        this.dataInstance = b;

        try {
            this.evalCore = new Evaluation(this.dataInstance);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cross10FoldEval() {
        var classifier = this.targetClassifer.getClassifier();
        try {
            this.evalCore.crossValidateModel(
                classifier, 
                this.dataInstance, 
                10, 
                new Random(1)
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Evaluation getEvaluationCore() { return this.evalCore; }
}
