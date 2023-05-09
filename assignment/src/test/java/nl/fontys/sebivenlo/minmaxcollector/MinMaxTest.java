

package nl.fontys.sebivenlo.minmaxcollector;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
 
/**
 *
 * @author Pieter van den Hombergh {@code p.vandehombergh@gmail.com}
 */
public class MinMaxTest {


    //@Disabled( "Think TDD" )
    @Test
    public void initiallyNull() {
        
        MinMaxCollector.MinMax<String> m = new MinMaxCollector.MinMax<>( (a,b)-> a.compareTo(b));
        assertThat(m.getMin()).isNull();
        assertThat(m.getMax()).isNull();
    }

    //@Disabled( "Think TDD" )
    @ParameterizedTest
    @CsvSource(value={
        // input, min, max
        "P,P,P",
        "A|B,A,B",
        "B|C|A,A,C",
    
    })
    public void minMaxTest(String inputs, String expectedMin, String expectedMax) {
        
        String[] ins = inputs.split("\\|");
        
        MinMaxCollector.MinMax<String> m = new MinMaxCollector.MinMax<>( (a,b)-> a.compareTo(b));
        
        for ( String in : ins ) {
            m.accept( in );
        }
        
        assertThat(m.getMin()).isEqualTo( expectedMin);
        assertThat(m.getMax()).isEqualTo( expectedMax);
    }
    
}
