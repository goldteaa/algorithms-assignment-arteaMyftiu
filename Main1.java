import java.util.*;

public class Main1 {

    private static final double INFINITY = Double.MAX_VALUE;

    /**
     * Creates a graph based on user input.
     *
     * @return The graph represented as a map of nodes to a list of edges.
     */
    private static Map<String, List<Edge>> createGraphFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        Map<String, List<Edge>> graph = new HashMap<>();

        System.out.print("Enter the number of nodes in the graph: ");
        int numNodes = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numNodes; i++) {
            System.out.print("Enter the name of node " + (i + 1) + ": ");
            String nodeName = scanner.nextLine();
            System.out.print("Enter the number of edges for node " + nodeName + ": ");
            int numEdges = scanner.nextInt();
            scanner.nextLine();

            List<Edge> edges = new ArrayList<>();
            for (int j = 0; j < numEdges; j++) {
                System.out.print("Enter the name of the destination node for edge " + (j + 1) + ": ");
                String toNode = scanner.nextLine();
                System.out.print("Enter the cost of the edge: ");
                double cost = scanner.nextDouble();
                scanner.nextLine();

                edges.add(new Edge(cost, toNode));
            }

            graph.put(nodeName, edges);
        }

        return graph;
    }

    /**
     * Checks if the graph has any negative edges.
     *
     * @param graph The graph represented as a map of nodes to a list of edges.
     * @return True if the graph has negative edges, False otherwise.
     */
    private static boolean hasNegativeEdges(Map<String, List<Edge>> graph) {
        for (List<Edge> edges : graph.values()) {
            for (Edge edge : edges) {
                if (edge.getCost() < 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds the shortest paths in the graph using either Dijkstra's algorithm or Bellman-Ford algorithm.
     *
     * @param graph The graph represented as a map of nodes to a list of edges.
     * @param start The starting node.
     * @return A map containing the algorithm used and the shortest paths from the start node to all other nodes.
     */
    private static Map<String, Object> dijkstras(Map<String, List<Edge>> graph, String start) {
        if (hasNegativeEdges(graph)) {
            return bellmanFord(graph, start);
        }

        Map<String, Double> shortestPaths = new HashMap<>();
        List<String> unvisited = new ArrayList<>(graph.keySet());

        for (String node : unvisited) {
            shortestPaths.put(node, INFINITY);
        }

        shortestPaths.put(start, 0.0);

        while (!unvisited.isEmpty()) {
            String minNode = null;

            for (String node : unvisited) {
                if (minNode == null) {
                    minNode = node;
                } else if (shortestPaths.get(node) < shortestPaths.get(minNode)) {
                    minNode = node;
                }
            }

            for (Edge edge : graph.get(minNode)) {
                double cost = edge.getCost();
                String toNode = edge.getToNode();

                if (cost + shortestPaths.get(minNode) < shortestPaths.get(toNode)) {
                    shortestPaths.put(toNode, cost + shortestPaths.get(minNode));
                }
            }

            unvisited.remove(minNode);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", hasNegativeEdges(graph) ? "Bellman-Ford" : "Dijkstra's");
        result.put("shortestPaths", shortestPaths);

        return result;
    }

    /**
     * Finds the shortest paths in the graph using the Bellman-Ford algorithm.
     *
     * @param graph The graph represented as a map of nodes to a list of edges.
     * @param start The starting node.
     * @return A map containing the algorithm used and the shortest paths from the start node to all other nodes.
     */
    private static Map<String, Object> bellmanFord(Map<String, List<Edge>> graph, String start) {
        Map<String, Double> shortestPaths = new HashMap<>();
        List<String> nodes = new ArrayList<>(graph.keySet());

        for (String node : nodes) {
            shortestPaths.put(node, INFINITY);
        }

        shortestPaths.put(start, 0.0);

        for (int i = 0; i < nodes.size() - 1; i++) {
            for (String node : nodes) {
                for (Edge edge : graph.get(node)) {
                    double cost = edge.getCost();
                    String toNode = edge.getToNode();

                    if (shortestPaths.get(node) + cost < shortestPaths.get(toNode)) {
                        shortestPaths.put(toNode, shortestPaths.get(node) + cost);
                    }
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("algorithm", "Bellman-Ford");
        result.put("shortestPaths", shortestPaths);

        return result;
    }

    public static void main(String[] args) {
        Map<String, List<Edge>> graph = createGraphFromUserInput();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the starting node: ");
        String start = scanner.nextLine();

        Map<String, Object> result = dijkstras(graph, start);
        String algorithm = (String) result.get("algorithm");
        Map<String, Double> shortestPaths = (Map<String, Double>) result.get("shortestPaths");

        System.out.println("Algorithm used: " + algorithm);
        System.out.println("Shortest path from " + start + ": " + shortestPaths);
    }

    private static class Edge implements Comparable<Edge> {
        private final double cost;
        private final String toNode;

        public Edge(double cost, String toNode) {
            this.cost = cost;
            this.toNode = toNode;
        }

        public double getCost() {
            return cost;
        }

        public String getToNode() {
            return toNode;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.cost, other.cost);
        }
    }
}
