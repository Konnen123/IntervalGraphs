package org.example;

import Utils.LexicographicBFS;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;

public class Main {
    public static void main(String[] args) {
        Graph graph = GraphBuilder.empty().buildGraph();
        graph.addVertices(1,2,3,4,5,6,7,8);
        graph.addEdge(1, 6);

        graph.addEdge(2, 8);

        graph.addEdge(3, 6);
        graph.addEdge(3, 8);

        graph.addEdge(4, 7);
        graph.addEdge(4, 8);

        graph.addEdge(5, 8);
        graph.addEdge(5, 7);

        graph.addEdge(6, 7);
        graph.addEdge(6, 8);

        graph.addEdge(7, 8);

        LexicographicBFS ddLexicographicBFS = new LexicographicBFS(graph);

        var orderedList = ddLexicographicBFS.getOrderedVertexList();

        for(var vertex : orderedList)
            System.out.println(vertex);
    }
}