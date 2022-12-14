/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import javafx.util.Pair;
import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	//TODO: Add your member variables here in WEEK 3
	private int numVertices;
	private int numEdges;
	private Map<GeographicPoint, HashSet<MapEdge>> adjListsMap;
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		// TODO: Implement in this constructor in WEEK 3
		numVertices = 0;
		numEdges = 0;
		adjListsMap = new HashMap<GeographicPoint, HashSet<MapEdge>>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		//TODO: Implement this method in WEEK 3
		return numVertices;
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		//TODO: Implement this method in WEEK 3
		return adjListsMap.keySet();
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		//TODO: Implement this method in WEEK 3
		return numEdges;
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		// TODO: Implement this method in WEEK 3
		if (location == null || adjListsMap.containsKey(location)) { return false; }
		
		HashSet<MapEdge> edges = new HashSet<MapEdge>();
		adjListsMap.put(location,  edges);
		numVertices ++;
		return true;
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		//TODO: Implement this method in WEEK 3
		if (!adjListsMap.containsKey(from) || !adjListsMap.containsKey(to) 
				|| length < 0) {
			throw new IllegalArgumentException();
		}
		MapEdge edge = new MapEdge(from, to, roadName, roadType, length);
		if (!(adjListsMap.get(from)).contains(edge)) {
			(adjListsMap.get(from)).add(edge);
			numEdges ++;
		}
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3
		if (start == null || goal == null) {
			return null;
		}
		
		HashMap<GeographicPoint, GeographicPoint> parentMap = 
				new HashMap<GeographicPoint, GeographicPoint>();
		boolean found = bfsSearch(start, goal, parentMap);
		
		if (!found) {
			return null;
		}
		// goal found, reconstruct path
		return constructPath(start, goal, parentMap);
		
		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
	}
	
	// Helper method for bfs
	private boolean bfsSearch(GeographicPoint start, GeographicPoint goal, 
			HashMap<GeographicPoint, GeographicPoint> parentMap) {
		
		HashSet<GeographicPoint> visited = new HashSet<GeographicPoint>();
		Queue<GeographicPoint> toExplore = new LinkedList<GeographicPoint>();
		visited.add(start);
		toExplore.add(start);
		boolean found = false;
		
		while (!toExplore.isEmpty()) {
			GeographicPoint curr = toExplore.remove();
			if (curr.equals(goal)) {
				found = true;
				break;
			}
			
			for (MapEdge edge : adjListsMap.get(curr)) {
				GeographicPoint nei = edge.getEnd();
				
				if (!visited.contains(nei)) {
					visited.add(nei);
					parentMap.put(nei, curr);
					toExplore.add(nei);
					
				}
			}
			
		}
		return found;
	}
	
	// Helper method to reconstruct path from a given predecessor map
	private List<GeographicPoint> constructPath(GeographicPoint start, GeographicPoint goal, 
			HashMap<GeographicPoint, GeographicPoint> parentMap) {
		
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>();
		GeographicPoint curr = goal;
		
		while (curr != start) {
			path.addFirst(curr); // addFirst adds to beginning of linked list
			curr = parentMap.get(curr);
		}
		path.addFirst(start);
		return path;
	}
	

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 4
		if (start == null || goal == null) {
			return null;
		}
		
		HashMap<GeographicPoint, GeographicPoint> parentMap = 
				new HashMap<GeographicPoint, GeographicPoint>();
		
		boolean useHeuristics = false;
		boolean found = dijkstraSearch(start, goal, parentMap, useHeuristics);
		
		if (!found) {
			return null;
		}
		// goal found, reconstruct path
		return constructPath(start, goal, parentMap);
		

		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
	}
	
	// Helper method for Dijkstra's algorithm
	private boolean dijkstraSearch(GeographicPoint start, GeographicPoint goal, 
			HashMap<GeographicPoint, GeographicPoint> parentMap,
			boolean useHeuristics) {
		
		// Initialize distance table
		HashMap<GeographicPoint, Double> distances = new HashMap<GeographicPoint, Double>();
		for (GeographicPoint v : adjListsMap.keySet()) {
			distances.put(v, Double.POSITIVE_INFINITY);
		}
		// Initialize priority queue
		PriorityQueue<Pair<GeographicPoint, Double>> pq = 
				new PriorityQueue<Pair<GeographicPoint, Double>>(new comparator()); 
		
		HashSet<GeographicPoint> visited = new HashSet<GeographicPoint>();
		distances.replace(start, Double.valueOf(0));
		pq.add(new Pair<>(start, Double.valueOf(0)));
		
		String visitOrder = "";
		
		while (!pq.isEmpty()) {
			GeographicPoint curr = (pq.remove()).getKey();
			if (!visited.contains(curr)) {
				visited.add(curr);
				visitOrder += curr + "\n";
				if (curr.equals(goal)) { 
					System.out.println("Nodes visited in this sequence: " + "\n" + visitOrder);
					return true; }
				
				for (MapEdge edge : adjListsMap.get(curr)) {
					GeographicPoint nei = edge.getEnd();
					
					if (!visited.contains(nei)) {
						Double dist = distances.get(curr) + edge.getLength();
						if (dist < distances.get(nei)) {
							parentMap.put(nei, curr); // replace previous predecessor if present
							distances.put(nei, dist); // update distance table
							
							if (!useHeuristics) { pq.add(new Pair<>(nei, dist)); }
							else {
								Double distFromGoal = nei.distance(goal);
								pq.add(new Pair<>(nei, dist + distFromGoal));
							}
						}
					}
				}
			}
		}
		return false;
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 4
		if (start == null || goal == null) {
			return null;
		}
		
		HashMap<GeographicPoint, GeographicPoint> parentMap = 
				new HashMap<GeographicPoint, GeographicPoint>();
		
		boolean useHeuristics = true;
		boolean found = dijkstraSearch(start, goal, parentMap, useHeuristics);
		
		if (!found) {
			return null;
		}
		// goal found, reconstruct path
		return constructPath(start, goal, parentMap);
		
		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
	
	}

	
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// You can use this method for testing. 
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		
//		MapGraph simpleTestMap = new MapGraph();
//		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
//		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
//		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
//		List<GeographicPoint> testbfsOutput = simpleTestMap.bfs(testStart, testEnd);
//		System.out.println(SearchGrader.printBFSList(testbfsOutput));
		
