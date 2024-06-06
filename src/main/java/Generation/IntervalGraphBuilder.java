package Generation;

import org.graph4j.Graph;
import org.graph4j.GraphBuilder;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IntervalGraphBuilder {
    public static Graph build(int size)
    {
        // The resulting graph will have vertices start from 1 and end to size
        int[] vertexList = new int[size * 2];
        for(int i=0; i<vertexList.length; i++)
        {
            vertexList[i] = (i/2);
        }

        //Shuffle the vertexList
        Random random = new Random();
        for(int i=0;i<vertexList.length;i++)
        {
            int randomIndex = random.nextInt(0, size);
            int aux = vertexList[i];
            vertexList[i] = vertexList[randomIndex];
            vertexList[randomIndex] = aux;
        }

        Graph intervalGraph = GraphBuilder.empty().buildGraph();
        for(int i=0;i<size;i++)
        {
            intervalGraph.addVertex(i);
        }

        Set<Integer> previousVertices = new HashSet<>();
        for(var currentVertex : vertexList)
        {
            if(!previousVertices.contains(currentVertex))
            {
                for(var neighbour : previousVertices)
                {
                    intervalGraph.addEdge(currentVertex, (int)neighbour);
                }
                previousVertices.add(currentVertex);
            }
            else
            {
                previousVertices.remove(currentVertex);
            }
        }

        return intervalGraph;
    }
}
