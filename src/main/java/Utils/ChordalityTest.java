package Utils;

import org.graph4j.Graph;

import java.util.*;

public class ChordalityTest {

    private Graph graph;

    public ChordalityTest(Graph graph)
    {
        this.graph = graph;
    }
    public boolean checkChordality() {

        LexicographicBFS lexicographicBFS = new LexicographicBFS(graph);
        int[] orderedVertexList = lexicographicBFS.getOrderedVertexList();

        // For each vertex, store its neighbours that appear on the right side of the LexBFS list.
        List<List<Integer>> rightNeighbours = new ArrayList<>(graph.numVertices());
        // Parent of the current vertex is the first right neighbour element
        // Parent object is a map, key - vertex, value - parent
        Map<Integer, Integer> parent = new HashMap<>();
        // For each vertex, store the list of right neighbours without its parent.
        List<List<Integer>> rightNeighboursWithoutParent = new ArrayList<>(graph.numVertices());
        List<List<Integer>> children = new ArrayList<>();

        initialize(orderedVertexList, rightNeighbours, parent, rightNeighboursWithoutParent, children);

        for(var p : parent.entrySet())
        {
            System.out.println(p.getKey() + ": " + p.getValue());
        }
        // Chordality test
        for(int i=0;i<orderedVertexList.length - 1;i++)
        {
           // Get the vertex and the parent
           int vertex = orderedVertexList[i];
           int currentParent = parent.get(vertex);

           for(int j = 0; j < rightNeighboursWithoutParent.get(vertex).size(); j++)
           {
               boolean existsElement = false;
               // Now check if rightNeighbourNoParent list is a sublist of rightNeighbour
               for(var rightNeighbourElement : rightNeighbours.get(currentParent))
               {
                    for(var rightNeighbourNoParentElement : rightNeighboursWithoutParent.get(vertex))
                    {
                        if(Objects.equals(rightNeighbourElement, rightNeighbourNoParentElement))
                        {
                            existsElement = true;
                            break;
                        }

                    }
                   if(existsElement)
                       break;
               }
               // If it isn't a sublist, it means the graph isn't chordal, return false
               if(!existsElement)
                   return false;
           }
        }

        return true;
     }
    private void initialize(int[] orderedVertexList, List<List<Integer>> rightNeighbours, Map<Integer, Integer> parent, List<List<Integer>> rightNeighboursWithoutParent, List<List<Integer>> children)
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

}
