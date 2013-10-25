package it.pokefundroid.pokedroid.models;

import it.pokefundroid.pokedroid.utils.BaseHelper;
import it.pokefundroid.pokedroid.utils.StaticClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

public class Monster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7653177864738700016L;
	private int id;
	private String name;

	// basic types of the pokemon
	private int firstType;
	private int secndType;

	// base stats of the pokemon
	private short baseHp;
	private short baseAtk;
	private short baseDef;
	private short baseSAtk;
	private short baseSDef;
	private short baseSpd;

	// ev yield by the pokemon
	private short hpYield;
	private short atkYield;
	private short defYield;
	private short spAtkYield;
	private short spDefYield;
	private short spdYield;

	public String my_name;
	private int dbId = 0;
	private PokemonSex sex;
	private double found_x;
	private double found_y;
	private int level;

	// personal ev of the pokemon initialized to 0
	private int hpEv = 0;
	private int atkEv = 0;
	private int defEv = 0;
	private int sAtkEv = 0;
	private int sDefEv = 0;
	private int spdEv = 0;

	// individual values initialized to 0, created when the db id get loaded
	private int ivSeed = 0;

	private int hpIv = 0;
	private int atkIv = 0;
	private int defIv = 0;
	private int sAtkIv = 0;
	private int sDefIv = 0;
	private int spdIv = 0;

	public enum PokemonSex {
		MALE, FEMALE, GENDERLESS
	}

	public final static int NUMBER_POKEMON = 151;

	// rarity
	public final static int VERYCOMMON = 0;
	public final static int COMMON = 1;
	public final static int SUBRARE = 2;
	public final static int RARE = 3;
	public final static int VERYRARE = 4;
	public final static int NONPRESENT = 4;

	// stat id
	public final static int HEALTPOINT = 1;
	public final static int ATTACK = 2;
	public final static int DEFENCE = 3;
	public final static int SPATTACK = 4;
	public final static int SPDEFENCE = 5;
	public final static int SPEED = 6;

	// status id (in team or out of team)
	private final static int TEAM = 10;
	private final static int BOX = 11;

	// query by status
	private final static String TEAMQUERY = "SELECT * FROM "
			+ BaseHelper.TABLE_PERSONAL_POKEMON + " ORDER BY "
			+ BaseHelper.ORDER + " LIMIT 6";
	private final static String BOXQUERY = "SELECT * FROM "
			+ BaseHelper.TABLE_PERSONAL_POKEMON + " ORDER BY "
			+ BaseHelper.ORDER + " LIMIT 6, 100000";

	// commento in fondo il numero delle evoluzioni/pokemon seguito dalla prima
	// forma
	public final static int[] RARITY = {
			SUBRARE,
			NONPRESENT,
			NONPRESENT,
			SUBRARE,
			NONPRESENT,
			NONPRESENT,
			SUBRARE,
			NONPRESENT,
			NONPRESENT, // 9starters
			VERYCOMMON,
			COMMON,
			NONPRESENT,
			VERYCOMMON,
			COMMON,
			NONPRESENT,
			VERYCOMMON,
			SUBRARE,
			NONPRESENT, // 3caterpie, 3weedle, 3pidgey
			COMMON,
			NONPRESENT,
			COMMON,
			NONPRESENT,
			SUBRARE,
			NONPRESENT,
			SUBRARE,
			NONPRESENT,
			SUBRARE,
			NONPRESENT, // 2rattata, 2spearow, 2ekans, 2pikachu, 2sandshrew
			SUBRARE,
			NONPRESENT,
			NONPRESENT,
			SUBRARE,
			NONPRESENT,
			NONPRESENT,
			RARE,
			NONPRESENT, // 3nidoranmaschio, 3nidoran femmina, 2clefary
			SUBRARE,
			NONPRESENT,
			SUBRARE,
			NONPRESENT,
			VERYCOMMON,
			COMMON,
			COMMON,
			SUBRARE,
			NONPRESENT, // 2vulpix, 2jigglypuff, 2zubat D:, 3oddish
			SUBRARE,
			NONPRESENT,
			SUBRARE,
			NONPRESENT,
			VERYCOMMON,
			NONPRESENT,
			COMMON,
			NONPRESENT, // 2paras, 2venonath, 2diglett, 2meowth
			COMMON,
			NONPRESENT,
			SUBRARE,
			NONPRESENT,
			SUBRARE,
			NONPRESENT,
			VERYCOMMON,
			SUBRARE,
			NONPRESENT, // 2psyduck, 2mankey, 2growlite, 3poliwag
			SUBRARE,
			SUBRARE,
			NONPRESENT,
			COMMON,
			SUBRARE,
			NONPRESENT,
			COMMON,
			SUBRARE,
			NONPRESENT, // 3abra, 3machop, 3bellsprout
			VERYCOMMON,
			NONPRESENT,
			VERYCOMMON,
			SUBRARE,
			NONPRESENT,
			SUBRARE,
			NONPRESENT, // 2tentacool, 3geodude, 2ponyta
			RARE, NONPRESENT, COMMON,
			NONPRESENT,
			RARE,
			VERYCOMMON,
			NONPRESENT,
			SUBRARE,
			NONPRESENT, // 2slowpoke, 3magnemite, 1farfetch'd, 2doduo, 2seel
			COMMON, NONPRESENT, SUBRARE, NONPRESENT, SUBRARE,
			RARE,
			NONPRESENT,
			SUBRARE,
			RARE,
			NONPRESENT, // 2grimer, 2shellder, 3gastly, 1onix, 2drowzee
			COMMON, NONPRESENT, VERYCOMMON, COMMON,
			RARE,
			NONPRESENT,
			SUBRARE,
			NONPRESENT, // 2krabby, 2voltorb, 2exeggcute, 2cubone
			SUBRARE, SUBRARE,
			SUBRARE,
			SUBRARE,
			NONPRESENT,
			SUBRARE,
			NONPRESENT, // 1hitmonlee, 1hitmonchan, 1lickitung, 2koffing,
						// 2rhyhorn
			RARE, RARE, SUBRARE, SUBRARE, NONPRESENT, COMMON,
			NONPRESENT,
			SUBRARE,
			NONPRESENT, // 1chansey, 1tangela, 1kangaskhan, 2horsea, 2goldeen,
						// 2staryu
			RARE, RARE, RARE, RARE, RARE, RARE, SUBRARE,
			VERYCOMMON,
			SUBRARE, // 1mime, 1scyther, 1jynx, 1electabuzz, 1magmar, 1pinsir,
						// 1tauros, 2magikarp
			SUBRARE, RARE, RARE, NONPRESENT, NONPRESENT, NONPRESENT, RARE,
			RARE, NONPRESENT, // 1lapras, 1ditto, 4eevee, 1porygon, 2omanyte
			RARE, NONPRESENT, RARE, SUBRARE, VERYRARE, VERYRARE, VERYRARE, // 2kabuto,
																			// 1aerodactyl,
																			// 1snorlax,
																			// 3
																			// legendary
																			// birds
			RARE, NONPRESENT, NONPRESENT, VERYRARE, VERYRARE // 3dratini,
																// mewtwo, mew
	};

	public Monster(int id) {
		this.id = id;
		this.name = StaticClass.dbpoke.oneRowOnColumnQuery("pokemon_species",
				"identifier", "id=" + id);

		// get base stats from the db
		this.baseHp = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery(
				"pokemon_stats", "base_stat", "pokemon_id=" + id
						+ " and stat_id=" + HEALTPOINT));
		this.baseAtk = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery(
				"pokemon_stats", "base_stat", "pokemon_id=" + id
						+ " and stat_id=" + ATTACK));
		this.baseDef = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery(
				"pokemon_stats", "base_stat", "pokemon_id=" + id
						+ " and stat_id=" + DEFENCE));
		this.baseSAtk = Short.parseShort(StaticClass.dbpoke
				.oneRowOnColumnQuery("pokemon_stats", "base_stat",
						"pokemon_id=" + id + " and stat_id=" + SPATTACK));
		this.baseSDef = Short.parseShort(StaticClass.dbpoke
				.oneRowOnColumnQuery("pokemon_stats", "base_stat",
						"pokemon_id=" + id + " and stat_id=" + SPDEFENCE));
		this.baseSpd = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery(
				"pokemon_stats", "base_stat", "pokemon_id=" + id
						+ " and stat_id=" + SPEED));

		this.hpYield = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery(
				"pokemon_stats", "effort", "pokemon_id=" + id + " and stat_id="
						+ HEALTPOINT));
		this.atkYield = Short.parseShort(StaticClass.dbpoke
				.oneRowOnColumnQuery("pokemon_stats", "effort", "pokemon_id="
						+ id + " and stat_id=" + ATTACK));
		this.defYield = Short.parseShort(StaticClass.dbpoke
				.oneRowOnColumnQuery("pokemon_stats", "effort", "pokemon_id="
						+ id + " and stat_id=" + DEFENCE));
		this.spAtkYield = Short.parseShort(StaticClass.dbpoke
				.oneRowOnColumnQuery("pokemon_stats", "effort", "pokemon_id="
						+ id + " and stat_id=" + SPATTACK));
		this.spDefYield = Short.parseShort(StaticClass.dbpoke
				.oneRowOnColumnQuery("pokemon_stats", "effort", "pokemon_id="
						+ id + " and stat_id=" + SPDEFENCE));
		this.spdYield = Short.parseShort(StaticClass.dbpoke
				.oneRowOnColumnQuery("pokemon_stats", "effort", "pokemon_id="
						+ id + " and stat_id=" + SPEED));

		// TODO query to take the double pkmn type 0 isn't a type id: no type id
		this.firstType = 0;
		this.secndType = 0;
	}

	public Monster(int id, String my_name, PokemonSex sex, double found_x,
			double found_y, int level) {
		this(id);
		this.my_name = my_name;
		this.sex = sex;
		this.found_x = found_x;
		this.found_y = found_y;
		this.level = 20;
		this.level = level;

	}

	// /// static metod

	public static void scambiaOrdineMonster(int idDb1, int idDb2) {
		String temp1, temp2;
		int tempint1, tempint2;
		temp1 = StaticClass.dbpoke.oneRowOnColumnQuery(
				BaseHelper.TABLE_PERSONAL_POKEMON, BaseHelper.ORDER,
				BaseHelper.MY_ID + " = " + idDb1);
		tempint1 = Integer.parseInt(temp1);
		temp2 = StaticClass.dbpoke.oneRowOnColumnQuery(
				BaseHelper.TABLE_PERSONAL_POKEMON, BaseHelper.ORDER,
				BaseHelper.MY_ID + " = " + idDb2);
		tempint2 = Integer.parseInt(temp2);
		StaticClass.dbpoke.executeSQL("UPDATE "
				+ BaseHelper.TABLE_PERSONAL_POKEMON + " SET "
				+ BaseHelper.ORDER + " = " + tempint1 + " WHERE "
				+ BaseHelper.MY_ID + " = " + idDb2);
		StaticClass.dbpoke.executeSQL("UPDATE "
				+ BaseHelper.TABLE_PERSONAL_POKEMON + " SET "
				+ BaseHelper.ORDER + " = " + tempint2 + " WHERE "
				+ BaseHelper.MY_ID + " = " + idDb1);

	}

	public static String getSexAsci(PokemonSex sex) {
		String s = " ";
		if (sex == PokemonSex.MALE) {
			s = "♀";
		} else {
			if (sex == PokemonSex.FEMALE)
				s = "♂";
		}
		return s;
	}

	public static ArrayList<Monster> getTeamMonsters(Context ctx) {
		return getAllPersonaPokemon(ctx, TEAM);
	}

	public static ArrayList<Monster> getBoxMonsters(Context ctx) {
		return getAllPersonaPokemon(ctx, BOX);
	}

	private static ArrayList<Monster> getAllPersonaPokemon(Context ctx,
			int status) {
		String query;
		if (status == TEAM) {
			query = TEAMQUERY;
		} else if (status == BOX) {
			query = BOXQUERY;
		} else {
			return null;
		}

		if (StaticClass.dbpoke == null)
			StaticClass.openBatabaseConection(ctx.getApplicationContext());
		StaticClass.dbpoke.openDataBase();
		Cursor c = StaticClass.dbpoke.dbpoke.rawQuery(query, null);
		int id, dbId, sex, found_x, found_y, level;
		PokemonSex realSex;
		String my_name;
		ArrayList<Monster> mPokemon = new ArrayList<Monster>();
		while (c.moveToNext()) {

			dbId = c.getInt(c.getColumnIndex(BaseHelper.MY_ID));
			id = c.getInt(c.getColumnIndex(BaseHelper.BASE_POKEMON_ID));
			my_name = c.getString(c.getColumnIndex(BaseHelper.MY_NAME));
			sex = c.getInt(c.getColumnIndex(BaseHelper.SEX));
			realSex = intToGender(sex);
			found_x = c.getInt(c.getColumnIndex(BaseHelper.FOUND_X));
			found_y = c.getInt(c.getColumnIndex(BaseHelper.FOUND_Y));

			level = c.getInt(c.getColumnIndex(BaseHelper.LEVEL));

			int seed = c.getInt(c.getColumnIndex(BaseHelper.SEED));

			// TODO remove hardcoded level
			Monster nuovo = new Monster(id, my_name, realSex, found_x, found_y,
					level);
			nuovo.setSeed(seed);
			nuovo.setDbId(dbId);

			mPokemon.add(nuovo);
		}
		c.close();
		StaticClass.dbpoke.close();
		return mPokemon;
	}

	public int getDbId() {
		return dbId;
	}

	public static String getImagUri(int id) {
		if (id < 10)
			return "assets://pkm/pkfrlg00" + id + ".png";
		if (id < 100)
			return "assets://pkm/pkfrlg0" + id + ".png";
		else
			return "assets://pkm/pkfrlg" + id + ".png";
	}

	public static Bitmap getImagBitmap(Context context, int id) {
		String s;
		if (id < 10)
			s = "pkm/pkfrlg00" + id + ".png";
		else {
			if (id < 100)
				s = "pkm/pkfrlg0" + id + ".png";
			else
				s = "pkm/pkfrlg" + id + ".png";
		}

		return StaticClass.getBitmapFromAssats(context, s);
	}

	// ////// FINE STATIC ----- INIZIO CLASSE ISTANZIABILE POKEMON

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o == this)
			ret = true;
		else if (o instanceof Monster) {
			ret = this.id == ((Monster) o).getId();
		} else {
			ret = false;
		}
		return ret;
	}

	public int attack(Monster defender, Move move) {
		Monster atk = this;
		Monster def = defender;

		// TODO if to change about the type of the move (eg. status, special,
		// phisical)
		// damage case
		int finalDamage = 0;
		// double rawDamage;
		int randomNumber = (new Random()).nextInt(26) + 85;
		double effectivness = 1.0;
		double stab = (atk.getFirstType() == move.getType() || atk
				.getSecndType() == move.getType()) ? 1.5 : 1.0;
		int power = move.getPower();
		// double nature = 1.0;
		double additional = 1.0;

		int attack = atk.getAttack();
		int defence = def.getDefence();

		// damage formula
		finalDamage = (int) ((effectivness * randomNumber * stab * additional / 50) * (attack
				* power * 0.02 * (atk.getLevel() / 5 + 1) / defence + 1));

		return finalDamage;
	}

	public void saveOnDatabase() {
		int sex = Monster.genderToInt(this.sex);

		ContentValues c = new ContentValues();

		c.put(BaseHelper.BASE_POKEMON_ID, id);
		c.put(BaseHelper.SEX, sex);
		c.put(BaseHelper.FOUND_X, found_x);
		c.put(BaseHelper.FOUND_Y, found_y);
		c.put(BaseHelper.LEVEL, level);
		c.put(BaseHelper.HPEV, hpEv);
		c.put(BaseHelper.ATKEV, atkEv);
		c.put(BaseHelper.DEFEV, defEv);
		c.put(BaseHelper.SATKEV, sAtkEv);
		c.put(BaseHelper.SDEFEV, sDefEv);
		c.put(BaseHelper.SPDEV, spdEv);
		c.put(BaseHelper.MY_NAME, my_name);

		StaticClass.dbpoke.openDataBase();
		int dbId = (int) StaticClass.dbpoke.dbpoke.insertOrThrow(
				BaseHelper.TABLE_PERSONAL_POKEMON, null, c);
		StaticClass.dbpoke.close();

		StaticClass.dbpoke.executeSQL("UPDATE "
				+ BaseHelper.TABLE_PERSONAL_POKEMON + " SET "
				+ BaseHelper.ORDER + "=" + dbId + " WHERE " + BaseHelper.MY_ID
				+ " = " + dbId);

		// StaticClass.dbpoke.executeSQL(insertPersonalPokemon);

	}

	public void removeFromDatabase() {
		String sql = "DELETE FROM " + BaseHelper.TABLE_PERSONAL_POKEMON
				+ " WHERE " + BaseHelper.MY_ID + "=" + dbId;
		StaticClass.dbpoke.executeSQL(sql);
	}

	// GETTER AND SETTERS

	public String getMy_name() {
		return my_name;
	}

	public int getLevel() {
		return level;
	}

	public static int getRarityFromId(int id) {
		return (RARITY[id - 1]);
	}

	public PokemonSex getSex() {
		return sex;
	}

	public double getFound_x() {
		return found_x;
	}

	public double getFound_y() {
		return found_y;
	}

	public int getHp() {
		return (int) ((((hpIv + 2 * this.baseHp + (hpEv / 4) + 100) * this
				.getLevel()) / 100 + 10));
	}

	public int getAttack() {
		return (int) ((((atkIv + 2 * this.baseAtk + (atkEv / 4)) * this
				.getLevel()) / 100 + 5));
	}

	public int getDefence() {
		return (int) ((((defIv + 2 * this.baseDef + (defEv / 4)) * this
				.getLevel()) / 100 + 5));
	}

	public int getSpecialAttack() {
		return (int) ((((sAtkIv + 2 * this.baseSAtk + (sAtkEv / 4)) * this
				.getLevel()) / 100 + 5));
	}

	public int getSpecialDefence() {
		return (int) ((((sDefIv + 2 * this.baseSDef + (sDefEv / 4)) * this
				.getLevel()) / 100 + 5));
	}

	public int getSpeed() {
		return (int) ((((spdIv + 2 * this.baseSpd + (spdEv / 4)) * this
				.getLevel()) / 100 + 5));
	}

	public int getRarity() {
		return RARITY[this.id - 1];
	}

	public String getName() {
		;
		return name;
	}

	public int getId() {
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

	public void setSeed(int seed) {
		this.ivSeed = seed;
	}

	public void setDbId(int dbId) {
		this.dbId = dbId;
		Random r = new Random(ivSeed);
		hpIv = r.nextInt(32);
		atkIv = r.nextInt(32);
		defIv = r.nextInt(32);
		sAtkIv = r.nextInt(32);
		sDefIv = r.nextInt(32);
		spdIv = r.nextInt(32);
	}

	public static PokemonSex intToGender(int sex) {
		PokemonSex realSex;
		if (sex == 1)
			realSex = PokemonSex.MALE;
		else if (sex == 2)
			realSex = PokemonSex.FEMALE;
		else
			realSex = PokemonSex.GENDERLESS;
		return realSex;
	}

	public static int genderToInt(PokemonSex realSex) {
		int sex = 1;
		if (realSex == PokemonSex.MALE)
			sex = 1;
		else if (realSex == PokemonSex.FEMALE)
			sex = 2;
		else
			sex = 3;
		return sex;
	}
}
