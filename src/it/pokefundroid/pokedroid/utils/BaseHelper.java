package it.pokefundroid.pokedroid.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "my_pokemon.db";
    private static final int DATABASE_VERSION = 1;
    
    /// questi solo key per i miei pokemon
    public static final String MY_ID = "my_id";
    public static final String NUMBER = "number";
    public static final String MY_NAME = "my_name";
    public static final String SEX = "sex";
    public static final String FOUND_X = "found_coordinate_x";
    public static final String FOUND_Y = "found_coordinate_y";
    
    /// key per i dati dei pokemon in generale
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String RARITY = "rarity";

    
    private static final String CREATE_MY_POKEMON_TABLE = "create table if not exists my_pokemon  " +
    		"(my_id integer primary key autoincrement," +
    		"number integer not null," +
    		"my_name text null," +
    		"sex integer not null," +
    		"found_coordinate_x integer null, " +
    		"found_coordinate_y integer null);";
    
    private static final String CREATE_POKEMON_TABLE = "create table if not exists pokemon " +
    		"(id integer primary key autoincrement," +
    		"name string not null," +
    		"type string not null," +
    		"rarity integer not null);";
    // qua bisognerà piazzarci tutte le altre stats

    // Costruttore
    public BaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Questo metodo viene chiamato durante la creazione del database
    @Override
    public void onCreate(SQLiteDatabase database) {
    	database.execSQL(CREATE_POKEMON_TABLE);
    	database.execSQL(CREATE_MY_POKEMON_TABLE);
    }

    // Questo metodo viene chiamato durante l'upgrade del database, ad esempio quando viene incrementato il numero di versione
    @Override
    public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion ) {
             
    	database.execSQL("DROP TABLE IF EXISTS pokemon");
    	database.execSQL("DROP TABLE IF EXISTS my_pokemon");
        onCreate(database);
             
    }
}