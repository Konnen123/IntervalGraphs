package Recognition;

import Utils.ChordalityTest;
import Utils.GenerateMaximalCliques;
import Utils.LexicographicBFS;
import org.graph4j.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IntervalGraphRecognition {
    //A graph is an interval graph if and only if it is chordal and AT-free
    /*Algorithm to recognize an interval graph:
    1. Create a lexicographicBFS and get the order of the vertices
    2. Using step 1, if graph is not chordal, reject
    3. Compute the maximal cliques and the clique tree
    4. For each vertex x, if cliques containing x are not in a consecutive order, graph is not interval
    5. Else, graph is interval
    */

    private final Graph graph;
    public IntervalGraphRecognition(Graph graph)
    {
        this.graph = graph;
    }

    public boolean check()
    {
        int[] orderedList = (new LexicographicBFS(graph)).getOrderedVertexList();

        ChordalityTest chordalityTest = new ChordalityTest(graph, orderedList);
        boolean isGraphChordal = chordalityTest.checkChordality();

        if(!isGraphChordal)
        {
            System.out.println("Error! Graph is not chordal");
            return false;
        }

        List<List<Integer>> rightNeighbours = chordalityTest.getRightNeighbours();
        Map<Integer, Integer> parent = chordalityTest.getParent();
        List<List<Integer>> children = chordalityTest.getChildren();

        GenerateMaximalCliques generateMaximalCliques = new GenerateMaximalCliques(graph, orderedList, rightNeighbours, children, parent);
        generateMaximalCliques.generate();

        Map<Integer, List<Integer>> cliques = generateMaximalCliques.getClique();
        List<Integer> orderedCliques = generateMaximalCliques.getOrderedCliques();
        Set<Integer> pivots = generateMaximalCliques.getPivots();

        return checkConsecutiveCliques(cliques, orderedCliques, pivots);

    }
    private boolean checkConsecutiveCliques(Map<Integer, List<Integer>> cliques, List<Integer> orderedCliques, Set<Integer> pivots)
    {
        for(var currentPivot : pivots)
        {
            int p = 0;
            int count = 0;
            for(var currentOrderedClique : orderedCliques)
            {
                // If the currentPivot is in the maximal clique
                if(cliques.get(currentOrderedClique).contains(currentPivot))
                {
                    if(p == 0)
                    {
                        p = count + 1;
                    }
                    else
                    {
                        p++;
                        if(p != count + 1)
                        {
                            System.out.println("Error! Graph has not consecutive cliques!");
                            return false;
                        }
                    }

                }

                count++;
            }
        }
        return true;
    }


}
