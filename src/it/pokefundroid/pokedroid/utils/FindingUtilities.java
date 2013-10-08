package it.pokefundroid.pokedroid.utils;

import it.pokefundroid.pokedroid.models.Pokemon;

import java.util.Random;

import android.location.Location;
import android.util.Log;

public class FindingUtilities {

	private final static int[] CHANCE = { 950, 800, 200, 30, 1, 0 };
	// possibilita' su 1000 elementi di essere selezionati nel set;

	private final static int[] FINDINGCHANCE = { 100, 80, 35, 15, 5, 1 };

	public static Pokemon[] currentPkmnSet = null;

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
	private static int MAX_POKEMON_IN_RANGE = 20;

	/**
	 * metodo che dato un array di interi estrae un elemento fra questi, ne
	 * ricava la rarita' e avvia la procedura per vedere se il 'ritrovamento'
	 * del pkmn ha avuto successo
	 */
	public static Pokemon findInPosition(double latitude, double longitude) {
		Random random = new Random(System.currentTimeMillis());
		Pokemon ret = null;
		boolean changed = generateRandoms(latitude, longitude);
		if (changed || currentPkmnSet == null) {
			generateSet();
		}
		int total = FINDINGCHANCE[currentPkmnSet[0].getRarity()]
				+ FINDINGCHANCE[currentPkmnSet[1].getRarity()]
				+ FINDINGCHANCE[currentPkmnSet[2].getRarity()];
		int tmp = random.nextInt(total);
		if ( tmp < FINDINGCHANCE[currentPkmnSet[0].getRarity()] ) {
			ret = currentPkmnSet[0];
		} else if ( tmp < (total - FINDINGCHANCE[currentPkmnSet[2].getRarity()]) ) {
			ret = currentPkmnSet[1];
		} else {
			ret = currentPkmnSet[2];
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
	
	public static Pokemon[] getPokemonSet() {
		return currentPkmnSet;
	}
	
	public static int generateHowManyPokemonInRange(double range){
		Random random = new Random(System.currentTimeMillis());
		return random.nextInt((int) (MAX_POKEMON_IN_RANGE*(range<10?range:15))+1);
	}

	private static void generateSet() {
		currentPkmnSet = new Pokemon[3];
		for (int i = 0; i < 3;) {
			int id = selectionRandom.nextInt(151);
			id = id+1;
			boolean b = selectByRarity(Pokemon.getRarityFromId(id));
			if (b)
				currentPkmnSet[i++] = new Pokemon(id);
		}
		
		Log.d("FindingUtilities.generateSet()", currentPkmnSet[0].getName()+"  "+currentPkmnSet[1].getName()+"  "+currentPkmnSet[2].getName()+"\n"+
		selectionSeed+"  "+setSeed);
	}

	private static boolean generateRandoms(double latitude, double longitude) {
		// quadrati di 0,001 gradi, circa 111 metri all'equatore
		boolean ret = false;
		
		Log.d("FindingUtilities.generateRandoms()", latitude+"  "+longitude);
		
		//sposto di 3 cifre in sotto la virgola e taglio i decimali castando a long
		latitude *= 10 ^ 3;
		longitude *= 10 ^ 3;
		long lat = (long) latitude;
		long lon = (long) longitude;
		
		long tSelectionSeed = ((lat * lon) - (151 * lat));
		long tSetSeed = ((lat * lon) - (270 * lon));
		
		if (tSelectionSeed != selectionSeed || tSetSeed != setSeed) {
			selectionSeed = tSelectionSeed;
			setSeed = tSetSeed;
			ret = true;
		}
		
		
		selectionRandom = new Random(selectionSeed);
		setRandom = new Random(setSeed);
		return ret;
	}
	
	public static Location getLocation(double x0, double y0, double radius) {
	    Random random = new Random();

	    // Convert radius from meters to degrees
	    double radiusInDegrees = radius / 111000f;

	    double u = random.nextDouble();
	    double v = random.nextDouble();
	    double w = radiusInDegrees * Math.sqrt(u);
	    double t = 2 * Math.PI * v;
	    double x = w * Math.cos(t);
	    double y = w * Math.sin(t);

	    // Adjust the x-coordinate for the shrinking of the east-west distances
	    double new_x = x / Math.cos(y0);

	    double foundLongitude = new_x + y0;
	    double foundLatitude = y + x0;
	    Location out = new Location("randomize");
	    out.setLatitude(foundLatitude);
	    out.setLongitude(foundLongitude);
	    return out;
	}
}
