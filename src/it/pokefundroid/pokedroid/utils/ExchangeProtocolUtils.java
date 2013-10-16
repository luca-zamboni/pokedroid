package it.pokefundroid.pokedroid.utils;

import it.pokefundroid.pokedroid.models.Monster;

import org.json.JSONException;
import org.json.JSONObject;

public class ExchangeProtocolUtils {

	// JSON fields
	private static final String COMMAND_FIELD = "command";
	private static final String PKMN_FIELD = "pkmn";
	private static final String NUM_FIELD = "ack";

	// COMMANDS
	public static final String SEND_COMMAND = "send";
	public static final String ACCEPT_COMMAND = "accept";
	public static final String ACK_COMMAND = "ack";

	// TODO move this method inside the pokemon class
	// POKEMON FIELDS
	private static final String PKMN_ID = "id";
	private static final String PKMN_LVL = "lvl";
	private static final String PKMN_SEX = "sex";
	private static final String PKMN_X = "foundX";
	private static final String PKMN_Y = "foundY";
	private static String PKMN_NAME = "name";

	public static Monster convertJSONToMonster(JSONObject jsobj)
			throws JSONException {
		return new Monster(jsobj.getInt(PKMN_ID),
				jsobj.getString(PKMN_NAME), Monster.intToGender(jsobj
						.getInt(PKMN_SEX)), jsobj.getDouble(PKMN_X),
				jsobj.getDouble(PKMN_Y), jsobj.getInt(PKMN_LVL));
	}
	
	public static JSONObject convertMonsterToJSON(Monster p)
			throws JSONException {
		JSONObject out = new JSONObject();
		out.put(PKMN_ID, p.getId());
		out.put(PKMN_NAME, p.getName());
		out.put(PKMN_LVL, p.getLevel());
		out.put(PKMN_SEX, Monster.genderToInt(p.getSex()));
		out.put(PKMN_X, p.getFound_x());
		out.put(PKMN_Y, p.getFound_y());
		return out;
	}
	
	public static String getMessageType(String s) throws JSONException{
		JSONObject json = new JSONObject(s);
		return json.getString(COMMAND_FIELD);
	}
	
	public static Monster readSendJSON(JSONObject jsobj)
			throws JSONException {
		return convertJSONToMonster(jsobj.getJSONObject(PKMN_FIELD));
	}

	public static String createSendMessage(Monster p)
			throws JSONException {
		JSONObject out = new JSONObject();
		out.put(COMMAND_FIELD, SEND_COMMAND);
		out.put(PKMN_FIELD, convertMonsterToJSON(p));
		return out.toString();
	}
	
	public static String createAcceptMessage(int n)
			throws JSONException {
		JSONObject out = new JSONObject();
		out.put(COMMAND_FIELD, ACCEPT_COMMAND);
		out.put(NUM_FIELD, n);
		return out.toString();
	}
	
	public static String createACKMessage(int n)
			throws JSONException {
		JSONObject out = new JSONObject();
		out.put(COMMAND_FIELD, ACK_COMMAND);
		out.put(NUM_FIELD, n);
		return out.toString();
	}
	
	public static boolean verifyACKMessage(JSONObject json,int n)
			throws JSONException {
		return json.getInt(NUM_FIELD)==n;
	}
	
	public static boolean verifyAcceptMessage(JSONObject json,int n)
			throws JSONException {
		return json.getInt(NUM_FIELD)==n;
	}
}
