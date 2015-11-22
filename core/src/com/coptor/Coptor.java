package com.coptor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Coptor extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	FPSLogger fpsLogger;
	OrthographicCamera camera;
	Texture txBg;
	TextureRegion txBelow;
	TextureRegion txAbove;
	float fOffset;
	Animation anPlane;
	float fPlaneAnimTime;
	Vector2 v2vPlane= new Vector2(); // vector 2d of the velocity of the plane
	Vector2 v2posPlane= new Vector2();
	Vector2 v2posPlaneDefault= new Vector2();
	Vector2 v2Gravity= new Vector2();
	private static final Vector2 v2Damping= new Vector2(0.99f,0.99f);
	
	@Override
	public void create () {
		fpsLogger=new FPSLogger();
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		txBg = new Texture("background.jpg");
		txBelow=new TextureRegion(new
				Texture("groundgrass.jpg"));
		txAbove=new TextureRegion(txBelow);

		txAbove.flip(true, true);
		anPlane = new Animation(0.05f, new TextureRegion(new
				Texture("plane1.png")),
				new TextureRegion(new Texture("plane2.png")),
				new TextureRegion(new Texture("plane3.png")),
				new TextureRegion(new Texture("plane4.png")));
		anPlane.setPlayMode(Animation.PlayMode.LOOP);
		resetScene();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		fpsLogger.log();
		updateScene();
		drawScene();
	}

	private void resetScene()
	{
		fOffset=0;
		fPlaneAnimTime=0;
		v2vPlane.set(400, 0);
		v2Gravity.set(0, -4);
		v2posPlaneDefault.set(400-88/2, 240-73/2);
		v2posPlane.set(v2posPlaneDefault.x,
				v2posPlaneDefault.y);
	}

	private void updateScene()
	{
		float fDtime = Gdx.graphics.getDeltaTime();
		fPlaneAnimTime+=fDtime;
		v2vPlane.scl(v2Damping);
		v2vPlane.add(v2Gravity);
		v2posPlane.mulAdd(v2vPlane, fDtime);
		fOffset-=v2posPlane.x-v2posPlaneDefault.x;
		v2posPlane.x=v2posPlaneDefault.x;
		if(fOffset*-1>txBelow.getRegionWidth())
		{
			fOffset=0;
		}
		if(fOffset>0)
		{
			fOffset=-txBelow.getRegionWidth();
		}
	}
	private void drawScene()
	{
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.disableBlending();
		batch.draw(txBg, 0, 0);
		batch.enableBlending();
		batch.draw(txBelow, fOffset, 0);
		batch.draw(txBelow, fOffset +
				txBelow.getRegionWidth(), 0);
		batch.draw(txAbove, fOffset, 480 -
				txAbove.getRegionHeight());
		batch.draw(txAbove, fOffset +
				txAbove.getRegionWidth(), 480 -
				txAbove.getRegionHeight());
		batch.draw(anPlane.getKeyFrame(fPlaneAnimTime), v2posPlane.x,
				v2posPlane.y);
		batch.end();
	}
}
