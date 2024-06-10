# IntervalGraphs

A Java project for recognizing, generating and other algorithms for interval graphs.

## Documentation
In this project, there are 3 main algorithms for interval graphs: recognition, generation and determining the maximum clique.

### Recognition
Interval graphs are a class of graphs that can be constructed from a set of intervals on the real line, where each interval represents a vertex, and there is an edge between two vertices if and only if their corresponding intervals overlap. Recognizing interval graphs involves determining whether a given graph can be represented in this way.

#### Properties of an interval graph
An interval graph is a graph that is chordal, and it is AT-free.

Chordal graphs: Interval graphs are a subset of chordal graphs, which means they have no induced cycles of length greater than three.

AT-free graphs: An asteroidal triple is a set of three vertices such that there is a path between any pair of them that avoids the neighborhood of the third.

The recognition of interval graphs can be efficiently performed using the following steps:

1. Performing a lexicographical Breadth-First Search.
2. Chordality test
3. Order the maximal cliques.
4. Checking if the cliques are in order.

The algorithm has a time complexity of O(n + m)
#### 1. Lexicographical Breadth-First Search
The sequence in which vertices need to be visited is not entirely specified by the standard breadth-first search method. Further restrictions are imposed by Lex-BFS. This ensures that certain desirable qualities are present in the order in which vertices are visited.
We refer to the sequence in which the vertices are visited as Lex-BFS ordering. We will need this sequence to check if the graph is chordal.

Approach:
```java
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
```
#### 2. Chordality test
A graph G is chordal if and only if the ordering of vertices produced by Lex-BFS is a perfect elimination ordering.

First, for each vertex x, compute the right neighbours of x and set the parent of vertex x to be the first value of right neighbours list. Also, we will create the set of right neighbours without the parent for vertex x.
If the graph is chordal, right neighbours without parent should be a subset of right neighbours, for each vertex x.
```java
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
}
```
#### 3. Order the maximal cliques
First, to order the maximal cliques, we need to get the cliques from the graph.

A given graph's maximal cliques can be located and arranged using the GenerateCliques class. It generates these cliques based on a given vertex order and adjacency information, then arranges them to find a certain maximal clique order. This sorted list of cliques can be used to identify whether a graph is an interval graph.

```java
public class GenerateCliques {
    private final Graph graph;
    private int[] orderedVertexList;
    private Map<Integer, Set<Integer>> rightNeighbours;
    private Map<Integer, Set<Integer>> children;
    private Map<Integer, Integer> parent;

    private Set<Integer> pivots = new HashSet<>();
    private List<Integer> orderedCliques = new LinkedList<>();
    private Map<Integer, Set<Integer>> clique = new HashMap<>();


    public GenerateCliques(Graph graph, int[] orderedVertexList, Map<Integer, Set<Integer>> rightNeighbours, Map<Integer, Set<Integer>> children, Map<Integer, Integer> parent) {
        this.graph = graph;
        this.orderedVertexList = orderedVertexList;
        this.rightNeighbours = rightNeighbours;
        this.children = children;
        this.parent = parent;
    }

    public void generate() {
        Set<Integer> childrenRoot = children.get(orderedVertexList[orderedVertexList.length - 1]);
        Set<Integer> cliques = new HashSet<>();
        clique.put(orderedVertexList[orderedVertexList.length - 1], new HashSet<>());
        clique.get(orderedVertexList[orderedVertexList.length - 1]).add(orderedVertexList[orderedVertexList.length - 1]);
        generateCliques(childrenRoot, clique, cliques);

        for (var currentClique : cliques) {
            pivots.addAll(clique.get(currentClique));
        }

        Stack<List<Integer>> partitions = new Stack<>();
        partitions.add(new LinkedList<>(cliques));

        while (!partitions.isEmpty()) {
            Set<Integer> inPartition = new HashSet<>();
            Set<Integer> outPartition = new HashSet<>();

            List<Integer> lastPartition = partitions.pop();

            orderedCliques.add(0, lastPartition.remove(0));
            List<Integer> pivotClique = new ArrayList<>(clique.get(orderedCliques.get(0)));

            for (var currentPartition : lastPartition) {
                if (!Collections.disjoint(pivotClique, clique.get(currentPartition))) {
                    inPartition.add(currentPartition);
                } else {
                    outPartition.add(currentPartition);
                }
            }
            if (!outPartition.isEmpty()) {
                partitions.add(new LinkedList<>(outPartition));
            }
            if (!inPartition.isEmpty()) {
                partitions.add(new LinkedList<>(inPartition));
            }
        }
    }

    private void generateCliques(Set<Integer> childrenNodes, Map<Integer, Set<Integer>> clique, Set<Integer> cliques) {
        for (int vertex : childrenNodes) {
            clique.put(vertex, new HashSet<>());
            for (var neighbour : rightNeighbours.get(vertex)) {
                clique.get(vertex).add(neighbour); // Add elements from rightNeighbours to clique
            }
            clique.get(vertex).add(vertex);

            int currentParent = parent.get(vertex);

            boolean isSubset = true;
            for (var parentClique : clique.get(currentParent)) {
                if (!clique.get(vertex).contains(parentClique)) {
                    isSubset = false;
                    break;
                }
            }
            if (isSubset) {
                cliques.remove(currentParent);
            }
            cliques.add(vertex);
            if (!children.get(vertex).isEmpty()) {
                generateCliques(children.get(vertex), clique, cliques);
            }
        }
    }
}
```

