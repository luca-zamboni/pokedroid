package it.pokefundroid.pokedroid.utils;

import java.util.Random;

import android.util.Log;

public class FindingUtilities {
	
	public final int VERYCOMMON = 0;
	public final int COMMON = 1;
	public final int SUBRARE = 2;
	public final int RARE = 3;
	public final int VERYRARE = 4;
	private final static int[] CHANCE = {15, 18, 24, 50, 140}; // originals 19, 22, 28, 56, 150
	
	/**
	 * metodo che dato un array di interi estrae un elemento fra questi, ne ricava la rarita' e avvia la procedura
	 * per vedere se il 'ritrovamento' del pkmn ha avuto successo
	 * */
	public static int findInGroup(int[] group) {
		int retval = -1;
		Random random = new Random(System.currentTimeMillis());
		int selection = random.nextInt(group.length);
		if (findByRarity(getRarityFromId(selection))) {
			retval = selection;
		}
		return retval;
	}
	
	/**
	 * data la rarity del pokemon usando uno pseudorandom calcola se in quel passo ha trovato un pokemon con quella rarita'*/
	public static boolean findByRarity(int rarity) {
		boolean found = false;
		Random random = new Random(System.currentTimeMillis());
		found = (random.nextInt(CHANCE[rarity])==0);
		return found;
	}
	
	/**
	 * metodo per ricavarsi dall'id del pokemon la sua rarita'
	 * da associare a un array/classe/db/qualcosa per ricavarsi da li la rarita'
	 * possibilit' di cambiare il db ogni tanto per cambiare le rarita' dei pokemon
	 * */
	public static int getRarityFromId(int id) {
		return (id % 3);
	}
}
