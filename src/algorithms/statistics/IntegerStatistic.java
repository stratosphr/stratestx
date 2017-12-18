package algorithms.statistics;

/**
 * Created by gvoiron on 18/12/17.
 * Time : 20:21
 */
public final class IntegerStatistic extends AStatistic<Integer> {

    IntegerStatistic(Integer value) {
        super(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
