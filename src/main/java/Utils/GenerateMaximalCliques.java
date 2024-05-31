package Utils;

import org.graph4j.Graph;

import java.util.*;

public class GenerateMaximalCliques {
    private final Graph graph;
    private int[] orderedVertexList;
    private List<List<Integer>> rightNeighbours;
    private List<List<Integer>> children;
    private Map<Integer, Integer> parent;

    private Set<Integer> pivots = new HashSet<>();
    private List<Integer> orderedCliques = new LinkedList<>();
    private Map<Integer, List<Integer>> clique = new HashMap<>();



    public GenerateMaximalCliques(Graph graph, int[] orderedVertexList, List<List<Integer>> rightNeighbours, List<List<Integer>> children, Map<Integer, Integer> parent)
    {
        this.graph = graph;
        this.orderedVertexList = orderedVertexList;
        this.rightNeighbours = rightNeighbours;
        this.children = children;
        this.parent = parent;
    }
    public void generate()
    {
        List<Integer> childrenRoot = children.get(orderedVertexList[orderedVertexList.length - 1]);
        List<Integer> cliques = new ArrayList<>();
        clique.put(orderedVertexList[orderedVertexList.length - 1], new LinkedList<>());
        clique.get(orderedVertexList[orderedVertexList.length - 1]).add(orderedVertexList[orderedVertexList.length - 1]);
        generateCliques(childrenRoot, clique, cliques);

        for(int i=0;i<cliques.size();i++)
        {
            pivots.addAll(clique.get(cliques.get(i)));
        }

        Stack<List<Integer>> partitions = new Stack<>();
        partitions.add(cliques);

        while (!partitions.isEmpty())
        {
            Set<Integer> inPartition = new HashSet<>();
            Set<Integer> outPartition = new HashSet<>();

            List<Integer> lastPartition = partitions.pop();

            orderedCliques.add(0, lastPartition.remove(0));
            List<Integer> pivotClique = clique.get(orderedCliques.get(0));

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
                partitions.add(new ArrayList<>(outPartition));
            }
            if(!inPartition.isEmpty())
            {
                partitions.add(new ArrayList<>(inPartition));
            }
        }
    }

    private void generateCliques(List<Integer> childrenNodes, Map<Integer, List<Integer>> clique, List<Integer> cliques)
    {
        for(int vertex : childrenNodes)
        {
            clique.put(vertex, new LinkedList<>());
            for(var neighbour : rightNeighbours.get(vertex))
            {
                clique.get(vertex).add(neighbour); // Add elements from rightNeighbours to clique
            }
            clique.get(vertex).add(vertex);

            int currentParent = parent.get(vertex);

            boolean isSubset = true;
            for(var parentClique : clique.get(currentParent))
            {
                boolean existsElement = false;
                for(var vertexClique : clique.get(vertex))
                {
                    if(Objects.equals(vertexClique, parentClique))
                    {
                        existsElement = true;
                        break;
                    }
                }
                if(!existsElement)
                {
                    isSubset = false;
                    break;
                }
            }
            if(isSubset && cliques.contains(currentParent))
            {
                cliques.remove((Object)currentParent);
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

    public Map<Integer, List<Integer>> getClique() {
        return clique;
    }

    private int intersectionLength(List<Integer> l1, List<Integer> l2)
    {
        int count = 0;
        for(var elementL1 : l1)
        {
            if(l2.contains(elementL1))
            {
                count++;
            }
        }
        return count;
    }
}
