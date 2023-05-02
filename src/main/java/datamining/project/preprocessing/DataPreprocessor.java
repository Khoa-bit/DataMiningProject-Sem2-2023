package datamining.project.preprocessing;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;

public class DataPreprocessor {
    
    private boolean isTestMode;
    private String inputFileName, outputFileName, outputARFFName;
    private static String 
        devInputPath = "%s/src/main/resources/%s", 
        realInputPath = "%s/%s",
        devOutputPath = "%s/src/main/resources/output/%s", 
        realOutputPath = "%s/output/%s";
    

    private Instances dataInstance, binningDataInstance;

    public DataPreprocessor(String a, boolean b) {
        this.inputFileName = a;
        this.outputFileName = this.removeExtension(this.inputFileName) + "-output.csv";
        this.outputARFFName = this.removeExtension(this.inputFileName) + "-output.arff";
        this.isTestMode = b;
    }

    public void loadCSVFile() {
        var isSuccess = false;
        var preferredPath = this.isTestMode ? devInputPath : realInputPath;
        var loader = new CSVLoader();
        try {
            loader.setSource(new File(
                String.format(
                    preferredPath,
                    System.getProperty("user.dir"),
                    this.inputFileName
                )
            ));

            this.dataInstance = loader.getDataSet();
            isSuccess = true;
        }

        catch (Exception e) {
            System.out.printf("[ERROR] Can not find file name \"%s\"!\n", this.inputFileName);
        }

        finally {
            if (!isSuccess) {
                System.out.printf("[MAIN] Exiting the program with code 1...\n\n");
                System.exit(1);
            }
        }
    }

    public void preprocessData() {
        fillBmiNA(this.dataInstance);
        binData(this.dataInstance);
    }

    // Outputs preprocessed CSV and ARFF
    public void generateFiles() {
        var preferredPath = this.isTestMode ? devOutputPath : realOutputPath;
        var csvSaver = new CSVSaver();
        var arffSaver = new ArffSaver();
        csvSaver.setInstances(this.binningDataInstance);
        arffSaver.setInstances(this.binningDataInstance);
        
        try {
            csvSaver.setFile(new File(
                String.format(
                    preferredPath, 
                    System.getProperty("user.dir"), 
                    this.outputFileName
                )
            ));

            csvSaver.writeBatch();
            System.out.println("[PREPROCESSING] CSV file modified and saved successfully!");

            arffSaver.setFile(new File(
                String.format(
                    preferredPath,
                    System.getProperty("user.dir"),
                    this.outputARFFName
                )
            ));
            arffSaver.writeBatch();
            System.out.println("[PREPROCESSING] ARFF file generated successfully!");
        }
        catch (Exception e) {
            System.out.println("[ERROR] An error occurred while generating output files! Details as below");
            e.printStackTrace();
        }
    }

    public void fillBmiNA(Instances instances) {
        // Create an array of all the available bmi values
        List<Double> bmiValues = new ArrayList<>();
        for (int i = 0; i < instances.numInstances(); i++) {
            var bmiString = instances.instance(i).stringValue(TableColumnIndex.BMI.index);
            if (!bmiString.equals("N/A")) {
                bmiValues.add(Double.parseDouble(bmiString));
            }
        }

        // Compute the median of the available bmi values
        var median = 0.0;
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
            var bmiString = instances.instance(i).stringValue(TableColumnIndex.BMI.index);
            if (bmiString.equals("N/A")) {
                instances.instance(i).setValue(TableColumnIndex.BMI.index, median);
            }
        }

