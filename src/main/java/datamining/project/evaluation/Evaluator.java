package datamining.project.evaluation;

import weka.core.Instances;
import weka.classifiers.Evaluation;
import weka.classifiers.AbstractClassifier;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Evaluator implements Runnable {
    private long duration;
    private Evaluation evalCore;
    private Instances dataInstance;
    private AbstractClassifier targetClassifier;

    public Evaluator(AbstractClassifier a, Instances b) {
        this.targetClassifier = a;
        this.dataInstance = b;

        try {
            this.evalCore = new Evaluation(this.dataInstance);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        cross10FoldEvaluation();
    }

    public void cross10FoldEvaluation() {
        var startMark = System.nanoTime();
        try {
            this.evalCore.crossValidateModel(
                    this.targetClassifier,
                    this.dataInstance,
                    10,
                    new Random(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        var endMark = System.nanoTime();

        this.duration = TimeUnit.MILLISECONDS.convert(endMark - startMark, TimeUnit.NANOSECONDS);
    }

    public String getClassifierName() {
        return targetClassifier.getClass().getSimpleName();
    }

    public double getAccuracy() {
        return this.evalCore.pctCorrect();
    }

    public Evaluation getEvaluationCore() {
        return this.evalCore;
    }
    public long getRuntimeDuration() { return this.duration; }
}
