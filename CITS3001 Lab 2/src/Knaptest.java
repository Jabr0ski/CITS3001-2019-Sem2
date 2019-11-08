import java.util.Random;

public class Knaptest {

	public static void main(String[] args) {
		int[] weights = new int[20];
		int[] values = new int[20];
		int capacity = 96;
		
		/*weights[0] = 7;
		weights[1] = 4;
		weights[2] = 4;
		
		values[0] = 15;
		values[1] = 8;
		values[2] = 8;*/
		
		weights[0] = 36; values[0] = 28;
		weights[1] = 46; values[1] = 32;
		weights[2] = 18; values[2]= 27;
		weights[3] = 15; values[3]= 19;
		weights[4] = 25; values[4]= 43;
		weights[5] = 12; values[5]= 34;
		weights[6] = 18; values[6]= 24;
		weights[7] = 45; values[7]= 15;
		weights[8] = 3; values[8]= 11;
		weights[9] = 41; values[9]= 2;
		weights[10] = 2; values[10]= 18;
		weights[11] = 3; values[11]= 41;
		weights[12] = 46; values[12]= 46;
		weights[13] = 22; values[13]= 28;
		weights[14] = 28; values[14]= 13;
		weights[15] = 7; values[15]= 19;
		weights[16] = 47; values[16]= 48;
		weights[17] = 42; values[17]= 10;
		weights[18] = 25; values[18]= 25;
		weights[19] = 21; values[19]= 21;
			
		
		KnapsackImp test = new KnapsackImp();
		
		/*Random rand = new Random();
		
		for(int i = 0; i < weights.length; i++) {
			weights[i] = rand.nextInt(10)+1;
			values[i] = rand.nextInt(10)+1;			
		}*/
		
		System.out.println(test.fractionalKnapsack(weights, values, capacity));
		System.out.println(test.discreteKnapsack(weights, values, capacity));

	}
}
