package datamining.project.evaluation;

import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.AbstractClassifier;

import java.util.Random;

public class EvaluationModule {
    private Evaluation evalCore;
    private Instances dataInstance;
    private AbstractClassifier targetClassifier;

    public EvaluationModule(AbstractClassifier a, Instances b) {
        this.targetClassifier = a;
        this.dataInstance = b;

        try {
            this.evalCore = new Evaluation(this.dataInstance);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cross10FoldEval() {
        try {
            this.evalCore.crossValidateModel(
                this.targetClassifier, 
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
