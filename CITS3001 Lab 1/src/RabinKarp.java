import java.util.ArrayList;

public class RabinKarp {
	public static ArrayList<Integer> naive(String T, String P) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		for(int s = 0; s < (T.length() - P.length()+1); s++) {
			boolean match = true;
			for(int j = 0; j < (P.length()-1); j++) {
				if(T.charAt(s+j) != P.charAt(j)) {
					match = false;
				}	
			}
			if(match) {
				result.add(s);
			}
		}
		return result;
	}
	
	static ArrayList<Integer> RK(String T, String P) {
		int M = P.length(); 
        int N = T.length(); 
		int p = 0;
		//int t = 0;
		//int d = 10;
		//int d_bar = 1;

		ArrayList<Integer> result = new ArrayList<Integer>();
		
		for (int i = 0; i < M; i++) 
        { 
            p = p * 256 + P.charAt(i); 
            //d_bar*=d;
            //t = t*10 + Character.getNumericValue(T.charAt(i)); 
        } 
		double z = 0;
		//d_bar/=d;
		for (int j = 0; j < M - 1; j++) {
			z = z * 256 + T.charAt(j);
		}
		for(int s = 0; s <= (N-M); s++) {
			z = (z % Math.pow(256, M-1)) * 256 + T.charAt(s+M-1);
			if(p == z) {
				result.add(s);
			}
			//int iP = Integer.valueOf(P);
			//p = p * 10 + iP.charAt(i);
		}
		return result;
	}
}
