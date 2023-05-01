package datamining.project.evaluation;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;

public class ComparisonEvaluator {
    private EvaluationModule evaluator1, evaluator2;
    private Instances dataInstance;

    private long startTime, runtime1, runtime2;

    public ComparisonEvaluator(AbstractClassifier classifier1, AbstractClassifier classifier2, Instances data) {
        dataInstance = data;
        this.evaluator1 = new EvaluationModule(classifier1, data);
        this.evaluator2 = new EvaluationModule(classifier2, data);
    }

    private void RunEvaluation() {
        startTime = System.currentTimeMillis();
        evaluator1.cross10FoldEval();
        runtime1 = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        evaluator2.cross10FoldEval();
        runtime2 = System.currentTimeMillis() - startTime;
    }

    private void PrintResults() {
        System.out.println("Comparison Results:");
        System.out.println("-------------------");
        System.out.println("Classifier 1: " + evaluator1.GetClassifierName());
        System.out.println("Accuracy: " + evaluator1.getEvaluationCore().pctCorrect());
        System.out.println("Runtime: " + runtime1 + "ms");

        System.out.println("Classifier 2: " + evaluator2.GetClassifierName());
        System.out.println("Accuracy: " + evaluator2.getEvaluationCore().pctCorrect());
        System.out.println("Runtime: " + runtime2 + "ms");
    }

    public void GetComparison() {
        RunEvaluation();
        PrintResults();
    }

    public static void main(String[] args) {
        try {
            DataSource source = new DataSource("src\\main\\resources\\diabetes.arff");
            Instances data = source.getDataSet();
            if (data.classIndex() == -1) {
                data.setClassIndex(data.numAttributes() - 1);
            }
            ComparisonEvaluator compare = new ComparisonEvaluator(new J48(), new OneR(), data);
            compare.GetComparison();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