//		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
//		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
//		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
//		System.out.println(DijkstraGrader.printPath(testroute));
//		System.out.println(DijkstraGrader.printPath(testroute2));
		
		
//		MapGraph testMap = new MapGraph();
//		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
//		GeographicPoint testStart = new GeographicPoint(32.869423, -117.220917);
//		GeographicPoint testEnd = new GeographicPoint(32.869255, -117.216927);
//		List<GeographicPoint> testbfsOutput = testMap.bfs(testStart, testEnd);
//		System.out.println(SearchGrader.printBFSList(testbfsOutput));
		
//		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
//		List<GeographicPoint> testroute = testMap.dijkstra(testStart,testEnd);
//		List<GeographicPoint> testroute2 = testMap.aStarSearch(testStart,testEnd);
//		System.out.println(DijkstraGrader.printPath(testroute));
//		System.out.println(DijkstraGrader.printPath(testroute2));
		
		// A slightly more complex test using real data
		GeographicPoint testStart = new GeographicPoint(32.8674388, -117.2190213);
		GeographicPoint testEnd = new GeographicPoint(32.8697828, -117.2244506);
//		List<GeographicPoint> testbfsOutput = testMap.bfs(testStart, testEnd);
//		System.out.println(SearchGrader.printBFSList(testbfsOutput));

//		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
//		List<GeographicPoint> testroute = testMap.dijkstra(testStart,testEnd);
//		List<GeographicPoint> testroute2 = testMap.aStarSearch(testStart,testEnd);
//		System.out.println(DijkstraGrader.printPath(testroute));
//		System.out.println(DijkstraGrader.printPath(testroute2));
		
		
		/* Use this code in Week 3 End of Week Quiz */
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
		
		
		
	}
	
}
