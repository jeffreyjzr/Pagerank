package com.data.interfaces;

import com.graph.Graph;

/**
 * interface for loading data
 * @author Jeff
 *
 */
public interface IDataLoader {
	
	/**
	 * load nodes from file (with prior)
	 * file format:
	 * ID1 prior
	 * ID2 prior
	 * ID3 prior
	 * ... 
	 * @param filePath
	 */
	void loadNodesWithPrior(String filePath,Graph g);
	
	
	/**
	 * load edges from file (with prior)
	 * file format:
	 * ID1 ID2 prior
	 * ID2 ID3 prior
	 * ID3 ID4 prior
	 * ... 
	 * @param filePath
	 */
	void loadEdgesWithPrior(String filePath,Graph g);
	
	/**
	 * load node relevant probability based on feedback information
	 * file format:
	 * ID1 feedback
	 * ID2 feedback
	 * ID3 feedback
	 * @param filePath
	 */
    public void loadNodeRelevantBasedonFeedback(String filePath,Graph g);

}
