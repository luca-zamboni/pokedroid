package it.pokefundroid.pokedroid.utils;

import java.util.Random;

public class Utilities {
	
	public final int VERYCOMMON = 0;
	public final int COMMON = 1;
	public final int SUBRARE = 2;
	public final int RARE = 3;
	public final int VERYRARE = 4;
	private final static int[] CHANCE = {19, 22, 28, 56, 150};
	
	private static boolean findByRarity(int rarity) {
		boolean found = false;
		Random random = new Random(System.currentTimeMillis());
		found = (random.nextInt(CHANCE[rarity])==0);
		return found;
	}

}
