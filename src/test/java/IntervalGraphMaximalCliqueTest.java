import Generation.IntervalGraphBuilder;
import Recognition.IntervalGraphRecognition;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;
import org.junit.Test;

public class IntervalGraphMaximalCliqueTest {
    @Test
    public void maxClique1()
    {
        Graph graph = GraphBuilder.empty().buildGraph();
        graph.addVertices(2,5,0,3,4,7,1,6);
        graph.addEdge(0, 5);

        graph.addEdge(1, 7);

        graph.addEdge(2, 5);
        graph.addEdge(2, 7);

        graph.addEdge(3, 6);
        graph.addEdge(3, 7);

        graph.addEdge(4, 7);
        graph.addEdge(4, 6);

        graph.addEdge(5, 6);
        graph.addEdge(5, 7);

        graph.addEdge(6, 7);

        var maxClique = MaxClique.IntervalGraphMaximalClique.findMaximalClique(graph);

        System.out.println("Test 1");
        for(var node : maxClique)
        {
            System.out.print(node + ", ");
        }
        System.out.println();
    }
    @Test
    public void maxClique2()
    {
        Graph graph = GraphBuilder.empty().buildGraph();
        graph.addVertices(0,1,2,3);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,0);
        graph.addEdge(0,2);

        var maxClique = MaxClique.IntervalGraphMaximalClique.findMaximalClique(graph);
        System.out.println("Test 2");
        for(var node : maxClique)
        {
            System.out.print(node + ", ");
        }
        System.out.println();
    }
    @Test
    public void maxClique3()
    {
        Graph graph = GraphBuilder.empty().buildGraph();
        graph.addVertices(0,1,2,3,4,5,6);
        graph.addEdge(0,1);
        graph.addEdge(0,2);
        graph.addEdge(0,3);

        graph.addEdge(1,2);

        graph.addEdge(2,3);
        graph.addEdge(2,4);
        graph.addEdge(2,5);

        graph.addEdge(3,4);
        graph.addEdge(3,5);

        graph.addEdge(5,6);

        var maxClique = MaxClique.IntervalGraphMaximalClique.findMaximalClique(graph);
        System.out.println("Test 3");
        for(var node : maxClique)
        {
            System.out.print(node + ", ");
        }
        System.out.println();
    }
    @Test
    public void maxClique4()
    {
        Graph graph = GraphBuilder.empty().buildGraph();
        graph.addVertices(0,1,2,3);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);


        var maxClique = MaxClique.IntervalGraphMaximalClique.findMaximalClique(graph);
        System.out.println("Test 4");
        for(var node : maxClique)
        {
            System.out.print(node + ", ");
        }
        System.out.println();
    }
}
