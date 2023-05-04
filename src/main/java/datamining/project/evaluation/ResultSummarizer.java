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
    private boolean shouldReportDetailed;

    public ResultSummarizer(AbstractClassifier classifier1, AbstractClassifier classifier2, Instances data, boolean detailedReport) {
        this.dataInstance = data;
        this.shouldReportDetailed = detailedReport;
        this.evaluator1 = new Evaluator(classifier1, this.dataInstance);
        this.evaluator2 = new Evaluator(classifier2, this.dataInstance);
        this.exeService = Executors.newFixedThreadPool(2);
    }

    public void runEvals() {
        this.exeService.submit(evaluator1);
        this.exeService.submit(evaluator2);

        System.out.println("[MAIN] All evaluation tasks are submitted!");
    }

    public void reportResults() {
        exeService.shutdown();
        try {
            if (!exeService.awaitTermination(5, TimeUnit.MINUTES)) {
                exeService.shutdownNow();
            } 
        } catch (InterruptedException ie) {
            exeService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        var state = this.shouldReportDetailed ? "Comprehensive" : "Brief";

        System.out.println("[MAIN] All evaluation tasks are completed!");
        System.out.println();
        System.out.println(state + " Comparison Results:");
        System.out.println("------------------------------------------");
        System.out.println(getClassifierString(evaluator1.getClassifierName()));
        detailReportEvaluator1();
        System.out.println();
        System.out.println("*********************");
        System.out.println();
        System.out.println(getClassifierString(evaluator2.getClassifierName()));
        detailReportEvaluator2();
        System.out.println("------------------------------------------");
    }

    private void detailReportEvaluator1() {
        if (!this.shouldReportDetailed) {
            System.out.println(getAccuracyString(evaluator1.getAccuracy()));
        }

        else {
            System.out.println(evaluator1.getEvaluationCore().toSummaryString());
        }
        System.out.println(getRuntimeString(evaluator1.getRuntimeDuration()));
    }
    
    private void detailReportEvaluator2() {
        if (!this.shouldReportDetailed) {
            System.out.println(getAccuracyString(evaluator2.getAccuracy()));
        }

        else {
            System.out.println(evaluator2.getEvaluationCore().toSummaryString());
        }
        System.out.println(getRuntimeString(evaluator2.getRuntimeDuration()));
    }

    private String getClassifierString(String a) {
        return String.format("Classifier: %s", a);
    }

    private String getAccuracyString(double a) {
        var str = String.format("\tAccuracy: \t\t%.5f", a);
        str += "%\t\t\t";
        
        return str;
    }

    private String getRuntimeString(long a) {
        return String.format("\tTotal Runtime: \t\t%sms", a);
    }
}
