package client;

import nl.fontys.sebivenlo.minmaxcollector.MinMaxCollector;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

/**
 * Load test comparison. Did we gain any performance
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class CollectorTest {
    List<String> names = List.of(
            "Jan", "Piet", "Klaas", "Henk"
    );

    private final static Random rnd = new Random( 1234567 );
    private static Base64.Encoder encoder = Base64.getEncoder().withoutPadding();

    private static String makeRandomString() {
        String rs = new String( encoder.encode( ( "" + rnd.nextLong() ).getBytes() ) );
        return rs;
    }
    MinMaxCollector<String> minMaxCollector = MinMaxCollector.minmax(( a, b ) -> a.compareTo( b ) );

    //@Disabled
    @Test
    public void testCollector() {
        Optional<MinMaxCollector.MinMax<String>> collect = names.stream().collect( minMaxCollector );

        assertThat( collect ).isNotEmpty();
        MinMaxCollector.MinMax<String> minmax = collect.get();
        assertThat( minmax.getMin() ).isEqualTo( "Henk" );
        assertThat( minmax.getMax() ).isEqualTo( "Piet" );
//        fail( "testCollector reached it's and. You will know what to do." );
    }

    //@Disabled
    @Test
    public void testemptyTest() {

        List<String> emptyStream = List.of();
        Optional<MinMaxCollector.MinMax<String>> collect = emptyStream.stream().collect( minMaxCollector );
        assertThat( collect ).isEmpty();

    }

    
    //@Disabled
    @Test
    public void testParallel() {
        List<String> list = Stream.iterate( 0L, l->l+1 )
                .limit( 10_000)
                .map( v ->  makeRandomString())
                .collect( Collectors.toList());
        Comparator<String> comp = ( a, b ) -> a.compareTo( b );
        MinMaxCollector.MinMax<String> minmax = list.parallelStream().collect( minMaxCollector ).get();
        
        
        String max = list.parallelStream().max( comp ).get();
        String min = list.parallelStream().min( comp ).get();
        
        
        assertThat( minmax.getMax() ).isEqualTo( max );
        assertThat( minmax.getMin() ).isEqualTo( min );

//        fail( "testParallel reached it's and. You will know what to do." );
    }

    //@Disabled
    @Test
    public void testOneStream() {
        List<String> list = Stream.iterate( 0L, l->l+1 )
                .limit( 10_000)
                .map( v ->  makeRandomString())
                .collect( Collectors.toList());
        Comparator<String> comp = ( a, b ) -> a.compareTo( b );
        MinMaxCollector.MinMax<String> minmax = list.stream().collect( minMaxCollector ).get();
        
        
        String max = list.stream().max( comp ).get();
        String min = list.stream().min( comp ).get();
        
        
        assertThat( minmax.getMax() ).isEqualTo( max );
        assertThat( minmax.getMin() ).isEqualTo( min );

//        fail( "testParallel reached it's and. You will know what to do." );
    }
    
}
