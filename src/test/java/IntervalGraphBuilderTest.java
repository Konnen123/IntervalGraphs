import Generation.IntervalGraphBuilder;
import org.graph4j.Graph;
import org.junit.Test;

public class IntervalGraphBuilderTest {
    @Test
    public void buildGraph1()
    {
        Graph intervalGraph = IntervalGraphBuilder.build(4);
        System.out.println(intervalGraph);
    }
    @Test
    public void buildGraph2()
    {
        Graph intervalGraph = IntervalGraphBuilder.build(7);
        System.out.println(intervalGraph);
    }
    @Test
    public void buildGraph3()
    {
        Graph intervalGraph = IntervalGraphBuilder.build(9);
        System.out.println(intervalGraph);
    }
    @Test
    public void buildGraph4()
    {
        long startTime = System.nanoTime();
        Graph intervalGraph = IntervalGraphBuilder.build(14);
        long endTime = System.nanoTime();

        // Calculate elapsed time
        long elapsedTime = endTime - startTime;

        // Output the results
        System.out.println("Elapsed time in milliseconds: " + elapsedTime / 1_000_000.0);
        System.out.println(intervalGraph);
    }
}
