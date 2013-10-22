package it.pokefundroid.pokedroid;

import it.pokefundroid.pokedroid.models.Monster;
import it.pokefundroid.pokedroid.utils.LocationUtils;
import it.pokefundroid.pokedroid.utils.StaticClass;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_activity extends Activity {

	private TextView mText;
	private ImageView mImg;
	private LocationUtils mLocationUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		
		mImg = (ImageView) findViewById(R.id.splash_pokeball);
		mImg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
		mText = (TextView) findViewById(R.id.text);
		
		// Connessionae database all' inizio del progrmma nn cancellare
		StaticClass.openBatabaseConection(getApplicationContext());
		StaticClass.sTeam = Monster.getTeamMonsters(this);
		startActivity(new Intent(Splash_activity.this, Menu_Activity.class));
		this.finish();
	}


	public void setText(String m) {
		mText = (TextView) findViewById(R.id.text);
		mText.setText(m);
	}

}
