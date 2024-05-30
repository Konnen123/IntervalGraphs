package org.example;

import Utils.ChordalityTest;
import Utils.LexicographicBFS;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;

public class Main {
    public static void main(String[] args) {
        Graph graph = GraphBuilder.empty().buildGraph();
        //graph.addVertices(0, 1, 2, 3, 4, 5, 6, 7);
        graph.addVertices(2,5,0,3,4,7,1,6);
        graph.addEdge(0, 5);

        graph.addEdge(1, 7);

        graph.addEdge(2, 5);
        graph.addEdge(2, 7);

        graph.addEdge(3, 6);
        graph.addEdge(3, 7);

        graph.addEdge(4, 7);
        graph.addEdge(4, 6);

        graph.addEdge(5, 6);
        graph.addEdge(5, 7);

        graph.addEdge(6, 7);

        ChordalityTest chordalityTest = new ChordalityTest(graph);
        System.out.println(chordalityTest.checkChordality());
    }
}