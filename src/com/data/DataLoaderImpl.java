package com.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.data.interfaces.IDataLoader;
import com.graph.Graph;

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
	public void loadNodesWithPrior(String filePath, Graph g){
		try {
			// read 
			BufferedReader bin = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath+File.separator+"node.dat")));
			String readline = "";
			while ((readline = bin.readLine()) != null) {
				String s[] = readline.split("\\s+");
				g.addNode(s[0], Double.parseDouble(s[1]));
			}
			bin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * load edges from file (with prior)
	 * file format:
	 * EDGEID1 NODEID1 NODEID2 WEIGHT TYPE
	 * EDGEID2 NODEID2 NODEID3 WEIGHT TYPE
	 * ... 
	 * @param filePath
	 */
	public void loadEdgesWithPrior(String filePath,Graph g){
		try {
			// read
			BufferedReader bin = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath+File.separator+"edge.dat")));
			String readline = "";
			while ((readline = bin.readLine()) != null) {
				String s[] = readline.split("\\s+");
				g.addEdge(s[1], Integer.parseInt(s[0]), s[2], Double.parseDouble(s[3]), Integer.parseInt(s[4]));
			}
			bin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * load node relevant probability based on feedback information
	 * file format:
	 * ID1 feedback
	 * ID2 feedback
	 * ID3 feedback
	 * @param filePath
	 */
    public void loadNodeRelevantBasedonFeedback(String filePath,Graph g){
    	try {
			// read 
			BufferedReader bin = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath+File.separator+"feedback.dat")));
			String readline = "";
			while ((readline = bin.readLine()) != null) {
				String s[] = readline.split("\\s+");
				g.addNodeFeedback(s[0], Double.parseDouble(s[1]));
			}
			bin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
