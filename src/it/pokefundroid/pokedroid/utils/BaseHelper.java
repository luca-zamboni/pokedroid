package it.pokefundroid.pokedroid.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseHelper extends SQLiteOpenHelper{

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/it.pokefundroid.pokedroid/databases/";
    private static String DB_NAME = "pokemon";
    public SQLiteDatabase dbpoke;
 
    private final Context myContext;
	public static final String POKEMON_SPECIES = "pokemon_species";
	public static final String GENDERS = "genders";
	
	
	public static final String TABLE_PERSONAL_POKEMON = "my_pokemon";
	 
    public static final String MY_ID = "my_id";
    public static final String BASE_POKEMON_ID = "id";
    public static final String MY_NAME = "my_name";
    public static final String SEX = "sex";
    public static final String FOUND_X = "found_coordinate_x";
    public static final String FOUND_Y = "found_coordinate_y";
    
    
    ///// create table my pokemon ..... estarna al mega dump scaricato da internet
    private static final String CREATE_MY_POKEMON_TABLE = "create table if not exists "+ TABLE_PERSONAL_POKEMON +
    		" (my_id integer primary key autoincrement," +
    		"id integer not null," +
    		"my_name text null," +
    		"sex integer not null," +
    		"found_coordinate_x integer null, " +
    		"found_coordinate_y integer null);";
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public BaseHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    	try {
			createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	openDataBase();
    }	
    
    public String oneRowOnColumnQuery(String tableName,String columns,String where){
    	openDataBase();
    	Cursor c = dbpoke.rawQuery("select "+ columns +" from "+ tableName +" where " + where, null);
    	c.moveToFirst();
    	String s = c.getString(c.getColumnIndex(columns));
    	close();
    	return s;
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
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
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
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
    	Log.e("", "lL");
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public SQLiteDatabase openDataBase() throws SQLException{
 
    	//Open the database
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
 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
 
}