package algorithms.statistics;

import utilities.Time;

/**
 * Created by gvoiron on 18/12/17.
 * Time : 20:21
 */
public final class TimeStatistic extends AStatistic<Time> {

    TimeStatistic(Time value) {
        super(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
