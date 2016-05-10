package org.eclipse.topology.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 * This Combination class is used to generate combination. The scenario is as following: 
 * There are n boxes(number of n is dynamically by users' input), each boxes contains m elements with generic type T(number of m is also dynamic)
 * Now each time taking one element from each box, how many combinations are there totally? 
 *
 * @author Hao
 *
 */

public class Combination <T> {
    

	
	private List<List<T>> combinationsResults = new ArrayList<List<T>>();
	
	private List<List<T>> Boxes = null;
	
	private Stack <List<T>> combinationResultStack = new Stack<List<T>>();
	


	
	public Combination(List<List<T>> inputBoxes){
		
		this.Boxes = new ArrayList<List<T>>(inputBoxes);
	}
	
	public List<List<T>> getCombinationsResults() {
		List<T> oneCombinationResult = new ArrayList<T>();
		generateCombinations(Boxes,oneCombinationResult);
		return combinationsResults;
	}

	private void generateCombinations(List<List<T>> Boxes, List<T> oneCombinationResult) {
        List<T> ElementlistOfOneBox = Boxes.get(0);
        List<T> tempResultForStack = new ArrayList<T>(oneCombinationResult);
        combinationResultStack.push(tempResultForStack);
        
        for(T oneElement : ElementlistOfOneBox) {
            List<List<T>> newBoxes = new ArrayList<List<T>>(Boxes);
            newBoxes.remove(ElementlistOfOneBox);

            if(Boxes.size() > 1) {
            	oneCombinationResult.add(oneElement);
               
            	generateCombinations(newBoxes, oneCombinationResult);
       		    oneCombinationResult.clear();
       		    List<T> restultFromStack =  (List<T>) combinationResultStack.pop();
       		 for(T oneData: restultFromStack){
       			 oneCombinationResult.add(oneData);	
       			}
       		    oneCombinationResult.remove((oneCombinationResult.size()-1));
             } else {
            	 oneCombinationResult.add(oneElement);
            	 List<T> tempResult = new ArrayList<T>(oneCombinationResult);
            	 combinationsResults.add(tempResult);
            	 T lastElement = ElementlistOfOneBox.get(ElementlistOfOneBox.size()-1);
            	 if(oneElement.equals(lastElement)){

            	 }
            	 else{
            		 oneCombinationResult.remove(oneElement);
            	 }
             }
        }
    }
	

}
