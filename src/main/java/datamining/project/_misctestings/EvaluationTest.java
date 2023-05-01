package datamining.project._misctestings;

import datamining.project.evaluation.ResultSummarizer;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class EvaluationTest {
    public static void main(String[] args) {
        try {
            DataSource source = new DataSource("src\\main\\resources\\diabetes.arff");
            Instances data = source.getDataSet();
            if (data.classIndex() == -1) {
                data.setClassIndex(data.numAttributes() - 1);
            }
            ResultSummarizer compare = new ResultSummarizer(new J48(), new OneR(), data);
            compare.runEvals();
            compare.reportResults();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
