package algorithms.statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by gvoiron on 18/12/17.
 * Time : 20:21
 */
public final class PercentageStatistic extends AStatistic<Double> {

    PercentageStatistic(Double value) {
        super(value);
    }

    @Override
    public String toString() {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toString();
    }

}
