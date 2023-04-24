package datamining.project;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.meta.FilteredClassifier;
import weka.filters.unsupervised.attribute.Remove;
import java.util.Random;

public class Evaluator {

    public static void main(String[] args) throws Exception {

        // Load the data
        DataSource source = new DataSource("src\\main\\resources\\diabetes.arff");
        Instances data = source.getDataSet();

        // Set the class index
        if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);

        // Create a classifier
        Classifier classifier = new Logistic();

        // Create a filter for removing attributes
        Remove removeFilter = new Remove();
        removeFilter.setAttributeIndices("1"); // Remove the second attribute

        // Create a filtered classifier
        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setFilter(removeFilter);
        filteredClassifier.setClassifier(classifier);

        Evaluate(filteredClassifier, data);

    }

    public static void Evaluate(Classifier classifier, Instances instances) throws Exception {
        Evaluation evaluation = new Evaluation(instances);
        evaluation.crossValidateModel(classifier, instances, 10, new Random(1));
        // Output the results
        System.out.println("Results:");
        System.out.println(evaluation.toSummaryString());
    }
}
