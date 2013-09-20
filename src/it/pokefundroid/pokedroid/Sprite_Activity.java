package it.pokefundroid.pokedroid;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color; 

import android.hardware.SensorEventListener;

public class Sprite_Activity extends BaseGameActivity {
	 
	private static final int CAMERA_WIDTH = 320;
	 private static final int CAMERA_HEIGHT = 480;
	 
	 private Camera    m_Camera;
	 private Scene    m_Scene;
	 
	 private BitmapTextureAtlas texBanana;
	 private TiledTextureRegion regBanana;
	 private AnimatedSprite  sprBanana;
	 
	 private static int   SPR_COLUMN  = 14;
	 private static int   SPR_ROWS  = 100;
	 
	 @Override
	 public EngineOptions onCreateEngineOptions()
	 {
	  m_Camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	  EngineOptions en = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
	    CAMERA_WIDTH, CAMERA_HEIGHT), m_Camera);
	 
	  return en;
	 }
	 
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx");
		  texBanana = new BitmapTextureAtlas(this.getTextureManager(), 992, 3696, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		  regBanana = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(texBanana, this,
		    "sprite_pokemon.png", 12, 12, SPR_COLUMN, SPR_ROWS);
		  texBanana.load();
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		  m_Scene = new Scene();
		  m_Scene.setBackground(new Background(Color.WHITE));
		 
		  sprBanana = new AnimatedSprite(0, 0, regBanana, this.getVertexBufferObjectManager());
		  m_Scene.attachChild(sprBanana); 
		 
		  sprBanana.animate(100);
		 
		  pOnCreateSceneCallback.onCreateSceneFinished(m_Scene);
		
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
