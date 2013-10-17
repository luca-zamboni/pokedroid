package it.pokefundroid.pokedroid.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseHelper extends SQLiteOpenHelper{

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/it.pokefundroid.pokedroid/databases/";
    private static String DB_NAME = "pokemon";
    public SQLiteDatabase dbpoke;
 
    private final Context myContext;
    
	public static final String TABLE_POKEMON_SPECIES = "pokemon_species";
	public static final String TABLE_GENDERS = "genders";
	
	public static final String TABLE_PERSONAL_POKEMON = "my_pokemon";
	 
    public static final String MY_ID = "my_id";
    public static final String BASE_POKEMON_ID = "id";
    public static final String MY_NAME = "my_name";
    public static final String SEX = "sex";
    public static final String FOUND_X = "found_coordinate_x";
    public static final String FOUND_Y = "found_coordinate_y";
    public static final String SEED = "seed";
    public static final String ORDER = "orderTeam";
    public static final String LEVEL = "level";
    public static final String HPEV = "hpEv";
    public static final String ATKEV = "atkEv";
    public static final String DEFEV = "defEv";
    public static final String SATKEV = "sAtkEv";
    public static final String SDEFEV = "sDefEv";
    public static final String SPDEV = "spdEv";
	
    
    
    ///// create table my pokemon ..... estarna al mega dump scaricato da internet
    private static final String CREATE_MY_POKEMON_TABLE = "create table if not exists "+ TABLE_PERSONAL_POKEMON +
    		" (" +MY_ID+" integer primary key autoincrement," +
    		BASE_POKEMON_ID + " integer not null," +
    		MY_NAME +" text null," +
    		SEX+" integer not null," +
    		FOUND_X + " double null, " +
    		FOUND_Y + " double null," +
			SEED + " int null," +
			LEVEL + " int null," +
			ORDER + " int null," +
			HPEV + " short not null," +
			ATKEV + " short not null," +
			DEFEV + " short not null," +
			SATKEV + " short not null," +
			SDEFEV + " short not null," +
			SPDEV + " short not null" +
					");";
 
    public BaseHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    	try {
			createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	createMyTable();
    }	
    
    public String oneRowOnColumnQuery(String tableName,String column,String where){
    	openDataBase();
    	Cursor c = dbpoke.rawQuery("select "+ column +" from "+ tableName +" where " + where, null);
    	c.moveToFirst();
    	String s = "";
    	s = c.getString(c.getColumnIndex(column));
    	close();
    	return s;
    }
    
    public ArrayList<String> multiRawOnColumnQuery(String tableName,String column,String where){
    	
    	ArrayList<String> array = new ArrayList<String>();
    	openDataBase();
    	Cursor c = dbpoke.rawQuery("select "+ column +" from "+ tableName +" where " + where, null);
    	while(c.moveToNext()){
    		array.add(c.getString(c.getColumnIndex(column)));
    	}
    	close();
    	return array;
    }
    
    public String[] oneRowMultiColumnQuery(String tableName,String[] columns,String where){
    	
    	openDataBase();
    	Cursor c = dbpoke.rawQuery("select "+ columns +" from "+ tableName +" where " + where, null);
    	c.moveToFirst();
    	int count = c.getColumnCount();
    	String[] ret = new String[count];
    	for(int i=0;i<count;i++){
    		ret[i] = c.getString(i);
    	}
    	close();
    	
    	return ret;
    }
    
    public void executeSQL(String query){
    	openDataBase();
    	dbpoke.execSQL(query);
    	close();
    }
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
    	boolean dbExist = checkDataBase();
    	if(dbExist){
    	}else{
        	this.getReadableDatabase();
        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
        	createMyTable();
    	}
    }
    private void createMyTable(){
    	openDataBase();
    	dbpoke.execSQL(CREATE_MY_POKEMON_TABLE);
    	dbpoke.
    	close();
    	
    	
    }
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null; 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(Exception e){
    		//database does't exist yet.
    	}
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
    	String outFileName = DB_PATH + DB_NAME;
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
 
    public SQLiteDatabase openDataBase() throws SQLException{
        String myPath = DB_PATH + DB_NAME;
    	dbpoke = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    	return dbpoke;
    }
 
    @Override
	public synchronized void close() {
    	    if(dbpoke != null)
    		    dbpoke.close();
    	    super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 
}