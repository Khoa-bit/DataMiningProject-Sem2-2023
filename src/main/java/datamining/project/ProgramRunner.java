package datamining.project;

import datamining.project.classification.DecisionTableModel;
import datamining.project.classification.NaiveBayesAlgorithm;
import datamining.project.evaluation.ResultSummarizer;
import datamining.project.preprocessing.DataPreprocessor;
import weka.core.WekaPackageManager;

public class ProgramRunner {
    public static void main(String[] args) {
        String inputFileName = null;
        String resultModeInput = null;
        String runModeInput = null;

        // Checking input parameters and assigning variables with values
        if (args.length < 1) {
            System.out.printf("\n[ERROR] Inadequate input argument(s)!\n");
            System.out.printf("[MAIN] Exiting the program with code 1...\n\n");
            System.exit(1);
        }
        if (args.length >= 2) {
            resultModeInput = args[1];
        }
        if (args.length >= 3) {
            runModeInput = args[2];
        }
        inputFileName = args[0];

        // Crucial line
        WekaPackageManager.loadPackages(false);
        var detailedReport = false;
        var appDevMode = false;

        if (resultModeInput != null && resultModeInput.equals("-detail")) {
            System.out.printf("[MAIN] Results will be reported in details!\n");
            detailedReport = true;
        }
        else System.out.printf("[MAIN] Results will be reported briefly!\n");

        if (runModeInput != null && runModeInput.equals("-dev")) {
            System.out.printf("\n[MAIN] Running the program on DEVELOPMENT mode...\n");
            appDevMode = true;
        }
        else System.out.printf("\n[MAIN] Running the program on NORMAL mode...\n");

        // Start Data Preprocessing and Evaluation modules
        var dataPrep = new DataPreprocessor(inputFileName, appDevMode);
            dataPrep.loadCSVFile();
            dataPrep.preprocessData();
            dataPrep.generateFiles();

        var reporter = new ResultSummarizer(
            new NaiveBayesAlgorithm(),
            new DecisionTableModel(),
            dataPrep.getARFFData(),
            detailedReport
        );

        reporter.runEvals();
        reporter.reportResults();
        System.out.printf("\n[MAIN] Exiting the program with code 0...\n");
    }
}
