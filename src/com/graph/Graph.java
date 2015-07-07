package com.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * data structure for storing the graph
 * 
 * @author Jeff
 * 
 */
public class Graph {

	// edge store in a map
	// (main map)key is nodeID(begin node), value is a sub map
	// sub map: key is edgeID, value is a string: nodeID(end node) weight type
	Map<String, Map<Integer, String>> edges = new HashMap<String, Map<Integer, String>>();

	// node store in a map
	// key is nodeID, value is prior
	Map<String, Double> nodes = new HashMap<String, Double>();

	// edge type store in a map
	// key is typeID, value is true/false(boolean)
	Map<Integer, Boolean> types = new HashMap<Integer, Boolean>();

	/**
	 * add nodes to graph
	 * 
	 * @param ID
	 * @param prior
	 */
	public void addNode(String ID, double prior) {
		nodes.put(ID, prior);
	}

	/**
	 * add edges to graph
	 * 
	 * @param bID
	 * @param eID
	 * @param weight
	 * @param type
	 */
	public void addEdge(String bID, int edgeID, String eID, double weight,
			int type) {
		if (edges.containsKey(bID)) {
			Map<Integer, String> subMap = edges.get(bID);
			subMap.put(edgeID, eID + " " + weight + " " + type);
			edges.put(bID, subMap);
		} else {
			Map<Integer, String> subMap = new HashMap<Integer, String>();
			subMap.put(edgeID, eID + " " + weight + " " + type);
			edges.put(bID, subMap);
		}
		types.put(type, true);
	}

	public Map<String, Map<Integer, String>> getEdges() {
		return edges;
	}

	public void setEdges(Map<String, Map<Integer, String>> edges) {
		this.edges = edges;
	}

	public Map<String, Double> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, Double> nodes) {
		this.nodes = nodes;
	}
	
	public Map<Integer, Boolean> getTypes() {
		return types;
	}

	public void setTypes(Map<Integer, Boolean> types) {
		this.types = types;
	}

}
