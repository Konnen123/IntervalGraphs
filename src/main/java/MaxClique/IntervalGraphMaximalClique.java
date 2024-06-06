package MaxClique;

import Utils.ChordalityTest;
import Utils.GenerateCliques;
import Utils.LexicographicBFS;
import org.graph4j.Graph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IntervalGraphMaximalClique {
    public static Set<Integer> findMaximalClique(Graph graph)
    {
        LexicographicBFS lexicographicBFS = new LexicographicBFS(graph);
        int[] lexicographicalOrder = lexicographicBFS.getOrderedVertexList();

        ChordalityTest chordalityTest = new ChordalityTest(graph, lexicographicalOrder);
        boolean isGraphChordal = chordalityTest.checkChordality();

        if(!isGraphChordal)
        {
            System.out.println("Error! Graph is not chordal, cannot check for maximal clique");
            return new HashSet<>();
        }

        Map<Integer, Set<Integer>> rightNeighbours = chordalityTest.getRightNeighbours();
        Map<Integer, Integer> parent = chordalityTest.getParent();
        Map<Integer, Set<Integer>> children = chordalityTest.getChildren();

        GenerateCliques generateCliques = new GenerateCliques(graph, lexicographicalOrder, rightNeighbours, children, parent);
        generateCliques.generate();

        Map<Integer, Set<Integer>> cliques = generateCliques.getClique();

        int size = 0;
        int key = -1;
        for(var cliquePair : cliques.entrySet())
        {
            var currentClique = cliquePair.getValue();
            if(currentClique.size() > size)
            {
                size = currentClique.size();
                key = cliquePair.getKey();
            }
        }
        if(key == -1)
            return new HashSet<>();

        return cliques.get(key);
    }
}