        System.out.println("[PREPROCESSING] Calculated BMI median from the desired dataset is: " + median);
    }

    public void binData(Instances data) {
        // Define binning parameters
        int[] bmiBins = {0, 19, 25, 30, 10000};
        String[] bmiLabels = {"Underweight", "Ideal", "Overweight", "Obesity"};

        int[] ageBins = {0, 13, 18, 45, 60, 200};
        String[] ageLabels = {"Children", "Teen", "Adult", "MidAdult", "Elderly"};

        int[] glucoseBins = {0, 90, 160, 230, 500};
        String[] glucoseLabels = {"Low", "Normal", "High", "VeryHigh"};

        // Create output Instances object with additional attributes for bin labels
        var output = new Instances(data);

        output.insertAttributeAt(new Attribute(
            "BMI_cat", 
            Arrays.asList(bmiLabels)), 
            output.numAttributes()
        );
        
        output.insertAttributeAt(new Attribute(
            "Age_cat", 
            Arrays.asList(ageLabels)), 
            output.numAttributes()
        );
        
        output.insertAttributeAt(new Attribute(
            "AVG_Glucose_cat", 
            Arrays.asList(glucoseLabels)), 
            output.numAttributes()
        );

        // Bin and label BMI values
        for (int i = 0; i < output.numInstances(); i++) {
            Instance instance = output.instance(i);
            var bmi = instance.value(TableColumnIndex.BMI.index);
            var binIndex = Arrays.binarySearch(bmiBins, (int) Math.round(bmi));

            // Get insertion points between bins
            if (binIndex < 0) {
                binIndex = -(binIndex + 1) - 1;
            }

            instance.setValue(output.attribute("BMI_cat"), bmiLabels[binIndex]);
        }

        // Bin and label age values
        for (int i = 0; i < output.numInstances(); i++) {
            Instance instance = output.instance(i);
            var age = instance.value(TableColumnIndex.AGE.index);
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

        this.binningDataInstance = output;
    }

    public void reportData() {
        System.out.println(this.binningDataInstance);
    }

    private String removeExtension(String s) {
        String separator = System.getProperty("file.separator");
        String filename;
    
        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }
    
        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return filename;
    
        return filename.substring(0, extensionIndex);
    }

    public Instances getARFFData() {
        Instances outputARFFData = null;
        var isSuccess = true;
        try {
            var preferredPath = this.isTestMode ? devOutputPath : realOutputPath;
            var src = new DataSource(
                String.format(
                    preferredPath,
                    System.getProperty("user.dir"),
                    this.outputARFFName
                )
            );
            var arffInstances = src.getDataSet();

            // Various Filtering/Converting operations (NEEDS REVISION)
            var nb = new NumericToNominal();
            int[] toBeConverted = {3, 4, 11};
                nb.setAttributeIndicesArray(toBeConverted);
                nb.setInputFormat(arffInstances);
			var newARFFData = Filter.useFilter(arffInstances, nb);
			
            // Deletion operations
            var remover = new Remove();
            int[] toBeKept = {1, 3, 4, 5, 6, 7, 10, 11, 12, 13, 14};
            // int[] toBeRemoved = {9, 8, 2, 0};
                // remover.setAttributeIndicesArray(toBeRemoved);
                remover.setAttributeIndicesArray(toBeKept);
                remover.setInvertSelection(true);
                remover.setInputFormat(newARFFData);
            newARFFData = Filter.useFilter(newARFFData, remover);
            outputARFFData = newARFFData;

            // Set class index to "Stroke" attribute
            outputARFFData.setClassIndex(7);
        }

        catch (Exception e) {
            isSuccess = false;
            System.out.printf("\n[ERROR] An error occurred while trying to import an ARFF file!\n");
            e.printStackTrace();
        }

        finally {
            if (!isSuccess) {
                System.out.printf("\n[MAIN] Exiting the program with code 1...\n\n");
                System.exit(1);
            }
        }

        return outputARFFData;
    }

    public List<String> getDatasetAttributes(boolean shouldPrint) {
        var atts = getARFFData().enumerateAttributes();
        List<String> returnList = new ArrayList<>();
        while (atts.hasMoreElements()) {
            var currentAttribute = atts.nextElement();
            System.out.println(currentAttribute.name());

            if (shouldPrint) returnList.add(currentAttribute.name());
        }

        return returnList;
    }
    
    public Instances getBinningDataInstances() { return this.binningDataInstance; }
}