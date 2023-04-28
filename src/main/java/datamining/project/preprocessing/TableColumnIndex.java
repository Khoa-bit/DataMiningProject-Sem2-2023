package datamining.project.preprocessing;

public enum TableColumnIndex {
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

    public final int index;

    TableColumnIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
