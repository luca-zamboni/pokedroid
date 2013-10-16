package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.utils.SharedPreferencesUtilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseSide extends Activity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_side);
	}
	
	public void chooseSide(View v){
		showConfirmDialog(v.getId()==R.id.choose_zeta);
	}

	private void showConfirmDialog(final boolean bad) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.title_confirm)
				.setMessage(R.string.dialog_sure)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (bad)
									SharedPreferencesUtilities
											.setBad(ChooseSide.this);
								dialog.dismiss();
								startActivity(new Intent(ChooseSide.this,ChoosePokemonActivity.class));
								ChooseSide.this.finish();
							}
						})
				.setNeutralButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
		builder.create().show();
	}
}
