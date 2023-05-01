package datamining.project.evaluation;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.output.prediction.AbstractOutput;
import weka.core.Instance;

public class CustomOutput extends AbstractOutput {
    public CustomOutput() {
        super();
    }

    @Override
    public String globalInfo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'globalInfo'");
    }

    @Override
    public String getDisplay() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDisplay'");
    }

    @Override
    protected void doPrintHeader() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doPrintHeader'");
    }

    @Override
    protected void doPrintClassification(Classifier classifier, Instance inst, int index) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doPrintClassification'");
    }

    @Override
    protected void doPrintClassification(double[] dist, Instance inst, int index) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doPrintClassification'");
    }

    @Override
    protected void doPrintFooter() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doPrintFooter'");
    }
}
