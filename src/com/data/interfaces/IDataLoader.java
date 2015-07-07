package com.data.interfaces;

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
	void loadNodesWithPrior(String filePath);
	
	
	/**
	 * load edges from file (with prior)
	 * file format:
	 * ID1 ID2 prior
	 * ID2 ID3 prior
	 * ID3 ID4 prior
	 * ... 
	 * @param filePath
	 */
	void loadEdgesWithPrior(String filePath);

}
