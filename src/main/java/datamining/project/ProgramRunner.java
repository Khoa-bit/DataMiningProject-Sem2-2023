package datamining.project;

import datamining.project.preprocessing.DataPreprocessor;
import weka.core.WekaPackageManager;

public class ProgramRunner {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.printf("\n[ERROR] Inadequate or invalid input argument(s)!\n");
            System.out.printf("[MAIN] Exiting the program with code 1...\n\n");
            System.exit(1);
        }
        // Crucial line
        WekaPackageManager.loadPackages(false);
        var inputFile = args[0];
        var appDevMode = false;

        if (args.length > 1 && args[1].equals("-d")) {
            appDevMode = true;
        }

        if (appDevMode) System.out.printf("\n[MAIN] Running the program on DEVELOPMENT mode...\n");
        else System.out.printf("\n[MAIN] Running the program on NORMAL mode...\n");

        DataPreprocessor dataPrep = new DataPreprocessor(inputFile, appDevMode);
            dataPrep.loadCSVFile();
            dataPrep.preprocessData();
            dataPrep.generateFiles();
          
        System.out.printf("[MAIN] Exiting the program with code 0...\n\n");
    }
}
