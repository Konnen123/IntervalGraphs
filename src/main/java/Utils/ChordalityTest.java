package Utils;

import org.graph4j.Graph;

import java.util.*;

public class ChordalityTest {

    private Graph graph;

    private int[] orderedVertexList;

    // For each vertex, store its neighbours that appear on the right side of the LexBFS list.
    private Map<Integer, Set<Integer>> rightNeighbours;
    // Parent of the current vertex is the first right neighbour element
    // Parent object is a map, key - vertex, value - parent
    private Map<Integer, Integer> parent;
    // For each vertex, store the list of right neighbours without its parent.
    private Map<Integer, Set<Integer>> rightNeighboursWithoutParent;
    private Map<Integer, Set<Integer>> children;

    public ChordalityTest(Graph graph, int[] orderedVertexList)
    {
        this.graph = graph;

        LexicographicBFS lexicographicBFS = new LexicographicBFS(graph);
        this.orderedVertexList = orderedVertexList;

        rightNeighbours = new HashMap<>();
        parent = new HashMap<>();
        rightNeighboursWithoutParent = new HashMap<>();
        children = new HashMap<>();

        initializeContainers();
    }

    public boolean checkChordality() {
        // Iterate through each element from the ordered list, except the last one
        for(int i=0;i<orderedVertexList.length - 1;i++)
        {
           // Get the vertex and the parent
           int vertex = orderedVertexList[i];
           int currentParent = parent.get(vertex);

           // Now check if rightNeighbourNoParent list is a sublist of rightNeighbour
           for(var rightNeighbourNoParentElement : rightNeighboursWithoutParent.get(vertex))
           {
               // If the element cannot be found in the set, it means it is not a sublist, return false
               if(!rightNeighbours.get(currentParent).contains(rightNeighbourNoParentElement))
                   return false;
           }

        }

        return true;
     }
    private void initializeContainers()
    {
        for(var vertex : orderedVertexList)
        {
            rightNeighbours.put(vertex, new HashSet<>());
            rightNeighboursWithoutParent.put(vertex, new HashSet<>());
            children.put(vertex, new HashSet<>());
        }
        // Iterate to orderVertexList.length - 1, because the last element doesn't have a parent (it is the start point of the LexBFS algorithm)
        for(int i=0;i<orderedVertexList.length - 1;i++)
        {
            int vertex = orderedVertexList[i];
            int parentVertex = Integer.MIN_VALUE; // Used to know which vertex was first chosen to set as a parent
            for(int j=i+1;j<orderedVertexList.length;j++)
            {
                int jVertex = orderedVertexList[j];
                if(graph.containsEdge(vertex, jVertex))
                {
                    // Set the first jVertex that has an edge with the vertex as a parent of the current vertex
                    if(parentVertex == Integer.MIN_VALUE)
                        parentVertex = jVertex;

                    // If an edge exists, we already know that jVertex is on the right side of vertex, so add it to the rightNeighbours
                    rightNeighbours.get(vertex).add(jVertex);
                }
            }
            // Add the parent and children
            parent.put(vertex,parentVertex);
            children.get(parentVertex).add(vertex);

            for(var neighbour : rightNeighbours.get(vertex))
            {
                // If the neighbour is parent, skip this iteration
                if(neighbour == parentVertex)
                    continue;

                // Populate the list without the parent
                rightNeighboursWithoutParent.get(vertex).add(neighbour);
            }
        }
    }

    public Map<Integer, Set<Integer>> getRightNeighbours() {
        return rightNeighbours;
    }

    public Map<Integer, Integer> getParent() {
        return parent;
    }


    public Map<Integer, Set<Integer>> getChildren() {
        return children;
    }
}
