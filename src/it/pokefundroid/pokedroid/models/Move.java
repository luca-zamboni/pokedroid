package it.pokefundroid.pokedroid.models;

import it.pokefundroid.pokedroid.utils.BaseHelper;
import it.pokefundroid.pokedroid.utils.StaticClass;
import android.widget.BaseAdapter;

public class Move {
	
	private int id;
	private String identifier;
	private int type;
	private short power;
    private short pp;
    private short accuracy;
    private int priority;
    private short targets;
    private int damageClass;
    private int effectId;
    private int effectChance;
    
    public Move(int id) {
    	this .id = id;
    	this.identifier = StaticClass.dbpoke.oneRowOnColumnQuery("moves", "identifier", "id="+id);
    	this.type = Integer.parseInt(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "type_id", "id="+id));
    	this.power = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "power", "id="+id));
    	this.pp = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "pp", "id="+id));
    	this.accuracy = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "accuracy", "id="+id));
    	this.priority = Integer.parseInt(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "priority", "id="+id));
    	this.targets = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "target_id", "id="+id));
    	this.damageClass = Integer.parseInt(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "damage_class_id", "id="+id));
    	this.effectId = Integer.parseInt(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "effect_id", "id="+id));
    	this.effectChance = Integer.parseInt(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "effect_chance", "id="+id));
    	
    }
    
    public Move(String identifier) {
    	this.identifier = identifier;
    	this .id = Integer.parseInt(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "id", "identifier="+identifier));
    	this.type = Integer.parseInt(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "type_id", "identifier="+identifier));
    	this.power = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "power", "identifier="+identifier));
    	this.pp = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "pp", "identifier="+identifier));
    	this.accuracy = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "accuracy", "identifier="+identifier));
    	this.priority = Integer.parseInt(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "priority", "identifier="+identifier));
    	this.targets = Short.parseShort(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "target_id", "identifier="+identifier));
    	this.damageClass = Integer.parseInt(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "damage_class_id", "identifier="+identifier));
    	this.effectId = Integer.parseInt(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "effect_id", "identifier="+identifier));
    	this.effectChance = Integer.parseInt(StaticClass.dbpoke.oneRowOnColumnQuery("moves", "effect_chance", "identifier="+identifier));
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public short getPower() {
		return power;
	}

	public void setPower(short power) {
		this.power = power;
	}

	public short getPp() {
		return pp;
	}

	public void setPp(short pp) {
		this.pp = pp;
	}

	public short getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(short accuracy) {
		this.accuracy = accuracy;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public short getTargets() {
		return targets;
	}

	public void setTargets(short targets) {
		this.targets = targets;
	}

	public int getDamageClass() {
		return damageClass;
	}

	public void setDamageClass(int damageClass) {
		this.damageClass = damageClass;
	}

	public int getEffectId() {
		return effectId;
	}

	public void setEffectId(int effectId) {
		this.effectId = effectId;
	}

	public int getEffectChance() {
		return effectChance;
	}

	public void setEffectChance(int effectChance) {
		this.effectChance = effectChance;
	}

	//TODO complete this class
	
}
