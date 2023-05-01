package datamining.project.reprocessing;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DataReprocessor {
    // Categorical Features: gender, ever_married, work_type, Residence_type, smoking_status
    // Binary Numerical Features: hypertension,heart_disease, stroke
    // Continuous Numerical Features: age, avg_glucose_level, bmi

    // Project root directory
    static final String projectDirectory = System.getProperty("user.dir");
    // Input CSV
    static final String dataCsv = "healthcare-dataset-stroke-data.csv";
    // Output CSV
    static final String outputCsv = "healthcare-dataset-stroke-data-output.csv";

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
        @Nullable String[][] outputTable = null;

        // Read input CSV
        try (CSVReader reader = new CSVReader(new FileReader(String.format("%s/src/main/resources/%s", projectDirectory, dataCsv)))) {
            List<String[]> rows = reader.readAll();
            String[][] table = rows.toArray(new String[rows.size()][]);

            // fill "N/A" with the median of the bmi column
            fillBmiNa(table);

            // Applied binning for all the continuous values
            String[][] tableWithBins = binData(table);

            // print the table
            printTable(tableWithBins);

            outputTable = tableWithBins;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Write output CSV
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(String.format("%s/src/main/resources/%s", projectDirectory, outputCsv)))) {
            if (outputTable == null) {
                throw new RuntimeException("Final outputTable is null");
            }

            // Write the data table to the CSV file
            for (String[] row : outputTable) {
                csvWriter.writeNext(row);
            }

            System.out.println("CSV file has been exported successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void fillBmiNa(String[][] mutTable) {
        // Convert the bmi values to doubles and collect the non-missing ones
        List<Double> bmiValues = new ArrayList<>();
        for (int i = 1; i < mutTable.length; i++) { // start at 1 to skip header row
            String bmiString = mutTable[i][TableColumnIndex.BMI.index];
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
        for (int i = 1; i < mutTable.length; i++) { // start at 1 to skip header row
            String bmiString = mutTable[i][TableColumnIndex.BMI.index];
            if (bmiString.equals("N/A")) {
                mutTable[i][TableColumnIndex.BMI.index] = Double.toString(median);
            }
        }

        System.out.println("BMI median: " + median);
    }

    public static String[][] binData(String[][] table) {
        // Define binning parameters
        int[] bmiBins = {0, 19, 25, 30, 10000};
        String[] bmiLabels = {"Underweight", "Ideal", "Overweight", "Obesity"};

        int[] ageBins = {0, 13, 18, 45, 60, 200};
        String[] ageLabels = {"Children", "Teens", "Adults", "Mid Adults", "Elderly"};

        int[] glucoseBins = {0, 90, 160, 230, 500};
        String[] glucoseLabels = {"Low", "Normal", "High", "Very High"};

        // Create output table with additional columns for bin labels
        int numColumns = table[0].length;
        int numRows = table.length;
        String[][] output = new String[numRows][numColumns + 3];

        // Copy existing table to output table
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                output[i][j] = table[i][j];
            }
        }

        // Bin and label BMI values
        output[0][numColumns] = "BMI_cat";
        for (int i = 1; i < numRows; i++) { // start at 1 to skip header row
            double bmi = Double.parseDouble(table[i][TableColumnIndex.BMI.index]);
            int binIndex = Arrays.binarySearch(bmiBins, (int) Math.round(bmi));

            // Get insertion points between bins
            if (binIndex < 0) {
                binIndex = -(binIndex + 1) - 1;
            }

            output[i][numColumns] = bmiLabels[binIndex];
        }

        // Bin and label age values
        output[0][numColumns + 1] = "Age_cat";
        for (int i = 1; i < numRows; i++) { // start at 1 to skip header row
            double age = Double.parseDouble(table[i][TableColumnIndex.AGE.index]);
            int binIndex = Arrays.binarySearch(ageBins, (int) Math.round(age));

            // Get insertion points between bins
            if (binIndex < 0) {
                binIndex = -(binIndex + 1) - 1;
            }

            output[i][numColumns + 1] = ageLabels[binIndex];
        }

        // Bin and label glucose values
        output[0][numColumns + 2] = "AVG_Glucose_cat";
        for (int i = 1; i < numRows; i++) { // start at 1 to skip header row
            double glucose = Double.parseDouble(table[i][TableColumnIndex.AVG_GLUCOSE_LEVEL.index]);
            int binIndex = Arrays.binarySearch(glucoseBins, (int) Math.round(glucose));

            // Get insertion points between bins
            if (binIndex < 0) {
                binIndex = -(binIndex + 1) - 1;
            }

            output[i][numColumns + 2] = glucoseLabels[binIndex];
        }

        return output;
    }

    public static void printTable(String[][] table) {
        // find the maximum width of each column
        int[] maxColumnWidths = new int[table[0].length];
        for (String[] row : table) {
            for (int i = 0; i < row.length; i++) {
                if (row[i] == null) {
                    maxColumnWidths[i] = Math.max(maxColumnWidths[i], 10);
                } else {
                    maxColumnWidths[i] = Math.max(maxColumnWidths[i], row[i].length());
                }
            }
        }

        // print the table header
        System.out.print("+");
        for (int maxColumnWidth : maxColumnWidths) {
            System.out.print("-".repeat(maxColumnWidth + 2) + "+");
        }
        System.out.println();

        // print the table contents
        for (int j = 0; j < table.length; j++) {
            System.out.print("| ");
            for (int i = 0; i < table[j].length; i++) {
                System.out.printf("%-" + maxColumnWidths[i] + "s | ", table[j][i]);
            }
            System.out.println();
            if (j == 0) {
                System.out.print("+");
                for (int maxColumnWidth : maxColumnWidths) {
                    System.out.print("-".repeat(maxColumnWidth + 2) + "+");
                }
                System.out.println();
            }
        }

        // print the table footer
        System.out.print("+");
        for (int maxColumnWidth : maxColumnWidths) {
            System.out.print("-".repeat(maxColumnWidth + 2) + "+");
        }
        System.out.println();
    }
}