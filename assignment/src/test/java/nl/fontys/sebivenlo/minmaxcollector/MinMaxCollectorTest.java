package nl.fontys.sebivenlo.minmaxcollector;

import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import nl.fontys.sebivenlo.minmaxcollector.MinMaxCollector.MinMax;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 *
 * @author "Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}"
 */
public class MinMaxCollectorTest {

    /**
     * Test that a combiner will combine two minmax-es  into one.
     * 
     */
    //@Disabled("Think TDD")
    @Test
    public void testCombiner() {
        Comparator<String> comp = ( a, b ) -> a.compareTo( b );
        MinMaxCollector<String> mmc = new MinMaxCollector<>( ( a, b )
                -> a.compareTo( b ) );
        BinaryOperator<MinMaxCollector.MinMax<String>> combiner = mmc.combiner();
        MinMax<String> a = new MinMax<>( comp );
        MinMax<String> b = new MinMax<>( comp );
        MinMax<String> combined = combiner.apply( a, b );
        assertThat( combined.getMin() ).isNull();
        assertThat( combined.getMax() ).isNull();
//        fail( "method testComibiner reached end. You know what to do." );
    }

    //@Disabled("Think TDD")
    @ParameterizedTest
    @CsvSource( {
        "AB,CD,A,D",
        "A,CD,A,D",
        "C,A,A,C"
    } )
    public void combinerValues( String inp1, String inp2, String min, String max ) {

        Comparator<String> comp = ( a, b ) -> a.compareTo( b );
        MinMaxCollector<String> mmc = new MinMaxCollector<>( ( a, b )
                -> a.compareTo( b ) );
        BinaryOperator<MinMaxCollector.MinMax<String>> combiner = mmc.combiner();

        MinMax<String> a = new MinMax<>( comp );
        MinMax<String> b = new MinMax<>( comp );

        String[] split = inp1.split( "" );
        for ( String string : split ) {
            a.accept( string );
        }

        split = inp2.split( "" );
        for ( String string : split ) {
            b.accept( string );
        }

        MinMax<String> combined = combiner.apply( a, b );
        SoftAssertions.assertSoftly( softly -> {
            softly.assertThat( combined.getMin() ).isEqualTo( min );
            softly.assertThat( combined.getMax() ).isEqualTo( max );
        } );

//        fail( "method combinerValues reached end. You know what to do." );
    }
    
    @Test
    public void testCharacteristics() {
        final Comparator<Integer> comparator = (a, b) -> b - a;
        final MinMaxCollector<Integer> mmc = new MinMaxCollector<>(comparator);
        assertThat(mmc.characteristics()).containsExactlyInAnyOrder(
                Collector.Characteristics.UNORDERED, 
                Collector.Characteristics.CONCURRENT);
    }
}
