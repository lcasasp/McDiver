package diver;

import datastructures.CoinTree;
import datastructures.PQueue;
import datastructures.SlowPQueue;
import game.*;
import graph.ShortestPaths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/** This is the place for your implementation of the {@code SewerDiver}.
 */
public class McDiver implements SewerDiver {

    /** See {@code SewerDriver} for specification. */
    @Override
    public void seek(SeekState state) {
        seek2(state);
    }
    List<Long> visited = new LinkedList<>();

    /**
     * first implementation of seek(), however, inferior to seek2(). See seek2 specifications.
     * @param state
     */
    public void seek1(SeekState state) {
        List<Long> visited = new LinkedList<>();
        Long u = state.currentLocation();
        visited.add(u);
        //DFS walk implementation
        for (NodeStatus neighbor : state.neighbors()){
            if (!visited.contains(neighbor.getId())) {
                state.moveTo(neighbor.getId());
                if (state.distanceToRing() == 0) {
                    return;
                }
                seek1(state);
                state.moveTo(u);
            }
        }
    }

    /**
     * This helper function for seek() uses a DFS walk, but sorts all neighbors by priority queue
     * to determine which neighbors should be traveled through first, fully exploring their paths
     * and retracing if needed.
     * @param state
     */
    public void seek2(SeekState state) {
        long u = state.currentLocation();
        visited.add(u);
        //Sort neighbors by closest to ring
        List<NodeStatus> neighbors = (List<NodeStatus>) state.neighbors();
        Collections.sort(neighbors);
        //DFS walk implementation
        for (NodeStatus neighbor : neighbors) {
            if (!visited.contains(neighbor.getId()) && state.distanceToRing() != 0) {
                u = state.currentLocation();
                state.moveTo(neighbor.getId());
                visited.add(neighbor.getId());
                seek2(state);
                if (state.distanceToRing() == 0) {
                    return;
                }
                state.moveTo(u);
            }
        }
    }

    /** See {@code SewerDriver} for specification. */
    @Override
    public void scram(ScramState state) {
        scram1(state);
    }

    /**
     * Uses the ScramState state to establish a maze and then searches through all nodes in
     * maze graph to find a path using Dijstras algorithm and a BFS algorithm that uses a ratio
     * of distance and coin value to find a bestPath that maximizes the total ratio value and
     * reaching the exit in the given number of steps.
     * @param state
     */
    public void scram1(ScramState state) {
        Maze maze = new Maze((Set<Node>)state.allNodes());
        ShortestPaths<Node, Edge> shortestPath = new ShortestPaths<>(maze);
        shortestPath.singleSourceDistances(state.currentNode());

        Collection<Node> allNodes = state.allNodes();
        List<Node> coins = new ArrayList<>(allNodes);
        PQueue<Node> queue = new SlowPQueue<>();

        for (Node node : coins) {
            float distance = (pathLength(maze, state.currentNode(), node));
            double ratio = (1/(distance))*node.getTile().coins();
            queue.add(node, -ratio);
        }

        while(!queue.isEmpty()) {
            shortestPath.singleSourceDistances(state.currentNode());
            Node coin = queue.extractMin();
            List<Edge> coinPath = shortestPath.bestPath(coin);

            int tripLength = (pathLength(maze, state.currentNode(), coin));
            int returnLength = pathLength(maze, coin, state.exit());
            if (state.stepsToGo() - tripLength > returnLength) {
                for (Edge edge : coinPath) {
                    state.moveTo(edge.destination());
                    state.currentNode().getTile().takeCoins();
                }
                queue = new SlowPQueue<>();
                for (Node node : coins) {
                    if (node != coin) {
                        float distance = (pathLength(maze, state.currentNode(), node));
                        double ratio = (1/(distance))*node.getTile().coins();
                        queue.add(node, -ratio);
                    }
                }
            }
        }

        shortestPath.singleSourceDistances(state.currentNode());
        List<Edge> fpath = shortestPath.bestPath(state.exit());
        for (Edge edge : fpath) {
            state.moveTo(edge.destination());
            state.currentNode().getTile().takeCoins();
        }
    }
    Map<String, Integer> pathLengthCache = new HashMap<>();

    /**
     * Helper for Scram phase. Takes in the maze, source and destination nodes and iterates
     * through edges in the ShortestPath to calculate the length of path, returning as int.
     * @param maze
     * @param source
     * @param dest
     * @return int
     */
    public int pathLength(Maze maze, Node source, Node dest) {
        String key = source.getId() + ":" + dest.getId();
        if (pathLengthCache.containsKey(key)) {
            return pathLengthCache.get(key);
        }
        int length = 0;
        ShortestPaths<Node, Edge> shortestPath = new ShortestPaths<>(maze);
        shortestPath.singleSourceDistances(source);
        List<Edge> path = shortestPath.bestPath(dest);
        for (Edge edge : path) {
            length += edge.length();
        }
        pathLengthCache.put(key, length);
        return length;
    }

}
