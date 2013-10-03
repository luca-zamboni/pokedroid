package it.pokefundroid.pokedroid.utils;

import it.pokefundroid.pokedroid.models.Move;
import it.pokefundroid.pokedroid.models.PersonalPokemon;
import it.pokefundroid.pokedroid.models.Pokemon;

import java.util.Random;

public class CombatUtils {
	
	//i don't see any other method that sould be put here.... maybe this method can be moved to another class
	public static int attack(Pokemon attacker, Pokemon defender, Move move) {
		PersonalPokemon atk = (PersonalPokemon) attacker;
		PersonalPokemon def = (PersonalPokemon) defender;
		
		int finalDamage = 0;
		double rawDamage;
		int randomNumber = (new Random()).nextInt(26)+85;
		double effectivness = 1.0;
		double stab = (atk.getFirstType()==move.getType() || atk.getSecndType() == move.getType()) ? 1.5 : 1.0;
		int power = move.getPower();
		double nature = 1.0;
		double additional = 1.0;
		int attack = (int) ((((15+2*atk.getBaseAtk()+0/4)*atk.getLevel())/100+5)* nature);
		int defence= (int) ((((15+2*def.getBaseDef()+0/4)*def.getLevel())/100+5)* nature);
		
		finalDamage = (int) ((effectivness*randomNumber*stab*additional/50)*(attack*power*0.02*
				(atk.getLevel()/5+1)/defence+1));
		
		return finalDamage;
	}

}
