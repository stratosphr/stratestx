package algorithms;

import utilities.Time;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 20:59
 */
public abstract class AComputer<Result> {

    public final ComputerResult<Result> compute() {
        preRun();
        long start = System.nanoTime();
        Result computed = run();
        long end = System.nanoTime();
        postRun();
        return new ComputerResult<>(computed, new Time(end - start));
    }


    @SuppressWarnings({"WeakerAccess", "EmptyMethod"})
    protected void preRun() {
    }

    abstract Result run();


    @SuppressWarnings({"WeakerAccess", "EmptyMethod"})
    protected void postRun() {
    }

}
