package com.pagerank;  
import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.File;  
import java.io.FileReader;  
import java.io.FileWriter;  
import java.util.HashMap;
import java.util.Hashtable;  
import java.util.Map;

import com.graph.Graph;

/**
 * PageRank
 * (self loop problem didn't handle)
 * @author Jeff
 *
 */
public class EMPageRankWithPrior {  
    
	// Graph for PageRank
	Graph g;
	// damping factor for pagerank
	double damping = 0.85;
	// iteration number
	int iteration = 200;
	// iteration number(minimum)
	int minIteration = 5;
	// threshold
	double threshold = 0.000001;
	
	// pagerank authority scores
    Map<String,Double>pr;   
    // temp pagerank authority scores  
    Map<String,Double> prTmp;
    
    // edge contribution for node
    Map<String,Map<Integer,Double>>ecn;
    // temp edge contribution for node
    Map<String,Map<Integer,Double>>ecnTmp;
    
    // edge usefulness for a task
    Map<Integer,Double>eu;
    
    
    // alpha parameter(in e step)
    double alpha = 0.5;
	
    // constructor
    public EMPageRankWithPrior(Graph g){
		this.g = g;
		//initialize
		pr = new HashMap<String,Double>();
		prTmp = new HashMap<String,Double>();
		ecn =  new HashMap<String,Map<Integer,Double>>();
		ecnTmp =  new HashMap<String,Map<Integer,Double>>();
		//edge type number
		double typenum = g.getTypes().keySet().size();
		for(String bID:g.getNodes().keySet()){
			pr.put(bID, g.getNodes().get(bID));  
	        prTmp.put(bID, 0.0);
	        
	        
	        Map<Integer,Double> conMap = new HashMap<Integer,Double>();
	        Map<Integer,Double> conMapTmp = new HashMap<Integer,Double>();
	        
	        for(int t:g.getTypes().keySet()){
	        	conMap.put(t,1.0/typenum);
	        	conMapTmp.put(t, 0/typenum);
	        }
	        ecn.put(bID, conMap);
	        ecnTmp.put(bID, conMapTmp);
		}
		
		eu = new HashMap<Integer,Double>();
		for(int t:g.getTypes().keySet()){
			eu.put(t,1.0/typenum);
        }
	}
	
	
	/**
	 * rank method()
	 * @param total
	 */
	public void iteration(int total){
	    
		// test if converge
		double gap=1.0;
		double previous = 0.0;
		double current = 0.0;
		// iterations
		int iterator = 0;
        while(iterator>=minIteration||iterator < 500||gap<threshold){
        	// run em algorithm 
        	
        	// E Step
        	e_step();
        	
        	// M Step
        	m_step();
            //PageRank with prior
        	pageRankWithPrior();
            
            
            
            
            gap = Math.abs(previous-current);
            previous = current;
            iterator++;
        }  
	    
	    
	}
	
	
	
	
	
	
	public void e_step(){
		// PageRank with Prior
		pageRankWithPrior();
		// normalize
		normalization();
		// calculate each edge type contribution to the node
		calEdgeContribution();
		
		
	}
	
	
	public void m_step(){
		//update edge usefulness
		
		
	}
	
	
	/**
	 * pagerank with prior for each time step
	 */
	public void pageRankWithPrior(){
		// PR score of current page  
		double fatherRank = 0.0; 
		// for each node in graph
        for(String bID:g.getNodes().keySet()){
        	// check if this node has any relation edges
        	if(g.getEdges().containsKey(bID)){
        		fatherRank = pr.get(bID); 
                for(String edgeInf:g.getEdges().get(bID).values()){
                	//edgeInf = endNodeID+" "+weight+" "+type
                	// infs[0]:endNodeID
                	// infs[1]:weight
                	// infs[2]:type
                	String infs[] = edgeInf.split("\\s+");
                    double weight = Double.parseDouble(infs[1]);
                    double tmp = prTmp.get(infs[0]);
                    double usefulness = eu.get(Integer.parseInt(infs[2]));
                    prTmp.put(infs[0], tmp+fatherRank*weight*usefulness);
                }
            }
        }
        
        // pagerank with prior
        for(String bID:g.getNodes().keySet()){
        	double tmp =  (1-damping)*prTmp.get(bID)  + damping * g.getNodes().get(bID);
        	pr.put(bID,tmp);
        	prTmp.put(bID, 0.0);
        }
		
	}
	
	/**
	 * calculate each edge type contribution to the node
	 */
	public void calEdgeContribution(){
		// for each node in graph
        for(String bID:g.getNodes().keySet()){
        	// check if this node has any relation edges
        	if(g.getEdges().containsKey(bID)){
        		// edge contribution(begin node)
        		Map<Integer,Double> ec4begin=ecnTmp.get(bID);
                for(String edgeInf:g.getEdges().get(bID).values()){
                	//edgeInf = endNodeID+" "+weight+" "+type
                	// infs[0]:endNodeID
                	// infs[1]:weight
                	// infs[2]:type
                	String infs[] = edgeInf.split("\\s+");
                	// edge contribution(end node)
            		Map<Integer,Double> ec4end=ecn.get(infs[0]);
            		for(int t:ec4end.keySet()){
            			double tmp = ec4end.get(t);
                        ec4end.put(t,tmp+alpha*ec4begin.get(t));
            		}
            		
                    double weight = Double.parseDouble(infs[1]);
                    int edgeType = Integer.parseInt(infs[2]);
                    double tmp = ec4end.get(edgeType);
                    ec4end.put(edgeType, tmp+(1-alpha)*pr.get(bID)*weight);
                    ecn.put(infs[0],ec4end);
                }
            }
        }
        // assign value to tmp and nomalization 
        // for each node in graph
        for(String bID:g.getNodes().keySet()){
        	// check if this node has any relation edges
        	Map<Integer,Double>ec = ecn.get(bID);
        	Map<Integer,Double>ecTmp = ecnTmp.get(bID);
        	double sum =0.0;
        	for(int t:ec.keySet()){
        		sum += ec.get(t);
    		}
        	for(int t:ec.keySet()){
        		double tmp= ec.get(t);
        		ec.put(t,tmp/sum);
    			ecTmp.put(t,tmp/sum);
    		}
        	ecn.put(bID, ec);
        	ecnTmp.put(bID, ecTmp);
        }
		
	}
	
	
	// normalize the node score
	public void normalization(){
		double sum = 0.0;
		for(String id:pr.keySet()){
			sum += pr.get(id);
        }
		for(String id:pr.keySet()){
			double tmp = pr.get(id);
			pr.put(id, tmp/sum);
        }
	}



	public double getAlpha() {
		return alpha;
	}



	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}



	public int getIteration() {
		return iteration;
	}


	public  void setIteration(int iteration) {
		this.iteration = iteration;
	}
	
	public double getThreshold() {
		return threshold;
	}



	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}



	public int getMinIteration() {
		return minIteration;
	}



	public void setMinIteration(int minIteration) {
		this.minIteration = minIteration;
	}
	
	
}