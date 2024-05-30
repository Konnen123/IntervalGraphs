package Utils;

import org.graph4j.Graph;

import java.util.*;

//Lex-BFS ordering
public class LexicographicBFS {
    Graph<Integer, Integer> graph;

    public LexicographicBFS(Graph graph)
    {
        this.graph = graph;
    }

    // Return the ordered vertex list using LexBFS Algorithm
    // The list returned is in descending order, meaning the first element visited will have the highest value, and the last will have the lowest value
    public int[] getOrderedVertexList() {
        List<List<Integer>> listOfCells = new LinkedList<>();
        int[] orderedVertices = new int[graph.numVertices()];

        int i = graph.numVertices();

        // Get an arbitrary order of the vertices
        List<Integer> firstList = new LinkedList<>();
        for (var vertex : graph.vertices()) {
            firstList.add(vertex);
        }
        listOfCells.add(firstList);

        // BFS
        // To achieve lexicographical search, get the first list from the listOfCells, then get the first element from first list, then remove the element
        // Removal is done in O(1) because of linked lists
        while (!listOfCells.isEmpty()) {
            // Get the front list
            List<Integer> frontList = listOfCells.get(0);
            // Get the front element
            int currentVertex = frontList.get(0);
            frontList.remove(0);
            if (frontList.isEmpty()) {
                listOfCells.remove(0);
            }

            // Vertices start from label 1
            orderedVertices[currentVertex - 1] = i--;

            List<List<Integer>> newCells = new LinkedList<>();
            // Iterate through each cell of the list
            for (List<Integer> currentCell : listOfCells) {
                // Keep track of the neighbours of the current node
                List<Integer> neighborsOfCurrentNode = new LinkedList<>();

                for (Iterator<Integer> it = currentCell.iterator(); it.hasNext();) {
                    int cellVertex = it.next();
                    // Check if the vertex from the current cell has an edge with current vertex
                    if (graph.containsEdge(currentVertex, cellVertex)) {
                        // Add it to the list, and then remove it from the list
                        neighborsOfCurrentNode.add(cellVertex);
                        it.remove();
                    }
                }
                // Add the neighbors of the cells to the newCells container if it is not empty.
                if (!neighborsOfCurrentNode.isEmpty()) {
                    newCells.add(neighborsOfCurrentNode);
                }
                // Remove the cell if it is empty
                if(currentCell.isEmpty())
                    listOfCells.remove(currentCell);
            }

            // Iterate through each new cell and add it to the front of our list of cells if it is not empty.
            for (List<Integer> cell : newCells) {
                if(cell.isEmpty())
                    continue;
                listOfCells.add(0, cell); // Add cell to the front of listOfCells
            }
        }
        return orderedVertices;
    }
}
