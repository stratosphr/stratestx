package algorithms.statistics;

/**
 * Created by gvoiron on 18/12/17.
 * Time : 20:13
 */

abstract class AStatistic<Value> {

    final Value value;

    AStatistic(Value value) {
        this.value = value;
    }

    Value getValue() {
        return value;
    }

    @Override
    public abstract String toString();

}
