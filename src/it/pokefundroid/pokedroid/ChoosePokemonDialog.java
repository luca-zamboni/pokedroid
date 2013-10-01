package it.pokefundroid.pokedroid;

import java.util.ArrayList;
import java.util.List;

import it.pokefundroid.pokedroid.viewUtils.ImageAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

public class ChoosePokemonDialog extends DialogFragment {

	private static final String POKEMON_IDS_LIST_KEY = "pokemonidslist";

	public static ChoosePokemonDialog newIstance(ArrayList<String> pokemonIds) {
		Bundle b = new Bundle();
		b.putStringArrayList(POKEMON_IDS_LIST_KEY, pokemonIds);
		ChoosePokemonDialog out = new ChoosePokemonDialog();
		out.setArguments(b);
		return out;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.dialog_choosepokemon, null, false);
		GridView gv = (GridView) v.findViewById(R.id.dialog_pokemongridview);
		if (savedInstanceState == null) {
			savedInstanceState = getArguments();
		}
		gv.setAdapter(new ImageAdapter(getActivity(), savedInstanceState
				.getStringArrayList(POKEMON_IDS_LIST_KEY)));

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.choose_pokemon_title).setView(v);
		return builder.create();
	}

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		arg0 = getArguments();
		super.onSaveInstanceState(arg0);
	}

}
