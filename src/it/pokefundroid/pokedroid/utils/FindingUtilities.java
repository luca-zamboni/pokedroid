package it.pokefundroid.pokedroid.utils;

import it.pokefundroid.pokedroid.models.Pokemon;

import java.util.Random;

import android.util.Log;

public class FindingUtilities { 

	private final static int[] CHANCE = { 950, 800, 200, 30, 1, 0 }; 
	// possibilita' su 1000 elementi di essere selezionati nel set;
	
	private final static int[] FINDINGCHANCE = {12, 16, 22, 40, 125};
	//original 18, 22, 27, 55, 150

	public static int[] currentPkmnSet = null;

	/**
	 * selectionRandom usato per scorrere tra tutti i pokemon, setRandom usato
	 * per decidere se il pokemon verra' inserito
	 */
	private static Random selectionRandom = null;
	private static Random setRandom = null;

	/**
	 * tengo i seed in memoria
	 */
	public static long selectionSeed = 0;
	public static long setSeed = 0;

	/**
	 * metodo che dato un array di interi estrae un elemento fra questi, ne
	 * ricava la rarita' e avvia la procedura per vedere se il 'ritrovamento'
	 * del pkmn ha avuto successo
	 */
	public static int findInPosition(double latitude, double longitude) {
		Random random = new Random(System.currentTimeMillis());
		int ret = -1;
		boolean changed = generateRandoms(latitude, longitude);
		if (changed || currentPkmnSet == null) {
			generateSet();
		}
		int tmp = random.nextInt(3);
		if (random.nextInt(FINDINGCHANCE[Pokemon.getRarityFromId(currentPkmnSet[tmp])])==1) {
			ret = currentPkmnSet[tmp];
		}
		return ret;
	}

	/**
	 * data la rarity del pokemon usando uno pseudorandom calcola se in quel
	 * passo ha trovato un pokemon con quella rarita'
	 */
	private static boolean selectByRarity(int rarity) {
		boolean found = false;
		Random random = new Random(System.currentTimeMillis());
		found = (random.nextInt(1000) < CHANCE[rarity]);
		return found;
	}

	private static void generateSet() {
		currentPkmnSet = new int[3];
		for (int i = 0; i < 3;) {
			int id = selectionRandom.nextInt(151);
			id = Pokemon.IDS[id];
			boolean b = selectByRarity(Pokemon.getRarityFromId(id));
			if (b) currentPkmnSet[i++] = id;
		}
	}

	private static boolean generateRandoms(double latitude, double longitude) {
		// quadrati di 0,001 gradi, circa 111 metri all'equatore
		boolean ret = false;
		latitude *= 10 ^ 3;
		longitude *= 10 ^ 3;
		long lat = (long) latitude;
		long lon = (long) longitude;
		if (((lat*lon)-(151*lat)) != selectionSeed || ((lat*lon)-(270*lon)) != setSeed) {
			selectionSeed = (lat*lon)-(151*lat);
			setSeed = (lat*lon)-(270*lon);
			ret = true;
		}
		selectionRandom = new Random(selectionSeed);
		setRandom = new Random(setSeed);
		return ret;
	}
}
