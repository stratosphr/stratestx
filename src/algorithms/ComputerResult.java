package algorithms;

import utilities.Time;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 21:03
 */
public final class ComputerResult<Result> {

    private final Result computed;
    private final Time time;

    public ComputerResult(Result computed, Time time) {
        this.computed = computed;
        this.time = time;
    }

    public Result getResult() {
        return computed;
    }

    public Time getTime() {
        return time;
    }

}