#### 4. Check if maximal cliques are in order
Using the ordered cliques given from the step above, check if the cliques are in ordered. If they are not, it means that the graph is not interval.
```java
public class IntervalGraphRecognition {
    private boolean checkConsecutiveCliques(Map<Integer, Set<Integer>> cliques, List<Integer> orderedCliques, Set<Integer> pivots) {
        for (var currentPivot : pivots) {
            int p = 0;
            int count = 0;
            for (var currentOrderedClique : orderedCliques) {
                // If the currentPivot is in the maximal clique
                if (cliques.get(currentOrderedClique).contains(currentPivot)) {
                    if (p == 0) {
                        p = count + 1;
                    } else {
                        p++;
                        if (p != count + 1) {
                            System.out.println("Error! Graph has not consecutive cliques!");
                            return false;
                        }
                    }

                }

                count++;
            }
        }
        return true;
    }
}
```

#### Interval graph recognition code
```java
public class IntervalGraphRecognition {
    //A graph is an interval graph if and only if it is chordal and AT-free
    /*Algorithm to recognize an interval graph:
    1. Create a lexicographicBFS and get the order of the vertices
    2. Using step 1, if graph is not chordal, reject
    3. Compute the maximal cliques and the clique tree
    4. For each vertex x, if cliques containing x are not in a consecutive order, graph is not interval
    5. Else, graph is interval
    */

    private final Graph graph;
    public IntervalGraphRecognition(Graph graph)
    {
        this.graph = graph;
    }

    public boolean check()
    {
        int[] orderedList = (new LexicographicBFS(graph)).getOrderedVertexList();

        ChordalityTest chordalityTest = new ChordalityTest(graph, orderedList);
        boolean isGraphChordal = chordalityTest.checkChordality();

        if(!isGraphChordal)
        {
            System.out.println("Error! Graph is not chordal");
            return false;
        }

        Map<Integer, Set<Integer>> rightNeighbours = chordalityTest.getRightNeighbours();
        Map<Integer, Integer> parent = chordalityTest.getParent();
        Map<Integer, Set<Integer>> children = chordalityTest.getChildren();

        GenerateCliques generateCliques = new GenerateCliques(graph, orderedList, rightNeighbours, children, parent);
        generateCliques.generate();

        Map<Integer, Set<Integer>> cliques = generateCliques.getClique();

        List<Integer> orderedCliques = generateCliques.getOrderedCliques();
        Set<Integer> pivots = generateCliques.getPivots();

        return checkConsecutiveCliques(cliques, orderedCliques, pivots);

    }
    private boolean checkConsecutiveCliques(Map<Integer, Set<Integer>> cliques, List<Integer> orderedCliques, Set<Integer> pivots)
    {
        for(var currentPivot : pivots)
        {
            int p = 0;
            int count = 0;
            for(var currentOrderedClique : orderedCliques)
            {
                // If the currentPivot is in the maximal clique
                if(cliques.get(currentOrderedClique).contains(currentPivot))
                {
                    if(p == 0)
                    {
                        p = count + 1;
                    }
                    else
                    {
                        p++;
                        if(p != count + 1)
                        {
                            System.out.println("Error! Graph has not consecutive cliques!");
                            return false;
                        }
                    }

                }

                count++;
            }
        }
        return true;
    }


}
```

### Generating
Generating an interval graph is pretty simple.

First, create an array with indices from 1 to n (ex: [1,1,2,2,3,3,....,(n-1),(n-1),(n),(n)])

Then shuffle the array and check if for each node if it intersects with other vertices. (ex: 1 2 2 1). If they intersect, an edge will be created between these 2 vertices.

```java
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
```

### Find maximal clique
For finding a maximal clique in an interval graph, we can use the order of the lexicographic breadth-first search and get the cliques of the graph. Then find the maximal clique.
```java
public class IntervalGraphMaximalClique {
    public static Set<Integer> findMaximalClique(Graph graph)
    {
        LexicographicBFS lexicographicBFS = new LexicographicBFS(graph);
        int[] lexicographicalOrder = lexicographicBFS.getOrderedVertexList();

        ChordalityTest chordalityTest = new ChordalityTest(graph, lexicographicalOrder);
        boolean isGraphChordal = chordalityTest.checkChordality();

        if(!isGraphChordal)
        {
            System.out.println("Error! Graph is not chordal, cannot check for maximal clique");
            return new HashSet<>();
        }

        Map<Integer, Set<Integer>> rightNeighbours = chordalityTest.getRightNeighbours();
        Map<Integer, Integer> parent = chordalityTest.getParent();
        Map<Integer, Set<Integer>> children = chordalityTest.getChildren();

        GenerateCliques generateCliques = new GenerateCliques(graph, lexicographicalOrder, rightNeighbours, children, parent);
        generateCliques.generate();

        Map<Integer, Set<Integer>> cliques = generateCliques.getClique();

        int size = 0;
        int key = -1;
        for(var cliquePair : cliques.entrySet())
        {
            var currentClique = cliquePair.getValue();
            if(currentClique.size() > size)
            {
                size = currentClique.size();
                key = cliquePair.getKey();
            }
        }
        if(key == -1)
            return new HashSet<>();

        return cliques.get(key);
    }
}

```
## References
[1] Graph4J: https://arxiv.org/abs/2308.09920

[2] Lex-BFS and partition refinement, with applications to transitive orientation, interval graph recognition and consecutive ones testing: https://www.sciencedirect.com/science/article/pii/S0304397597002417?ref=pdf_download&fr=RR-2&rr=88bc94cc5b0870cb