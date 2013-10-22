package it.pokefundroid.pokedroid.utils;

import it.pokefundroid.pokedroid.models.Monster;

import java.util.Random;

import android.location.Location;
import android.util.Log;

public class FindingUtilities {

	// possibilita' su 1000 elementi di essere selezionati nel set;
	private final static int[] CHANCE = { 950, 800, 300, 60, 4, 0 };

	//possibilita' su 100 di essere pescato
	private final static int[] FINDINGCHANCE = { 60, 47, 37, 19, 13, 8};

	public static final int FIGHT_PROXIMITY = 10;

	private final static int MIN_POKEMON = 3;
	private final static int MAX_POKEMON = 5;

	public static Monster[] currentPkmnSet = null;

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
	private static int MAX_POKEMON_IN_RANGE = 10;

	/**
	 * metodo che dato un array di interi estrae un elemento fra questi, ne
	 * ricava la rarita' e avvia la procedura per vedere se il 'ritrovamento'
	 * del pkmn ha avuto successo
	 */
	public static Monster[] findInPosition(double latitude, double longitude,
			double accuracy, int number) {
		Random random = new Random(System.currentTimeMillis());
		Monster[] ret = new Monster[number];
		boolean changed = generateRandoms(latitude, longitude);
		
		if (changed || currentPkmnSet == null) {
			generateSet();
			Log.d("FindingUtilities.generateRandoms()", latitude + "  "
					+ longitude);
			Log.d("FindingUtilities.generateRandoms()",
					((long) latitude * 10 * 10) + "  "
							+ ((long) longitude * 10 * 10));
		}
		
		for (int j = 0; j < number; j++) {
			int rdm = random.nextInt(100);
			ret[j] = null;
			int selection = random.nextInt(currentPkmnSet.length);
			int possibility = FINDINGCHANCE[currentPkmnSet[selection].getRarity()];
			if(rdm<possibility) {
				ret[j] = currentPkmnSet[selection];
			}

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

	public static Monster[] getPokemonSet() {
		return currentPkmnSet;
	}

	public static int generateHowManyPokemonInRange(double range) {
		Random random = new Random(System.currentTimeMillis());
		return random.nextInt((int) (MAX_POKEMON_IN_RANGE * (range < 10 ? range
				: 15)) + 1);
	}

	private static boolean existInSet(int id) {
		boolean ret = false;

		for (int i = 0; i < currentPkmnSet.length; i++) {
			if (currentPkmnSet[i] == null)
				break;
			if (currentPkmnSet[i].getId() == id)
				ret = true;
		}

		return ret;
	}

	private static void generateSet() {
		int dim = setRandom.nextInt(MAX_POKEMON - (MIN_POKEMON - 1))
				+ MIN_POKEMON;
		Log.e("AAAAAAARGH", "" + dim);
		currentPkmnSet = new Monster[dim];

		for (int i = 0; i < dim; i++) {
			currentPkmnSet[i] = null;
		}

		for (int i = 0; i < dim;) {
			int id = selectionRandom.nextInt(151) + 1;
			boolean b = selectByRarity(Monster.getRarityFromId(id));
			if (b) {

				if (!existInSet(id)) {
					currentPkmnSet[i] = new Monster(id);
					i++;
				}
			}
		}
		String log = "";
		for (int i = 0; i < currentPkmnSet.length; i++) {
			log += currentPkmnSet[i].getName() + "  ";
		}

		Log.d("FindingUtilities.generateSet()", log + "\n" + selectionSeed
				+ "  " + setSeed);
	}

	private static boolean generateRandoms(double latitude, double longitude) {
		// quadrati di 0,001 gradi, circa 111 metri all'equatore
		boolean ret = false;

		// sposto di 3 cifre in sotto la virgola e taglio i decimali castando a
		// long
		latitude *= Math.pow(10, 2);
		longitude *= Math.pow(10, 2);
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

	public static Location getRandomLocation(double x0, double y0, double radius) {
		Random random = new Random();

		// Convert radius from meters to degrees
		double radiusInDegrees = radius / 111300f;
		double minimumRadius = (FIGHT_PROXIMITY / 1.5) / 111300f;

		double u = random.nextDouble();
		double v = random.nextDouble();
		double w = radiusInDegrees * Math.sqrt(u);
		double t = 2 * Math.PI * v;
		double x = (w + minimumRadius) * Math.cos(t);
		double y = (w + minimumRadius) * Math.sin(t);
		
		

		// Adjust the x-coordinate for the shrinking of the east-west distances
		//double new_x = x / Math.cos(y0); probably not needed
		
		

		double foundLongitude = /*new_*/x + y0;
		double foundLatitude = y + x0;
		Location out = new Location("randomize");
		out.setLatitude(foundLatitude);
		out.setLongitude(foundLongitude);
		return out;

	}
}
