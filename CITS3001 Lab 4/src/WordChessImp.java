import java.util.*;

public class WordChessImp implements WordChess {
	
	/**
	 * Finds a shortest sequence of words String[] dictionary such that the first word 
	 * is the startWord, the last word is the endWord, and each word is equal to the
	 * previous word with one letter changed. Returns the result as a String array with
	 * the sequence of words used to reach the endWord from the startWord in order.
	 */
	public String[] findPath(String[] dictionary, String startWord, String endWord) {
		
		Set<String> effDic = new HashSet<String>();
		
		//Creates a smaller dictionary as a HashSet so unneeded words are not 
		//traversed in searches of the dictionary later
		for(int i = 0; i < dictionary.length; i++) {
			if(dictionary[i].length() == startWord.length()) {
				effDic.add(dictionary[i]);
			}
		}
		
		Queue<String> wordQ = new LinkedList<String>();
		List<String> wordOrder = new ArrayList<String>();
		Set<String> seen = new HashSet<String>();
		
		Map<String, String> parentTracker = new HashMap<String, String>();
		
		boolean found = false;

		wordQ.add(startWord);
		seen.add(startWord);
		
		//iterates through possible words by exchanging each character from left to right
		//with another alphabetical character. This continues until an actual word is reached
		//which brings the program closer to the endWord. This process repeats until the
		//word is found or the search has been exhausted and the word was not found.
		while(!wordQ.isEmpty() && !found) {
			
			String currWord = wordQ.poll();
			
			for(int i = 0; i < currWord.length(); i++) {
				
				if(found) {
					break;
				}
				
				for(char c = 'A'; c <= 'Z'; c++)
				{
					
					StringBuilder temp = new StringBuilder(currWord);
					temp.setCharAt(i, c);
					String tempStr = temp.toString();
					
					if(seen.contains(tempStr))
						continue;

					if(effDic.contains(tempStr)) {
						parentTracker.put(tempStr, currWord);
						wordQ.add(tempStr);
						seen.add(tempStr);
					}
					
					if(tempStr.equals(endWord))
					{
						found = true;
						break;
					}
				}
			}
		}

		if(!found) {
			return null;
		}
		
		String testWord = endWord;
		
		//Appends words as they were discovered and used in reverse order from the
		//parentTracker hashMap to wordOrder ArrayList
		while(!testWord.equals(startWord))
		{
			wordOrder.add(testWord);
			testWord = parentTracker.get(testWord);
		}
		
		wordOrder.add(testWord);
		String output[] = new String[wordOrder.size()];
		
		//order reversed to be correct
		Collections.reverse(wordOrder);
		wordOrder.toArray(output);
		return output;
	}

}
