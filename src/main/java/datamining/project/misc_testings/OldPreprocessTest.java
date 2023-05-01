package datamining.project.misc_testings;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OldPreprocessTest {
    // Categorical Features: gender, ever_married, work_type, Residence_type, smoking_status
    // Binary Numerical Features: hypertension,heart_disease, stroke
    // Continuous Numerical Features: age, avg_glucose_level, bmi

    // Project root directory
    static final String projectDirectory = System.getProperty("user.dir");
    // Input CSV
    static final String dataCsv = "healthcare-dataset-stroke-data.csv";
    // Output CSV
    static final String outputCsv = "healthcare-dataset-stroke-data-output.csv";

    // Output ARFF
    static final String outputArff = "healthcare-dataset-stroke-data-output.arff";

    // Map column with its ordering indices
    enum TableColumnIndex {
        ID(0),
        GENDER(1),
        AGE(2),
        HYPERTENSION(3),
        HEART_DISEASE(4),
        EVER_MARRIED(5),
        WORK_TYPE(6),
        RESIDENCE_TYPE(7),
        AVG_GLUCOSE_LEVEL(8),
        BMI(9),
        SMOKING_STATUS(10),
        STROKE(11);

        private final int index;

        TableColumnIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public static void main(String[] args) {
        try {
            CSVLoader csvLoader = new CSVLoader();
            csvLoader.setSource(new File(String.format("%s/src/main/resources/%s", projectDirectory, dataCsv)));
            Instances data = csvLoader.getDataSet();

            // fill "N/A" with the median of the bmi column
            fillBmiNa(data);

            // Applied binning for all the continuous values
            Instances binData = binData(data);

            // print the table
            // printTable(tableWithBins);
            System.out.println(binData);


            // Output to CSV file
            CSVSaver csvSaver = new CSVSaver();
            csvSaver.setInstances(binData);
            csvSaver.setFile(new File(String.format("%s/src/main/resources/output/%s", projectDirectory, outputCsv)));
            csvSaver.writeBatch();
            System.out.println("Save to CSV successfully!");

            // Output to ARFF file
            ArffSaver arffSaver = new ArffSaver();
            arffSaver.setInstances(binData);
            arffSaver.setFile(new File(String.format("%s/src/main/resources/output/%s", projectDirectory, outputArff)));
            arffSaver.writeBatch();
            System.out.println("Save to ARFF successfully!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fillBmiNa(Instances instances) {
        // Create an array of all the available bmi values
        List<Double> bmiValues = new ArrayList<>();
        for (int i = 0; i < instances.numInstances(); i++) {
            String bmiString = instances.instance(i).stringValue(TableColumnIndex.BMI.index);
            if (!bmiString.equals("N/A")) {
                bmiValues.add(Double.parseDouble(bmiString));
            }
        }

        // Compute the median of the available bmi values
        double median;
        if (bmiValues.isEmpty()) {
            // If there are no available values, use 0.0 as the median
            median = 0.0;
        } else {
            Collections.sort(bmiValues);
            int middleIndex = bmiValues.size() / 2;
            if (bmiValues.size() % 2 == 0) {
                median = (bmiValues.get(middleIndex - 1) + bmiValues.get(middleIndex)) / 2.0;
            } else {
                median = bmiValues.get(middleIndex);
            }
        }

        // Fill in the missing bmi values with the median
        for (int i = 0; i < instances.numInstances(); i++) {
            String bmiString = instances.instance(i).stringValue(TableColumnIndex.BMI.index);
            if (bmiString.equals("N/A")) {
                instances.instance(i).setValue(TableColumnIndex.BMI.index, median);
            }
        }

        System.out.println("BMI median: " + median);
    }

    public static Instances binData(Instances data) {
        // Define binning parameters
        int[] bmiBins = {0, 19, 25, 30, 10000};
        String[] bmiLabels = {"Underweight", "Ideal", "Overweight", "Obesity"};

        int[] ageBins = {0, 13, 18, 45, 60, 200};
        String[] ageLabels = {"Children", "Teens", "Adults", "Mid Adults", "Elderly"};

        int[] glucoseBins = {0, 90, 160, 230, 500};
        String[] glucoseLabels = {"Low", "Normal", "High", "Very High"};

        // Create output Instances object with additional attributes for bin labels
        Instances output = new Instances(data);

        output.insertAttributeAt(new Attribute("BMI_cat", Arrays.asList(bmiLabels)), output.numAttributes());
        output.insertAttributeAt(new Attribute("Age_cat", Arrays.asList(ageLabels)), output.numAttributes());
        output.insertAttributeAt(new Attribute("AVG_Glucose_cat", Arrays.asList(glucoseLabels)), output.numAttributes());

        // Bin and label BMI values
        for (int i = 0; i < output.numInstances(); i++) {
            Instance instance = output.instance(i);
            double bmi = instance.value(TableColumnIndex.BMI.index);
            int binIndex = Arrays.binarySearch(bmiBins, (int) Math.round(bmi));

            // Get insertion points between bins
            if (binIndex < 0) {
                binIndex = -(binIndex + 1) - 1;
            }

            instance.setValue(output.attribute("BMI_cat"), bmiLabels[binIndex]);
        }

        // Bin and label age values
        for (int i = 0; i < output.numInstances(); i++) {
            Instance instance = output.instance(i);
            double age = instance.value(TableColumnIndex.AGE.index);
            int binIndex = Arrays.binarySearch(ageBins, (int) Math.round(age));

            // Get insertion points between bins
            if (binIndex < 0) {
                binIndex = -(binIndex + 1) - 1;
            }

            instance.setValue(output.attribute("Age_cat"), ageLabels[binIndex]);
        }

        // Bin and label glucose values
        for (int i = 0; i < output.numInstances(); i++) {
            Instance instance = output.instance(i);
            double glucose = instance.value(TableColumnIndex.AVG_GLUCOSE_LEVEL.index);
            int binIndex = Arrays.binarySearch(glucoseBins, (int) Math.round(glucose));

            // Get insertion points between bins
            if (binIndex < 0) {
                binIndex = -(binIndex + 1) - 1;
            }

            instance.setValue(output.attribute("AVG_Glucose_cat"), glucoseLabels[binIndex]);
        }

        return output;
    }
}