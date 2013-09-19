package it.pokefundroid.pokedroid;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import android.hardware.SensorEventListener;
import android.view.Display;

public class Sprite_Activity extends BaseGameActivity {

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private Camera m_Camera;
	private Scene m_Scene;

	private BitmapTextureAtlas memArea;
	private TextureRegion img;
	private Sprite sprBanana;

	private static int SPR_COLUMN = 14;
	private static int SPR_ROWS = 100;

	@Override
	public EngineOptions onCreateEngineOptions() {
		m_Camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		final Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int heigth = display.getHeight();
		
		EngineOptions en = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(
						width, heigth), m_Camera);

		return en;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		memArea = new BitmapTextureAtlas(getTextureManager(), 1024, 65536, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		img = BitmapTextureAtlasTextureRegionFactory.createFromAsset(memArea, this, "sprite_pokemon.png", SPR_COLUMN, SPR_ROWS);
		//this.mEngine.getTextureManager().loadTexture(memArea);
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		this.m_Scene = new Scene();
		this.m_Scene.setBackground(new Background(Color.BLUE));
		pOnCreateSceneCallback.onCreateSceneFinished(m_Scene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		sprBanana = new Sprite(0, 0, img, this.getVertexBufferObjectManager());
		this.m_Scene.attachChild(sprBanana);
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}
