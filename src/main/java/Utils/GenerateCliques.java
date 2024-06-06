package Utils;

import org.graph4j.Graph;

import java.util.*;

public class GenerateCliques {
    private final Graph graph;
    private int[] orderedVertexList;
    private Map<Integer, Set<Integer>> rightNeighbours;
    private Map<Integer, Set<Integer>> children;
    private Map<Integer, Integer> parent;

    private Set<Integer> pivots = new HashSet<>();
    private List<Integer> orderedCliques = new LinkedList<>();
    private Map<Integer, Set<Integer>> clique = new HashMap<>();



    public GenerateCliques(Graph graph, int[] orderedVertexList, Map<Integer, Set<Integer>> rightNeighbours, Map<Integer, Set<Integer>> children, Map<Integer, Integer> parent)
    {
        this.graph = graph;
        this.orderedVertexList = orderedVertexList;
        this.rightNeighbours = rightNeighbours;
        this.children = children;
        this.parent = parent;
    }
    public void generate()
    {
        Set<Integer> childrenRoot = children.get(orderedVertexList[orderedVertexList.length - 1]);
        Set<Integer> cliques = new HashSet<>();
        clique.put(orderedVertexList[orderedVertexList.length - 1], new HashSet<>());
        clique.get(orderedVertexList[orderedVertexList.length - 1]).add(orderedVertexList[orderedVertexList.length - 1]);
        generateCliques(childrenRoot, clique, cliques);

        for(var currentClique : cliques)
        {
            pivots.addAll(clique.get(currentClique));
        }

        Stack<List<Integer>> partitions = new Stack<>();
        partitions.add(new LinkedList<>(cliques));

        while (!partitions.isEmpty())
        {
            Set<Integer> inPartition = new HashSet<>();
            Set<Integer> outPartition = new HashSet<>();

            List<Integer> lastPartition = partitions.pop();

            orderedCliques.add(0, lastPartition.remove(0));
            List<Integer> pivotClique = new ArrayList<>(clique.get(orderedCliques.get(0)));

            for(var currentPartition : lastPartition)
            {
                if(!Collections.disjoint(pivotClique, clique.get(currentPartition)))
                {
                    inPartition.add(currentPartition);
                }
                else
                {
                    outPartition.add(currentPartition);
                }
            }
            if(!outPartition.isEmpty())
            {
                partitions.add(new LinkedList<>(outPartition));
            }
            if(!inPartition.isEmpty())
            {
                partitions.add(new LinkedList<>(inPartition));
            }
        }
    }

    private void generateCliques(Set<Integer> childrenNodes, Map<Integer, Set<Integer>> clique, Set<Integer> cliques)
    {
        for(int vertex : childrenNodes)
        {
            clique.put(vertex, new HashSet<>());
            for(var neighbour : rightNeighbours.get(vertex))
            {
                clique.get(vertex).add(neighbour); // Add elements from rightNeighbours to clique
            }
            clique.get(vertex).add(vertex);

            int currentParent = parent.get(vertex);

            boolean isSubset = true;
            for(var parentClique : clique.get(currentParent))
            {
                if(!clique.get(vertex).contains(parentClique))
                {
                    isSubset = false;
                    break;
                }
            }
            if(isSubset)
            {
                cliques.remove(currentParent);
            }
            cliques.add(vertex);
            if(!children.get(vertex).isEmpty())
            {
                generateCliques(children.get(vertex), clique, cliques);
            }
        }
    }

    public Set<Integer> getPivots() {
        return pivots;
    }

    public List<Integer> getOrderedCliques() {
        return orderedCliques;
    }

    public Map<Integer, Set<Integer>> getClique() {
        return clique;
    }

}
