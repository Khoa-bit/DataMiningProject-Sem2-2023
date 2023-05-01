package datamining.project;

import datamining.project.evaluation.ComparisonEvaluator;
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
            ComparisonEvaluator compare = new ComparisonEvaluator(new J48(), new OneR(), data);
            compare.GetComparison();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
