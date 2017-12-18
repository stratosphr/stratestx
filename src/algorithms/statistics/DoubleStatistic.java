package algorithms.statistics;

/**
 * Created by gvoiron on 18/12/17.
 * Time : 20:21
 */
public final class DoubleStatistic extends AStatistic<Double> {

    DoubleStatistic(Double value) {
        super(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
