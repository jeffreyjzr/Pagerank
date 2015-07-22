package com.main;


import com.data.DataLoaderImpl;
import com.data.interfaces.IDataLoader;
import com.graph.Graph;
import com.pagerank.EMPageRankWithPrior;

public class example {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long sTime, eTime;
		sTime = System.currentTimeMillis();
		if (args.length != 0) {
			System.out.println("begin...");
			String path = args[0];
			
			// create a Graph
			Graph g = new Graph();
			
			// data loader
			IDataLoader dataLoader = new DataLoaderImpl();
			
			//load nodes
			dataLoader.loadNodesWithPrior(path, g);
			
			//load edges
			dataLoader.loadEdgesWithPrior(path, g);
			
			//load feedback
			dataLoader.loadNodeRelevantBasedonFeedback(path, g);
			
			//create a pagranker
			EMPageRankWithPrior emPageRanker = new EMPageRankWithPrior(g);
			
			//set output_path
			emPageRanker.setOutputPath(path);
			
			//run iterations
			emPageRanker.iteration();
			

		} else {
			System.out.println("please input parameter!");
		}
		eTime = System.currentTimeMillis();

		System.out.println("Total TIME (sec): " + (eTime - sTime) / 1000.0);
		
		

	}

}
