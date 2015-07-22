package com.pagerank;   
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import com.common.HeapSort;
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
	// iteration number (maximum)
	int maxIteration = 200;
	// iteration number(minimum)
	int minIteration = 5;
	// threshold
	double threshold = 0.000001;
	
	// top ranking number(for m-step)
	int topNum = 5;
	
	// output path
	String outputPath;
	
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
	        //
			prTmp.put(bID, g.getNodes().get(bID));
	        
	        
	        Map<Integer,Double> conMap = new HashMap<Integer,Double>();
	        Map<Integer,Double> conMapTmp = new HashMap<Integer,Double>();
	        
	        for(int t:g.getTypes().keySet()){
	        	conMap.put(t,0.0); // at first, the contribution for each type is zero
	        	conMapTmp.put(t, 1.0/typenum);
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
	 */
	public void iteration(){
	    
		// test if converge
		double convergence=1.0;
		// iterations
		int iterator = 0;
		// output Edge Usefulness
		ouputEdgeUsefulness(outputPath,iterator);
        while(iterator<minIteration||(iterator < maxIteration &&convergence>threshold)){
        	// run em algorithm 
        	
        	// E Step
        	convergence = e_step();
        	
        	// M Step
        	m_step();
            
            iterator++;
            // output Edge Usefulness
        	ouputEdgeUsefulness(outputPath,iterator);
        	
        	//System.out.println(iterator);
        } 
	}
	
	
	/**
	 * e step
	 */
	private double  e_step(){
		// PageRank with Prior
		pageRankWithPrior();
		// normalize
		double convergence = normalization();
		// calculate each edge type contribution to the node
		calEdgeContribution();
		
		return convergence;
		
		
	}
	
	/**
	 * m step
	 */
	private void m_step(){
		//ranking nodes using pagerank value
		List<String> nodeRanking = rankingNodes();
		
		//update edge usefulness
		updateUsefulness(nodeRanking);
	}
	
	
	/**
	 * ranking nodes based on pagerank value
	 * return top n nodes 
	 * @return
	 */
	private List<String> rankingNodes(){
		List<String>nodeRanking = new ArrayList<String>();
		// total node number
		int num = pr.keySet().size();
		String ids[] = new String[num];
		double values[] = new double[num];
		int n = 0;
		for(String id:pr.keySet()){
			ids[n]= id;
			values[n]=pr.get(id);
			n++;
		}
		
		HeapSort hs = new HeapSort();
		hs.heapSort(values, ids);
		for(int i = ids.length-1;i>=0&&i>=ids.length-topNum;i--){
			nodeRanking.add(ids[i]);
		}
		return nodeRanking;
	}
	
	/**
	 * update edge usefulness
	 * @param nodeRanking
	 */
	private void updateUsefulness(List<String> nodeRanking){
		double all_type = 0.0;
		//update edge usefulness
		for(int type:eu.keySet()){
			//for each type
			double type_total = 0.0;
			for(String node:nodeRanking){
				if(g.getFeedbacks().get(node)!=null){
					type_total += g.getFeedbacks().get(node)*ecn.get(node).get(type);
				}
			}
			all_type +=type_total;
			eu.put(type, type_total);
		}
		
		for(int type:eu.keySet()){
			double v = eu.get(type);
			eu.put(type, v/all_type);
		}
	}
	
	
	/**
	 * pagerank with prior for each time step
	 */
	private void pageRankWithPrior(){
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
        	prTmp.put(bID, pr.get(bID));
        	pr.put(bID,tmp);
        }
	}
	
	/**
	 * calculate each edge type contribution to the node
	 */
	private void calEdgeContribution(){
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
            			//ec4end.put(t,alpha*ec4begin.get(t));
            		}
            		
                    double weight = Double.parseDouble(infs[1]);
                    int edgeType = Integer.parseInt(infs[2]);
                    double tmp = ec4end.get(edgeType);
                    ec4end.put(edgeType, tmp+(1-alpha)*pr.get(bID)*weight);
                    //ec4end.put(edgeType, (1-alpha)*pr.get(bID)*weight);
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
	private double normalization(){
		double convergence = 0.0;
		double sum = 0.0;
		for(String id:pr.keySet()){
			sum += pr.get(id);
        }
		for(String id:pr.keySet()){
			double tmp = pr.get(id);
			double finalv= tmp/sum;
			convergence += Math.abs(finalv-prTmp.get(id));
			pr.put(id, finalv);
			prTmp.put(id, finalv);
        }
		return convergence;
	}
	
	
	
	/**
	 * output all the edge usefulness trend
	 * @param outputPath
	 * @param iteration
	 */
	private void ouputEdgeUsefulness(String outputPath,int iteration){
		try {
			FileWriter fw;
			fw = new FileWriter(outputPath + File.separator + "type_usefullness.csv", true);
			PrintWriter pw = new PrintWriter(fw);
			pw.print(iteration);
			for(int i =1;i<=eu.keySet().size();i++){
				pw.print(","+eu.get(i));
			}
			pw.println();
			fw.close();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public double getAlpha() {
		return alpha;
	}



	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}



	
	
	public double getDamping() {
		return damping;
	}


	public void setDamping(double damping) {
		this.damping = damping;
	}


	public int getMaxIteration() {
		return maxIteration;
	}


	public void setMaxIteration(int maxIteration) {
		this.maxIteration = maxIteration;
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


	public int getTopNum() {
		return topNum;
	}


	public void setTopNum(int topNum) {
		this.topNum = topNum;
	}


	public String getOutputPath() {
		return outputPath;
	}


	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	
}