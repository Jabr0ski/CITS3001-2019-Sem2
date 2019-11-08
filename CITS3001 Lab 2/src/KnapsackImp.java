import java.lang.Math;
import java.util.Arrays;

public class KnapsackImp implements Knapsack {
	
	/**
	 * Match class used to keep index of ratios with their corresponding
	 * weight/value after being sorted in ascending ordered
	 * @author Serhat
	 *
	 */
	private class Match implements Comparable<Match>
	{
		int index;
		double ratio;

		public Match(int i, double v, double w) {
			this.index = i;
			this.ratio = (v/w);
		}
		public int compareTo(Match m)
		{
			if(this.ratio == m.ratio)
				return 0;
			else if(this.ratio > m.ratio)
				return 1;
			return -1;
		}

	}
	
	public int fractionalKnapsack(int[] weights, int[] values, int capacity) {
		
		int arrLen = values.length;
		
		Match ratios[] = new Match[arrLen];
		
		//i provides a unique index/id for each ratio so that it can still be
		//found after sorting the array
		for(int i = 0; i < arrLen; i++)
		{
			ratios[i] = new Match(i, values[i], weights[i]);	
		}
		
		//quicksorts the ratios with a complexity of O(n log n)
		Arrays.sort(ratios); 
		
		//keeps track of current value in knapsack
		double currV = 0;
		//keeps track of remaining capacity in knapsack
		double remC = capacity;
		
		//checks if current best ratio fits in knapsack, if so
		//add the value, else add the portion that fits and break
		for(int i = arrLen-1; i >= 0; i--)
		{
			if((double)weights[ratios[i].index] > remC) 
			{
				currV+= ratios[i].ratio*remC;
				break;
			}
			else
			{
				currV += (double)values[ratios[i].index];
				remC -= weights[ratios[i].index];
			}
		}
		return (int) currV;
	}

	public int discreteKnapsack(int[] weights, int[] values, int capacity) {

		//plus 1 for the first column and row filled with zeroes
		int[][] maxArr = new int[values.length + 1][capacity + 1];
		
		//build the table maxArr[][] such that the bottom right
		//element contains the max possible value
		for(int i = 0; i <= values.length; i++) {
			for(int j = 0; j <= capacity; j++) {
				if(i == 0 || j == 0) {
					maxArr[i][j] = 0;
				}
				else if(weights[i - 1] <= j) {
					
					maxArr[i][j] = Math.max(values[i - 1] + maxArr[i - 1][j - weights[i - 1]], maxArr[i - 1][j]);
					
				} else {
					maxArr[i][j] = maxArr[i-1][j];
				}
			}
		}
		return maxArr[values.length][capacity];
	}

}
