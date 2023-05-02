package datamining.project.evaluation;

import weka.core.Instances;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import weka.classifiers.AbstractClassifier;

public class ResultSummarizer {
    private ExecutorService exeService;
    private Evaluator evaluator1, evaluator2;
    private Instances dataInstance;

    public ResultSummarizer(AbstractClassifier classifier1, AbstractClassifier classifier2, Instances data) {
        this.dataInstance = data;
        this.evaluator1 = new Evaluator(classifier1, this.dataInstance);
        this.evaluator2 = new Evaluator(classifier2, this.dataInstance);
        this.exeService = Executors.newFixedThreadPool(1);
    }

    public void runEvals() {
        this.exeService.submit(evaluator1);
        this.exeService.submit(evaluator2);

        System.out.println("[MAIN] All evaluation tasks are submitted!");
    }

    public void reportResults() {
        exeService.shutdown();
        try {
            if (!exeService.awaitTermination(60, TimeUnit.SECONDS)) {
                exeService.shutdownNow();
            } 
        } catch (InterruptedException ie) {
            exeService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("[MAIN] All evaluation tasks are completed!");
        System.out.println();
        System.out.println("Comparison Results:");
        System.out.println("------------------------------------------");
        System.out.println(getClassifierString(evaluator1.getClassifierName()));
        System.out.println(getAccuracyString(evaluator1.getAccuracy()));
        System.out.println(getRuntimeString(evaluator1.getRuntimeDuration()));
        System.out.println("**********");
        System.out.println(getClassifierString(evaluator2.getClassifierName()));
        System.out.println(getAccuracyString(evaluator2.getAccuracy()));
        System.out.println(getRuntimeString(evaluator2.getRuntimeDuration()));
        System.out.println("------------------------------------------");
    }

    private String getClassifierString(String a) {
        return String.format("Classifier: %s", a);
    }

    private String getRuntimeString(long a) {
        return String.format("Runtime: %sms", a);
    }

    private String getAccuracyString(double a) {
        var str = String.format("Accuracy: %.5f", a);
        str += "%\t\t\t";
        
        return str;
    }
}
