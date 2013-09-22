package it.pokefundroid.pokedroid.models;

import it.pokefundroid.pokedroid.utils.BaseAdapter;
import android.content.Context;

public class Pokemon {
	
	public int id;
	public String name;
	public String type;
	
	public final static int NUMBER_POKEMON = 151; 
	
	//rarity
	public final static int VERYCOMMON = 0;
	public final static int COMMON = 1;
	public final static int SUBRARE = 2;
	public final static int RARE = 3;
	public final static int VERYRARE = 4;
	public final static int NONPRESENT = 5;
	
	public final static int[] IDS = {
		1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
		11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 
		21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 
		31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 
		41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 
		51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 
		61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 
		71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 
		81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 
		91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 
		101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
		111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
		121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 
		131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 
		141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151
	};
	
	//commento in fondo il numero delle evoluzioni/pokemon seguito dalla prima forma
	public final static int[] RARITY = {
		SUBRARE, NONPRESENT, NONPRESENT, SUBRARE, NONPRESENT, NONPRESENT, SUBRARE, NONPRESENT, NONPRESENT,	//9starters
		VERYCOMMON, COMMON, NONPRESENT, VERYCOMMON, COMMON, NONPRESENT, VERYCOMMON, SUBRARE, NONPRESENT, //3caterpie, 3weedle, 3pidgey
		COMMON, NONPRESENT, COMMON, NONPRESENT, SUBRARE, NONPRESENT, SUBRARE, NONPRESENT, SUBRARE, NONPRESENT, // 2rattata, 2spearow, 2ekans, 2pikachu, 2sandshrew
		SUBRARE, NONPRESENT, NONPRESENT, SUBRARE, NONPRESENT, NONPRESENT, RARE, NONPRESENT, //3nidoranmaschio, 3nidoran femmina, 2clefary
		SUBRARE, NONPRESENT, SUBRARE, NONPRESENT, VERYCOMMON, COMMON, COMMON, SUBRARE, NONPRESENT, //2vulpix, 2jigglypuff, 2zubat D:, 3oddish
		SUBRARE, NONPRESENT, SUBRARE, NONPRESENT, VERYCOMMON, NONPRESENT, COMMON, NONPRESENT, //2paras, 2venonath, 2diglett, 2meowth
		COMMON, NONPRESENT, SUBRARE, NONPRESENT, SUBRARE, NONPRESENT, VERYCOMMON, SUBRARE, NONPRESENT, //2psyduck, 2mankey, 2growlite, 3poliwag
		SUBRARE, SUBRARE, NONPRESENT, COMMON, SUBRARE, NONPRESENT, COMMON, SUBRARE, NONPRESENT, //3abra, 3machop, 3bellsprout
		VERYCOMMON, NONPRESENT, VERYCOMMON, SUBRARE, NONPRESENT, SUBRARE, NONPRESENT, //2tentacool, 3geodude, 2ponyta
		RARE, NONPRESENT, COMMON, NONPRESENT, RARE, VERYCOMMON, NONPRESENT, SUBRARE, NONPRESENT, //2slowpoke, 3magnemite, 1farfetch'd, 2doduo, 2seel
		COMMON, NONPRESENT, SUBRARE, NONPRESENT, SUBRARE, RARE, NONPRESENT, SUBRARE, RARE, NONPRESENT, //2grimer, 2shellder, 3gastly, 1onix, 2drowzee
		COMMON, NONPRESENT, VERYCOMMON, COMMON, RARE, NONPRESENT, SUBRARE, NONPRESENT, //2krabby, 2voltorb, 2exeggcute, 2cubone
		SUBRARE, SUBRARE, SUBRARE, SUBRARE, NONPRESENT, SUBRARE, NONPRESENT, //1hitmonlee, 1hitmonchan, 1lickitung, 2koffing, 2rhyhorn
		RARE, RARE, SUBRARE, SUBRARE, NONPRESENT, COMMON, NONPRESENT, SUBRARE, NONPRESENT, //1chansey, 1tangela, 1kangaskhan, 2horsea, 2goldeen, 2staryu
		RARE, RARE, RARE, RARE, RARE, RARE, SUBRARE, VERYCOMMON, SUBRARE, //1mime, 1scyther, 1jynx, 1electabuzz, 1magmar, 1pinsir, 1tauros, 2magikarp
		SUBRARE, RARE, RARE, NONPRESENT, NONPRESENT, NONPRESENT, RARE, RARE, NONPRESENT, //1lapras, 1ditto, 4eevee, 1porygon, 2omanyte
		RARE, NONPRESENT, RARE, SUBRARE, VERYRARE, VERYRARE, VERYRARE, //2kabuto, 1aerodactyl, 1snorlax, 3 legendary birds
		RARE, NONPRESENT, NONPRESENT, VERYRARE, VERYRARE //3dratini, mewtwo, mew
	};
	
	public static void fillDatabasePokemon(Context c){
		BaseAdapter a = new BaseAdapter(c);
		a.open();
		/// qua bosognerebbe inserirli tutti
		a.insertPokemonBase(1, "Bulbasaur", "erba", RARITY[0]);
		a.insertPokemonBase(2, "Ivisaur", "erba", RARITY[1]);
		a.insertPokemonBase(3, "Venosaur", "erba", RARITY[2]);
		a.close();
	}
	
	/**
	 * metodo per ricavarsi dall'id del pokemon la sua rarita'
	 * da associare a un array/classe/db/qualcosa per ricavarsi da li la rarita'
	 * possibilita' di cambiare il db ogni tanto per cambiare le rarita' dei pokemon
	 * id-1 perche' i pokemon partono da 1 mentre l'array da 0
	 * */
	public static int getRarityFromId(int id) {
		return (RARITY[id-1]);
	}
	
	
	
	
}
