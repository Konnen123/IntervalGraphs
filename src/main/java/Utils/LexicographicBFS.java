package Utils;

import org.graph4j.Graph;

import java.util.*;

//Lex-BFS ordering
public class LexicographicBFS {
    private final Graph graph;

    public LexicographicBFS(Graph graph)
    {
        this.graph = graph;
    }

    // Return the ordered vertex list using LexBFS Algorithm
    // The list returned is in descending order, meaning the last element is the start, and the first element is the finish.
    public int[] getOrderedVertexList() {
        List<Integer> vertices = new LinkedList<>();
        // Keep track of vertices that need to be visited by storing lists of vertices
        List<List<Integer>> listOfVertices = new LinkedList<>();
        for(var vertex : graph.vertices())
        {
            vertices.add(vertex);
        }
        // Add the vertices in the list
        listOfVertices.add(vertices);

        int[] lexicographicalOrderVertices = new int[graph.numVertices()];
        int counter = graph.numVertices() - 1;

        while (!listOfVertices.isEmpty())
        {
            // Get the first list
            List<Integer> firstList = listOfVertices.get(0);

            // Get the first vertex from the list, and then remove it
            Integer vertex = firstList.remove(0);
            if(firstList.isEmpty())
                listOfVertices.remove(0);

            // Add the vertex in the order list
            lexicographicalOrderVertices[counter--] = vertex;

            // A list that stores the neighbours of a vertex
            List<Integer> neighboursList = new LinkedList<>();
            // A temporary queue used for listOfVertices
            Queue<List<Integer>> queueListOfVertices = new LinkedList<>();

            while (!listOfVertices.isEmpty())
            {
                // Get the front list and then remove it
                List<Integer> frontList = listOfVertices.remove(0);
                // A temp stack that will store vertexes that are not neighbours to the current vertex
                Stack<Integer> tempStack = new Stack<>();
                while (!frontList.isEmpty())
                {
                    // Get the first vertex and then remove it
                    int frontVertex = frontList.remove(0);

                    if(graph.containsEdge(vertex, frontVertex))
                    {
                        // If it contains an edge, add it to the list
                        neighboursList.add(frontVertex);
                    }
                    else
                    {
                        // Add it to the stack if it doesn't have an edge
                        tempStack.add(frontVertex);
                    }
                }
                while (!tempStack.isEmpty())
                {
                    // Add the vertices that are not neighbour with the current vertex to the front list
                    frontList.add(tempStack.pop());
                }
                if(!frontList.isEmpty())
                {
                    queueListOfVertices.add(frontList);
                }
            }
            if(!neighboursList.isEmpty())
            {
                // Add the neighbours to the list
                listOfVertices.add(neighboursList);
            }

            while (!queueListOfVertices.isEmpty())
            {
                // Add the vertices in a new list, that are not neighbour to the vertex
                listOfVertices.add(queueListOfVertices.remove());
            }
        }
        return lexicographicalOrderVertices;
    }
}
