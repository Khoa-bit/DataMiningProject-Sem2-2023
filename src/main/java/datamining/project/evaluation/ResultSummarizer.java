package datamining.project.evaluation;

import weka.core.Instances;
import weka.classifiers.AbstractClassifier;

public class ResultSummarizer {
    private Evaluator evaluator1, evaluator2;
    private Instances dataInstance;

    public ResultSummarizer(AbstractClassifier classifier1, AbstractClassifier classifier2, Instances data) {
        this.dataInstance = data;
        this.evaluator1 = new Evaluator(classifier1, this.dataInstance);
        this.evaluator2 = new Evaluator(classifier2, this.dataInstance);
    }

    public void runEvals() {
        evaluator1.cross10FoldEvaluation();
        evaluator2.cross10FoldEvaluation();
    }

    public void reportResults() {
        System.out.println("Comparison Results:");
        System.out.println("-------------------");
        System.out.println("Classifier 1: " + evaluator1.getClassifierName());
        System.out.println("Accuracy: " + evaluator1.getEvaluationCore().pctCorrect());
        System.out.println("Runtime: " + evaluator1.getRuntimeDuration() + "ms");

        System.out.println("Classifier 2: " + evaluator2.getClassifierName());
        System.out.println("Accuracy: " + evaluator2.getEvaluationCore().pctCorrect());
        System.out.println("Runtime: " + evaluator2.getRuntimeDuration() + "ms");
    }
}
