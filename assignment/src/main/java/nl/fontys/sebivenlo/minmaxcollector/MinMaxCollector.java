package nl.fontys.sebivenlo.minmaxcollector;

import nl.fontys.sebivenlo.minmaxcollector.MinMaxCollector.MinMax;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import static java.util.stream.Collector.Characteristics.*;

/**
 * Reduce/collect a stream to a min max pair.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <T>
 *
 */
public class MinMaxCollector<T> implements Collector<T, MinMax<T>, Optional<MinMax<T>>> {

    //Define fields
    private Comparator<T> comparator;

    /**
     * Min and max need a comparator.
     *
     * @param comparator to determine min and max of the stream
     */
    public MinMaxCollector( Comparator<T> comparator ) {
        this.comparator = comparator;
    }

    @Override
    public Supplier<MinMax<T>> supplier() {
        return () -> new MinMax<>(this.comparator);
    }

    @Override
    public BiConsumer<MinMax<T>, T> accumulator() {
        return MinMax::accept;
    }

    @Override
    public BinaryOperator<MinMax<T>> combiner() {
        return (minmax1, minmax2) -> {
            var result = supplier().get();
            result.accept(minmax1.min);
            result.accept(minmax1.max);
            result.accept(minmax2.min);
            result.accept(minmax2.max);

            return result;
        };
    }

    @Override
    public Function<MinMax<T>, Optional<MinMax<T>>> finisher() {
        return minMax -> {
            if (minMax.getMin() == null || minMax.getMax() == null) {
                return Optional.empty();
            }

            return Optional.of(minMax);
        };
    }

    @Override
    public Set<Collector.Characteristics> characteristics() {
        return Set.of(UNORDERED, CONCURRENT);
    }

    public static <T> MinMaxCollector<T> minmax( Comparator<T> comp ) {
        return new MinMaxCollector<>( comp );
    }

    /**
     * Helper min max pair class. This implementation is not thread safe. The
     * public API of this class allows only retrieval of min and max.
     *
     * @param <E> type of element.
     */
    public static class MinMax<E> implements Consumer<E> {

        private E min;
        private E max;
        private final Comparator<E> comparator;

        
        MinMax( Comparator<E> comp ) {
            this.comparator = comp;
        }
        
        public E getMin() {
            return min;
        }

        public E getMax() {
            return max;
        }

        public void accept( E next ) {
            //TODO implement accept.
            if (next == null) {
                return;
            }
            if (getMax() == null || getMin() == null) {
                this.min = next;
                this.max = next;
            }

            if (comparator.compare(getMax(), next) < 0) {
                this.max = next;
            }
            if (comparator.compare(getMin(), next) > 0) {
                this.min = next;
            }

        }
    }
}
