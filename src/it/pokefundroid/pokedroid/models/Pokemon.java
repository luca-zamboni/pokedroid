package it.pokefundroid.pokedroid.models;

import it.pokefundroid.pokedroid.utils.StaticClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Pokemon {
	
	private int id;
	private String name; 
	
	//basic types of the pokemon
	private int firstType;
	private int secndType;
	
	//base stats of the pokemon
	private short baseHp;
	private short baseAtk;
	private short baseDef;
	private short baseSAtk;
	private short baseSDef;
	private short baseSpd;
	
	//ev yield by the pokemon
	private short hpYield;
	private short atkYield;
	private short defYield;
	private short spAtkYield;
	private short spDefYield;
	private short spdYield;
	
	public final static int NUMBER_POKEMON = 151; 
	
	//rarity
	public final static int VERYCOMMON = 0;
	public final static int COMMON = 1;
	public final static int SUBRARE = 2;
	public final static int RARE = 3;
	public final static int VERYRARE = 4;
	public final static int NONPRESENT = 5;
	
	//stat id
	public final static int HEALTPOINT = 1;
	public final static int ATTACK = 2;
	public final static int DEFENCE = 3;
	public final static int SPATTACK = 4;
	public final static int SPDEFENCE = 5;
	public final static int SPEED = 6;
	
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
		RARE, NONPRESENT, NONPRESENT, VERYRARE, VERYCOMMON //3dratini, mewtwo, mew
	};
	/**
	 * metodo per ricavarsi dall'id del pokemon la sua rarita'
	 * da associare a un array/classe/db/qualcosa per ricavarsi da li la rarita'
	 * possibilita' di cambiare il db ogni tanto per cambiare le rarita' dei pokemon
	 * id-1 perche' i pokemon partono da 1 mentre l'array da 0
	 * */
	public static int getRarityFromId(int id) {
		return (RARITY[id-1]);
	}
	
	public static String getImagUri(int id) {
		if (id < 10)
			return "assets://pkm/pkfrlg00" + id + ".png";
		if (id < 100)
			return "assets://pkm/pkfrlg0" + id + ".png";
		else
			return "assets://pkm/pkfrlg" + id + ".png";
	}
	
	public static Bitmap getImagBitmap(Context context,int id) {
		String s;
		if (id < 10)
			s = "pkm/pkfrlg00" + id + ".png";
		else{
			if (id < 100)
				s = "pkm/pkfrlg0" + id + ".png";
			else
				s = "pkm/pkfrlg" + id + ".png";
		}
		

	    return StaticClass.getBitmapFromAssats(context, s);
	}
	
//////// FINE STATIC ----- INIZIO CLASSE ISTANZIABILE POKEMON
	
	public Pokemon(int id){
		this.id = id;
		this.name = StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_species", "identifier", "id="+id);
		
		//get base stats from the db
		this.baseHp = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "base_stat", 
				"pokemon_id="+id+" and stat_id="+HEALTPOINT));
		this.baseAtk = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "base_stat", 
				"pokemon_id="+id+" and stat_id="+ATTACK));
		this.baseDef = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "base_stat", 
				"pokemon_id="+id+" and stat_id="+DEFENCE));
		this.baseSAtk = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "base_stat", 
				"pokemon_id="+id+" and stat_id="+SPATTACK));
		this.baseSDef = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "base_stat", 
				"pokemon_id="+id+" and stat_id="+SPDEFENCE));
		this.baseSpd = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "base_stat", 
				"pokemon_id="+id+" and stat_id="+SPEED)); 
		
		this.hpYield = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "effort", 
				"pokemon_id="+id+" and stat_id="+HEALTPOINT));
		this.atkYield = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "effort", 
				"pokemon_id="+id+" and stat_id="+ATTACK));
		this.defYield = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "effort", 
				"pokemon_id="+id+" and stat_id="+DEFENCE));
		this.spAtkYield = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "effort", 
				"pokemon_id="+id+" and stat_id="+SPATTACK));
		this.spDefYield = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "effort", 
				"pokemon_id="+id+" and stat_id="+SPDEFENCE));
		this.spdYield = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_stats", "effort", 
				"pokemon_id="+id+" and stat_id="+SPEED)); 
		
		//TODO query to take the double pkmn type 0 isn't a type id: no type id
		this.firstType = 0; 
		this.secndType = 0;
	}
	
	public int getRarity() {
		return RARITY[this.id];
	}
	
	public String getName(){;
		return name;
	}
	
	public int getId(){
		return id;
	}

	public short getBaseHp() {
		return baseHp;
	}

	public short getBaseAtk() {
		return baseAtk;
	}

	public short getBaseDef() {
		return baseDef;
	}

	public short getBaseSAtk() {
		return baseSAtk;
	}

	public short getBaseSDef() {
		return baseSDef;
	}

	public short getBaseSpd() {
		return baseSpd;
	}

	public int getFirstType() {
		return firstType;
	}

	public int getSecndType() {
		return secndType;
	}

	public short getHpYield() {
		return hpYield;
	}

	public short getAtkYield() {
		return atkYield;
	}

	public short getDefYield() {
		return defYield;
	}

	public short getSpAtkYield() {
		return spAtkYield;
	}

	public short getSpDefYield() {
		return spDefYield;
	}

	public short getSpdYield() {
		return spdYield;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o == this) ret = true;
		else if(o instanceof Pokemon) {
			ret = this.id == ((Pokemon) o).getId();
		} else {
			ret = false;
		}
		return ret;
	}
	
	
	
}
