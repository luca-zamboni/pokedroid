package it.pokefundroid.pokedroid.models;


public class PersonalPokemon extends Pokemon {
	
	public String my_name;
	public int sex;
	public int found_x;
	public int found_y;
	
	public PersonalPokemon(int id,String my_name, int sex, int found_x, int found_y) {
		
		this.my_name = my_name;
		this.sex = sex;
		this.found_x = found_x;
		this.found_y = found_y;
	}
	
	
	
}
