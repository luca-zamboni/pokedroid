package it.pokefundroid.pokedroid.viewUtils;

import it.pokefundroid.pokedroid.models.PersonalPokemon;
import it.pokefundroid.pokedroid.models.PersonalPokemon.PokemonSex;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableMonster implements Parcelable{
	private String id;
	private String mName;
	private double mFoundX;
	private double mFoundY;
	private PokemonSex mSex;
	private int mLevel;
	
	
	public ParcelableMonster(String id,String name,
			double foundX, double foundY, PokemonSex sex, int level) {
		super();
		this.id = id;
		this.mName = name;
		this.mFoundX = foundX;
		this.mFoundY = foundY;
		this.mSex = sex;
		this.mLevel = level;
	}


	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getName() {
		return mName;
	}


	public void setName(String mName) {
		this.mName = mName;
	}


	public double getFoundX() {
		return mFoundX;
	}


	public void setFoundX(double mFoundX) {
		this.mFoundX = mFoundX;
	}


	public double getFoundY() {
		return mFoundY;
	}


	public void setFoundY(double mFoundY) {
		this.mFoundY = mFoundY;
	}


	public int getLevel() {
		return mLevel;
	}


	public void setLevel(int mLevel) {
		this.mLevel = mLevel;
	}

	public PokemonSex getSex() {
		return mSex;
	}


	public void setSex(PokemonSex mSex) {
		this.mSex = mSex;
	}


	private ParcelableMonster(Parcel in) {
		mName = in.readString();
		id=in.readString();
		mFoundX=in.readDouble();
		mFoundY=in.readDouble();
		mLevel = in.readInt();
		mSex = PersonalPokemon.intToGender(in.readInt());
	}
	
	public static final Parcelable.Creator<ParcelableMonster> CREATOR = new Parcelable.Creator<ParcelableMonster>() {
		public ParcelableMonster createFromParcel(Parcel in) {
             return new ParcelableMonster(in);
         }

		@Override
		public ParcelableMonster[] newArray(int size) {
			return new ParcelableMonster[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeString(id);
		dest.writeDouble(mFoundX);
		dest.writeDouble(mFoundY);
		dest.writeInt(mLevel);
		int sex = PersonalPokemon.genderToInt(mSex);
		dest.writeInt(sex);
	}
	
	public PersonalPokemon toPersonalPokemon(){
		return new PersonalPokemon(Integer.parseInt(id), mName, mSex, mFoundX, mFoundY, mLevel);
	}
	
}
