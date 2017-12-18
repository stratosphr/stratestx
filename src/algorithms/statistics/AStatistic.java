package algorithms.statistics;

/**
 * Created by gvoiron on 18/12/17.
 * Time : 20:13
 */
@SuppressWarnings("WeakerAccess")
public abstract class AStatistic<Value> {

    protected final Value value;

    public AStatistic(Value value) {
        this.value = value;
    }

    Value getValue() {
        return value;
    }

    @Override
    public abstract String toString();

}
