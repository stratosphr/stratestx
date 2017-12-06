package algorithms;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 21:03
 */
public final class ComputerResult<Result> {

    private final Result computed;
    private final long time;

    ComputerResult(Result computed, long time) {
        this.computed = computed;
        this.time = time;
    }

    public Result getResult() {
        return computed;
    }

    public long getTime() {
        return time;
    }

}
