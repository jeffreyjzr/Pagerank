package com.data;

import com.data.interfaces.IDataLoader;

/**
 * implementation of IDataLoader
 * there could be more than one data load method
 * @author Jeff
 *
 */
public class DataLoaderImpl implements IDataLoader{
	
	
	/**
	 * load nodes from file (with prior)
	 * file format:
	 * ID1 prior
	 * ID2 prior
	 * ID3 prior
	 * ... 
	 * @param filePath
	 */
	public void loadNodesWithPrior(String filePath){
		
	}
	
	
	/**
	 * load edges from file (with prior)
	 * file format:
	 * ID1 ID2 prior
	 * ID2 ID3 prior
	 * ID3 ID4 prior
	 * ... 
	 * @param filePath
	 */
	public void loadEdgesWithPrior(String filePath){
		
	}

	

}
