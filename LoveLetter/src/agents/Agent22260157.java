package agents;
import loveletter.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */
public class Agent22260157 implements Agent{

	private State current;
	private int myIndex;

	HashMap<Integer, Card> knownPlayers = new HashMap<Integer, Card>();
	HashMap<Integer, Card> unknownPlayers = new HashMap<Integer, Card>();
	
	/**
   	* Reports the agents name
   	**/
	public String toString(){return "Agent22260157";}


	/**
	 * Method called at the start of a round
	 * @param start the starting state of the round
	 **/
	public void newRound(State start){
		current = start;
		myIndex = current.getPlayerIndex();
	}

	/**
	 * Method called when any agent performs an action. 
	 * @param act the action an agent performs
	 * @param results the state of play the agent is able to observe.
	 * **/
	public void see(Action act, State results){
		current = results;
	}
	
	/**
	 * 
	 * @param known true if targeting only players whose cards are known,
	 * else false
	 * @param hm true if all available opponents are handmaided, else false
	 * @return target player to target
	 */
	public int targetPlayer(boolean known, boolean hm) {
		int max = -1;
		int target = -1;
		if(hm) {
			for (int i = 0; i < current.numPlayers(); i++) {
				if(current.score(i) > max && !current.eliminated(i) && i != myIndex) {
					max = current.score(i);
					target = i;
				}
			}
		}
		if(!known && !hm) {
			for (int i = 0; i < current.numPlayers(); i++) {
				if(current.score(i) > max && !current.eliminated(i) && i != myIndex &&
						!current.handmaid(i)) {
					max = current.score(i);
					target = i;
				}
			}
		} else if(known && !hm) {
			for (Map.Entry<Integer, Card> entry : knownPlayers.entrySet()) {
				if(current.score(entry.getKey()) > max && 
						!current.eliminated(entry.getKey()) && 
						entry.getKey() != myIndex &&
						!current.handmaid(entry.getKey())) {
					max = current.score(entry.getKey());
					target = entry.getKey();
				}
			}
		}
		return target;
	}
	
