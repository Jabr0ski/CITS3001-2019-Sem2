package loveletter;

import agents.*;

public class BotTester {

	public static void main(String[] args) {
		Agent[] agents = {new RandomAgent(), new RandomAgent(), new RandomAgent(), new Agent22260157()};
		LoveLetter env = new LoveLetter();
		int wins = 0;
		int games = 0;
		for(int i = 0; i<100; i++) {
			int[] results = env.playGame(agents);
			if(results[3]==4) {
				wins++;
			}
			games++;
		}
		double percentage = ((double)wins/(double)games)*100;
		System.out.println("Your bot won "+percentage+"% of games");
		System.out.println("Your bot played "+games+" games");
		System.out.println("Your bot won "+wins+" games");
	}
}

