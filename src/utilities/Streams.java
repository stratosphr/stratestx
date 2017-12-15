package utilities;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by gvoiron on 10/12/17.
 * Time : 17:35
 */
@SuppressWarnings("WeakerAccess")
public final class Streams {

    /**
     * Converts an {@link java.util.Iterator} to {@link java.util.stream.Stream}.
     */
    public static <T> Stream<T> iterate(Iterator<T> iterator) {
        int characteristics = Spliterator.ORDERED | Spliterator.IMMUTABLE;
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, characteristics), false);
    }

    /**
     * Zips the specified stream with its indices.
     */
    public static <T> Stream<Map.Entry<Integer, T>> zipWithIndex(Stream<T> stream) {
        return iterate(new Iterator<Map.Entry<Integer, T>>() {
            private final Iterator<? extends T> streamIterator = stream.iterator();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return streamIterator.hasNext();
            }

            @Override
            public Map.Entry<Integer, T> next() {
                return new AbstractMap.SimpleImmutableEntry<>(index++, streamIterator.next());
            }
        });
    }

    /**
     * Returns a stream consisting of the results of applying the given two-arguments function to the elements of this stream.
     * The first argument of the function is the element index and the second one - the element value.
     */
    public static <T, R> Stream<R> mapWithIndex(Stream<T> stream, BiFunction<Integer, T, R> mapper) {
        return zipWithIndex(stream).map(entry -> mapper.apply(entry.getKey(), entry.getValue()));
    }

    /**
     * Returns a stream consisting of the results of applying the given two-arguments function to the elements of this stream.
     * The first argument of the function is the element index and the second one - the element value.
     */
    public static <T, R> Stream<Map.Entry<Integer, T>> filterWithIndex(Stream<T> stream, Predicate<Map.Entry<Integer, T>> predicate) {
        return zipWithIndex(stream).filter(predicate);
    }

}