	/**
	 * Perform an action after drawing a card from the deck
	 * @param c the card drawn from the deck
	 * @return the action the agent chooses to perform
	 * @throws IllegalActionException when the Action produced is not legal.
	 * */
	public Action playCard(Card c){
	  
		Action act = null;

		Card[] hand = new Card[2];
		hand[0] = current.getCard(myIndex);
		hand[1] = c;
		
		//array containing all unseen cards, shuffled to make guesses
		//utilising it less predictable to other AI
		ArrayList<Card> unseen = new ArrayList<Card>(Arrays.asList(current.unseenCards()));
		//Card[] unseen = current.unseenCards();
		
		//remove instance of hand card from unseen
		for(int i = 0; i < unseen.size(); i++) {
			if(unseen.get(i).name() == hand[0].name()) {
				unseen.remove(i);
				break;
			}
		}
		
		//retrieves most common unseen card. If there is a tie for most
		//common, the higher value takes precedence. Prince is default
		//as it is the highest value and frequency legal card to guess
		Card mfc = Card.PRINCE;
		for(Card cu : unseen) {
			if((cu != Card.GUARD) && 
					(Collections.frequency(unseen, cu) > Collections.frequency(unseen, mfc))) {
				mfc = cu;
			}
			if((cu != Card.GUARD) && 
					(Collections.frequency(unseen, cu) == Collections.frequency(unseen, mfc)) &&
					(mfc.value() < cu.value())) {
				mfc = cu;
			}
		}
		
		//clears old list of known cards and populates it with the currently
		//known cards, doing the same for a list of unknown players
		knownPlayers.clear();
		unknownPlayers.clear();
		for(int i = 0; i < current.numPlayers(); i++) {
			if(current.getCard(i) != null) {
				knownPlayers.put(i, current.getCard(i));
			} else {
				unknownPlayers.put(i, current.getCard(i));
			}
		}

		//if countess must be played, play the countess
		if(((hand[0].name() == "PRINCE" || hand[1].name() == "PRINCE") || 
				(hand[0].name() == "KING" || hand[1].name() == "KING") ||
				(hand[0].name() == "PRINCESS" || hand[1].name() == "PRINCESS")) &&
				(hand[0].name() == "COUNTESS" || hand[1].name() == "COUNTESS")) {
			try {
				act = Action.playCountess(myIndex);
				if(current.legalAction(act, c)){
					return act;
				}
			} catch (IllegalActionException e) {/*do nothing*/}  
		}
		
		//If the king and princess are held and a guard or prince is known, 
		//swap with that player to take them out next turn
		if((hand[0].name() == "PRINCESS" || hand[1].name() == "PRINCESS") &&
				(hand[0].name() == "KING" || hand[1].name() == "KING")) {
			for(Map.Entry<Integer, Card> entry : knownPlayers.entrySet()) {
				if(entry.getValue().name() == "GUARD" || entry.getValue().name() == "PRINCE") {
					try {
						act = Action.playKing(myIndex, entry.getKey());
						if(current.legalAction(act, c)){
							return act;
						}
					} catch (IllegalActionException e) {/*do nothing*/}
				}
			}
		}
		
		//target highest scoring knownPlayer
		int target = targetPlayer(true, false);
	  
		//If a player's card is known, guess it to eliminate the player with a guard,
		//prioritising the player with the highest score
		for(Map.Entry<Integer, Card> entry : knownPlayers.entrySet()) {
			if((hand[0].name() == "GUARD" || hand[1].name() == "GUARD") 
					&& (entry.getKey() == target)) {
				try {
					act = Action.playGuard(myIndex, entry.getKey(), entry.getValue());
					if(current.legalAction(act, c)){
						return act;
					}
				} catch (IllegalActionException e) {/*do nothing*/}
			}
		}
		
		//If a player's card is known, play the baron should the other card in
		//hand be valuable enough to defeat it
		for(Map.Entry<Integer, Card> entry : knownPlayers.entrySet()) {
			if((hand[0].name() == "BARON") && (entry.getKey() != myIndex) 
					&& (entry.getValue().value() < hand[1].value())){
				try {
					act = Action.playBaron(myIndex, entry.getKey());
					if(current.legalAction(act, c)){
						return act;
					}
				} catch (IllegalActionException e) {/*do nothing*/}
			}
			else if((hand[1].name() == "BARON") && (entry.getKey() != myIndex) 
					&& (entry.getValue().value() < hand[0].value())){
				try {
					act = Action.playBaron(myIndex, entry.getKey());
					if(current.legalAction(act, c)){
						return act;
					}
				} catch (IllegalActionException e) {/*do nothing*/}
			}
		}
		
		//If a player's card is known to be the princess, eliminate the player with a prince
		//should a guard be unavailable
		for(Map.Entry<Integer, Card> entry : knownPlayers.entrySet()) {
			if((hand[0].name() == "PRINCE" || hand[1].name() == "PRINCE") 
					&& (entry.getKey() != myIndex) && entry.getValue().name() == "PRINCESS" &&
					current.handmaid(entry.getKey())) {
				try {
					act = Action.playPrince(myIndex, entry.getKey());
					if(current.legalAction(act, c)){
						return act;
					}
				} catch (IllegalActionException e) {/*do nothing*/}
			}
		}
		
		//target unknown, non-eliminated players with the highest score
		target = targetPlayer(false, false);
	
		//If there are players who possesses an unknown card, reveal the
		//one held by the highest scoring player with the priest
		if((hand[0].name() == "PRIEST" || hand[1].name() == "PRIEST")) {
			try {
				act = Action.playPriest(myIndex, target);
				if(current.legalAction(act, c)){
					return act;
				}
			} catch (IllegalActionException e) {/*do nothing*/}
		}
		
		
		//If a guard is held, play it against the winning player, guessing
		//an unseen card that is not a guard nor in hand
		if((hand[0].name() == "GUARD" || hand[1].name() == "GUARD")) {
			for(int i = 0; i < unseen.size(); i++) {
				if(unseen.get(i).name() != "GUARD") {
					try {
						act = Action.playGuard(myIndex, target, mfc);
						if(current.legalAction(act, c)){
							return act;
						}
					} catch (IllegalActionException e) {/*do nothing*/}
				}
			}
		}
		
		//If a priest is held, play it regardless
		for (Integer key : knownPlayers.keySet()) {
			if((hand[0].name() == "PRIEST" || hand[1].name() == "PRIEST")) {
				try {
					act = Action.playPriest(myIndex, key);
					if(current.legalAction(act, c)){
						return act;
					}
				} catch (IllegalActionException e) {/*do nothing*/}
			}
		}
							
		//If the average value of unseen cards is less than that of the other
		//card in hand, play the baron against an opponent we don't know has 
		//a card with a higher value
		int total = 0;
		for(int i = 0; i < unseen.size(); i++) {
			total += unseen.get(i).value();
		}
		for (Integer key : unknownPlayers.keySet()) {
			if(hand[0].name() == "BARON") {
				if((total/unseen.size() < hand[1].value()) ||
						(hand[1].name() == "BARON" || hand[1].name() == "PRINCESS")) {
					try {
						act = Action.playBaron(myIndex, key);
						if(current.legalAction(act, c)){
							return act;
						}
					} catch (IllegalActionException e) {/*do nothing*/}	
				}
			} else if(hand[1].name() == "BARON") {
				if(total/unseen.size() < hand[0].value() ||
						hand[0].name() == "PRINCESS") {
					try {
						act = Action.playBaron(myIndex, key);
						if(current.legalAction(act, c)){
							return act;
						}
					} catch (IllegalActionException e) {/*do nothing*/}	
				} 
			}
		}
		
		//If a handmaid is held, play it
		try {
			act = Action.playHandmaid(myIndex);
			if(current.legalAction(act, c)){
				return act;
			}
		} catch (IllegalActionException e) {/*do nothing*/}
		
		//if a prince is held, play it against the winning, non-eliminated player
		try {
			act = Action.playPrince(myIndex, target);
			if(current.legalAction(act, c)){
				return act;
			}
		} catch (IllegalActionException e) {/*do nothing*/}
		
		//if the king is the last option, play it against the losing player
		//if the to be swapped card's value is higher than the average of
		//the remaining cards. Else, play against the highest scoring player.
		if((hand[0].name() == "KING" && hand[1].value() > total/unseen.size()) ||
				(hand[1].name() == "KING" && hand[0].value() > total/unseen.size())) {
			int min = 5;
			target = -1;
			for (int i = 0; i < current.numPlayers(); i++) {
				if(current.score(i) < min && !current.eliminated(i) && i != myIndex &&
						!current.handmaid(i)) {
					min = current.score(i);
					target = i;
				}
			}
		}
		try {
			act = Action.playKing(myIndex, target);
			if(current.legalAction(act, c)){
				return act;
			}
		} catch (IllegalActionException e) {/*do nothing*/}
		
		//in the case that a handmaid blocks everything, play the lowest value card
		//against the winning player
		target = targetPlayer(false, true);
		
		if(hand[0].value() > hand[1].value()) {
			try{
				switch(hand[1]){
				case GUARD:
					act = Action.playGuard(myIndex, target, mfc);
					break;
				case PRIEST:
					act = Action.playPriest(myIndex, target);
					break;
				case BARON:  
					act = Action.playBaron(myIndex, target);
					break;
				case PRINCE:  
					act = Action.playPrince(myIndex, target);
					break;
				case KING:
					act = Action.playKing(myIndex, target);
					break;
				default:
					act = null;//never play princess
				}
			}catch(IllegalActionException e){/*do nothing, just try again*/}
		} else {
			try{
				switch(hand[0]){
				case GUARD:
					act = Action.playGuard(myIndex, target, mfc);
					break;
				case PRIEST:
					act = Action.playPriest(myIndex, target);
					break;
				case BARON:  
					act = Action.playBaron(myIndex, target);
					break;
				case PRINCE:  
					act = Action.playPrince(myIndex, target);
					break;
				case KING:
					act = Action.playKing(myIndex, target);
					break;
				default:
					act = null;//never play princess
				}
			}catch(IllegalActionException e){/*do nothing, just try again*/}
		}
		return act;
	}
}