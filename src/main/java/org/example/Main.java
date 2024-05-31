package org.example;

import Recognition.IntervalGraphRecognition;
import Utils.ChordalityTest;
import Utils.LexicographicBFS;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;

public class Main {
    public static void main(String[] args) {

        Main main = new Main();

        main.test1();
        main.test2();
        main.test3();
    }

    public void test1()
    {
        // Test 1: Does not work! The graph is not AT-free (vertices 1,3,0 form an asteroid graph)
        Graph graph = GraphBuilder.empty().buildGraph();
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

        IntervalGraphRecognition intervalGraphRecognition = new IntervalGraphRecognition(graph);

        System.out.println("Test 1: " + intervalGraphRecognition.check());
    }
    private void test2()
    {
        // Test 2: The graph is interval. It has a chord (edge 0-2) and it is AT-free
        Graph graph = GraphBuilder.empty().buildGraph();
        graph.addVertices(0,1,2,3);
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,0);
        graph.addEdge(0,2);

        IntervalGraphRecognition intervalGraphRecognition = new IntervalGraphRecognition(graph);

        System.out.println("Test 2: " + intervalGraphRecognition.check());
    }

    private void test3()
    {
        // Test 3: The graph is interval. It has a chorales and it doesn't have AT graphs
        Graph graph = GraphBuilder.empty().buildGraph();
        graph.addVertices(0,1,2,3,4,5,6);
        graph.addEdge(0,1);
        graph.addEdge(0,2);
        graph.addEdge(0,3);

        graph.addEdge(1,2);

        graph.addEdge(2,3);
        graph.addEdge(2,4);
        graph.addEdge(2,5);

        graph.addEdge(3,4);
        graph.addEdge(3,5);

        graph.addEdge(5,6);

        IntervalGraphRecognition intervalGraphRecognition = new IntervalGraphRecognition(graph);

        System.out.println("Test 3: " + intervalGraphRecognition.check());
    }
}