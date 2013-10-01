package it.pokefundroid.pokedroid.viewUtils;

import it.pokefundroid.pokedroid.AugmentedReality_Activity;
import it.pokefundroid.pokedroid.R;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mPokemonsIDs;

	public ImageAdapter(Context c, List pokemonsIDs) {
		mContext = c;
		this.mPokemonsIDs = pokemonsIDs;
	}

	public int getCount() {
		return this.mPokemonsIDs.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			Resources r = mContext.getResources();
			imageView.setLayoutParams(new GridView.LayoutParams(
					getPixelsFromDPI(r, r.getDimensionPixelSize(R.dimen.pokemon_size)),
					getPixelsFromDPI(r, r.getDimensionPixelSize(R.dimen.pokemon_size))));
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		} else {
			imageView = (ImageView) convertView;
		}
		try {
			imageView.setImageBitmap(BitmapFactory.decodeStream(mContext
					.getAssets().open(
							getPokemonFilename(mPokemonsIDs.get(position)))));
		} catch (IOException e) {
			imageView.setImageResource(R.drawable.creature_6);
		}
		return imageView;
	}

	private String getPokemonFilename(String string) {
		int id = Integer.parseInt(string);
		if (id < 10)
			return "pkm/pkfrlg00" + id + ".png";
		if (id < 100)
			return "pkm/pkfrlg0" + id + ".png";
		else
			return "pkm/pkfrlg" + id + ".png";
	}

	private int getPixelsFromDPI(Resources r, int n) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, n,
				r.getDisplayMetrics());
	}

}
