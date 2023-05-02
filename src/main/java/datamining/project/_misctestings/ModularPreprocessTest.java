package datamining.project._misctestings;

import datamining.project.preprocessing.DataPreprocessor;

public class ModularPreprocessTest {
    public static void main(String[] args) {
        DataPreprocessor dp = new DataPreprocessor("healthcare-dataset-stroke-data.csv", true);
            dp.loadCSVFile();
            dp.preprocessData();
            // dp.reportData();
            dp.generateFiles();
    }
}
