package Utils;

import org.graph4j.Graph;

import java.util.*;

public class ChordalityTest {

    private Graph graph;

    private int[] orderedVertexList;

    // For each vertex, store its neighbours that appear on the right side of the LexBFS list.
    private List<List<Integer>> rightNeighbours;
    // Parent of the current vertex is the first right neighbour element
    // Parent object is a map, key - vertex, value - parent
    private Map<Integer, Integer> parent;
    // For each vertex, store the list of right neighbours without its parent.
    private List<List<Integer>> rightNeighboursWithoutParent;
    private List<List<Integer>> children;

    public ChordalityTest(Graph graph, int[] orderedVertexList)
    {
        this.graph = graph;

        LexicographicBFS lexicographicBFS = new LexicographicBFS(graph);
        this.orderedVertexList = orderedVertexList;

        rightNeighbours = new ArrayList<>(graph.numVertices());
        parent = new HashMap<>();
        rightNeighboursWithoutParent = new ArrayList<>(graph.numVertices());
        children = new ArrayList<>();

        initializeContainers();
    }

    public boolean checkChordality() {
        // Iterate through each element from the ordered list, except the last one
        for(int i=0;i<orderedVertexList.length - 1;i++)
        {
           // Get the vertex and the parent
           int vertex = orderedVertexList[i];
           int currentParent = parent.get(vertex);

           for(int j = 0; j < rightNeighboursWithoutParent.get(vertex).size(); j++)
           {

               // Now check if rightNeighbourNoParent list is a sublist of rightNeighbour
               for(var rightNeighbourNoParentElement : rightNeighboursWithoutParent.get(vertex))
               {
                    boolean existsElement = false;
                    for(var rightNeighbourElement : rightNeighbours.get(currentParent))
                    {
                        if(Objects.equals(rightNeighbourElement, rightNeighbourNoParentElement))
                        {
                            existsElement = true;
                            break;
                        }
                    }
                   // If the element cannot be found, it means it is not a sublist, return false
                    if(!existsElement)
                        return false;

               }
           }
        }

        return true;
     }
    private void initializeContainers()
    {
        for(int i=0;i<orderedVertexList.length;i++)
        {
            rightNeighbours.add(new ArrayList<>());
            rightNeighboursWithoutParent.add(new ArrayList<>());
            children.add(new ArrayList<>());
        }
        // Iterate to orderVertexList.length - 1, because the last element doesn't have a parent (it is the start point of the LexBFS algorithm)
        for(int i=0;i<orderedVertexList.length - 1;i++)
        {
            int vertex = orderedVertexList[i];
            for(int j=i+1;j<orderedVertexList.length;j++)
            {
                int jVertex = orderedVertexList[j];
                if(graph.containsEdge(vertex, jVertex))
                {
                    // If an edge exists, we already know that jVertex is on the right side of vertex, so add it to the rightNeighbours
                    rightNeighbours.get(vertex).add(jVertex);
                }
            }
            // Add the parent and children
            parent.put(vertex,rightNeighbours.get(vertex).get(0));
            children.get(rightNeighbours.get(vertex).get(0)).add(vertex);

            for(int k=1;k<rightNeighbours.get(vertex).size();k++)
            {
                // Populate the list without the parent
                rightNeighboursWithoutParent.get(vertex).add(rightNeighbours.get(vertex).get(k));
            }
        }
    }

    public List<List<Integer>> getRightNeighbours() {
        return rightNeighbours;
    }

    public Map<Integer, Integer> getParent() {
        return parent;
    }

    public List<List<Integer>> getRightNeighboursWithoutParent() {
        return rightNeighboursWithoutParent;
    }

    public List<List<Integer>> getChildren() {
        return children;
    }
}
