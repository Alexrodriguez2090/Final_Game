package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import edu.lewisu.cs.cpsc41000.common.Boundary;
import edu.lewisu.cs.cpsc41000.common.EdgeHandler;
import edu.lewisu.cs.cpsc41000.common.ImageBasedScreenObject;
import edu.lewisu.cs.cpsc41000.common.ImageBasedScreenObjectDrawer;
import edu.lewisu.cs.cpsc41000.common.MobileImageBasedScreenObject;
import edu.lewisu.cs.cpsc41000.common.labels.ActionLabel;
import edu.lewisu.cs.cpsc41000.common.labels.SoundLabel;
import edu.lewisu.cs.cpsc41000.common.motioncontrollers.Tracker;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture bg, fg, menuImg;

	int WIDTH, HEIGHT;
	OrthographicCamera cam, titleCam;

	int level, scene;

	MobileImageBasedScreenObject character;
	ImageBasedScreenObjectDrawer drawer;
	boolean jumpAvailable;

	int[] levelChange = new int[4];

	ArrayList<Boundary> floor = new ArrayList<Boundary>();
	ArrayList<Boundary> floorl1s1 = new ArrayList<Boundary>();
	ArrayList<Boundary> floorl1s2 = new ArrayList<Boundary>();
	ArrayList<Boundary> walls = new ArrayList<Boundary>();
	EdgeHandler edges;
	Vector2 bounce;
	
	TextureAtlas tatlas;
	Animation<TextureRegion> animation;

	float totalTime;
	int status;
	int sceneNumber;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("chara2/animations.png");
		menuImg = new Texture("levels/level1/scene2.png");
		//tatlas = new TextureAtlas("chara2/running.atlas");
		//animation = new Animation<TextureRegion>(1f/32f, tatlas.getRegions());
		drawer = new ImageBasedScreenObjectDrawer(batch);
		character = new MobileImageBasedScreenObject(img, 50, 900, false);

		int[] idleSequence = {0,0,8,0,9,0,10,0,11,0,12,0,13,0,14,0,15,0,1,0,2,0,3,0,4,0,5,0,6,0,7,0};
		int[] runSequence = {9,1,20,1,21,1,22,1,23,1,24,1,25,1,26,1,27,1,28,1,10,0,11,0,12,1,13,1,14,1,15,1,16,1,17,1,18,1,19,1};
		int[] jumpSequence = {16,0,27,0,1,1,3,1,4,1,5,1,6,1,7,1,8,1,17,0,18,0,19,0,20,0,21,0,22,0,23,0,24,0,25,0,26,0,28,0,29,0,30,0,31,0,32,0,33,0,34,0,35,0,36,0};
		character.setAnimationParameters(54, 59, idleSequence, runSequence, jumpSequence, .1f);
		character.setMaxSpeed(250);
		character.setAcceleration(800);
		character.setDeceleration(600);
		jumpAvailable = true;

		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		level = 0; //Main menu
		titleCam = new OrthographicCamera(WIDTH,HEIGHT);
		titleCam.translate(WIDTH/2,HEIGHT/2);
		titleCam.update();

		bg = new Texture("levels/level1/scene1.png");
		fg = new Texture("levels/level1/scene1fore.png");

		cam = new OrthographicCamera(WIDTH,HEIGHT);
		cam.translate(WIDTH/2, HEIGHT/2);
		cam.update();
		batch.setProjectionMatrix(cam.combined);

		//Level 1 Scene 1
		//floorl1s1.add(new Boundary(20, 350, 140, 348));
		//floorl1s1.add(new Boundary(233, 306, 640, 374)); //Add wall
		//floorl1s1.add(new Boundary(205, 1, 640, 14));
		//floorl1s1.add(new Boundary(0, 1, 204, 3));
		//walls.add(new Boundary(233, 108, 640, 175));
		//floor.add(new Boundary(0, 150, 50, 160));

		//Level 1 Scene 2
		//bg = new Texture("scenes/scene2.png");
		//fg = null;

		sceneNumber = 1;
		edges = new EdgeHandler(character, cam, batch);
		loadScene(1, sceneNumber);
	}

	public void mainMenu() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			level = 1;
		} else {
			batch.setProjectionMatrix(titleCam.combined);
			batch.begin();
			batch.draw(menuImg, 0, 0);
			batch.end();
		}
	}

	public void loadScene(int level, int scene) {
		bg = new Texture(String.format("levels/level%d/scene%d.png", level, scene));
		fg = new Texture(String.format("levels/level%d/scene%dfore.png", level, scene));

		floor.clear();
		FileHandle handleFloor = Gdx.files.local(String.format("levels/level%d/floor%d.txt", level, scene));
		String textFloor = handleFloor.readString();
		String[] floorBounds = textFloor.split("\n");

		for (String bString : floorBounds) {
			String[] arrayToChange = bString.trim().split(" ");

			Integer[] bInt = new Integer[4];
			int i = 0;
			for (String number : arrayToChange) {
				bInt[i] = Integer.parseInt(number);
				i += 1;
			}
			floor.add(new Boundary(bInt[0], bInt[1], bInt[2], bInt[3]));
		}

		walls.clear();
		FileHandle handleWalls = Gdx.files.local(String.format("levels/level%d/walls%d.txt", level, scene));
		String textWalls = handleWalls.readString();
		String[] wallBounds = textWalls.split("\n");

		for (String bString : wallBounds) {
			String[] arrayToChange = bString.trim().split(" ");

			Integer[] bInt = new Integer[4];
			int i = 0;
			for (String number : arrayToChange) {
				bInt[i] = Integer.parseInt(number);
				i += 1;
			}
			walls.add(new Boundary(bInt[0], bInt[1], bInt[2], bInt[3]));
		}

		
		FileHandle levelChangeHandler = Gdx.files.local(String.format("levels/level%d/change%d.txt", level, scene));
		String textChange = levelChangeHandler.readString();
		String[] levelChangeString = textChange.split(" ");
		int i = 0;
		for (String coord : levelChangeString) {
			levelChange[i] = Integer.parseInt(coord);
			i++;
		}
	}
	public void playing() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float dt = Gdx.graphics.getDeltaTime();
		totalTime += dt;

		if (Gdx.input.isKeyPressed(Keys.SPACE) && jumpAvailable) {
			character.accelerateAtAngle(90);
			character.startJump();
			jumpAvailable = false;
		}
		if (Gdx.input.isKeyPressed(Keys.W)) { //Debug code
			character.accelerateAtAngle(90);
			character.startJump();
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			if (!character.getFlipX()) {
				character.flipX();
			}
			character.accelerateAtAngle(180);
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			character.accelerateAtAngle(270);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			if (character.getFlipX()) {
				character.flipX();
			}
			character.accelerateAtAngle(0);
		}
		character.applyPhysics(dt);
		edges.enforceEdges();

		status = 2;
		for (Boundary platform : floor) {
			if (character.overlaps(platform)) {
				character.rebound(character.preventOverlap(platform).angle(),0f);
				jumpAvailable = true;
				if (character.isMoving()) {
					status = 1;
				} else {
					status = 0;
				}
			}
		}

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(bg, 0, 0);
		//batch.draw(animation.getKeyFrame(totalTime, true), 0, 0);
		//batch.draw(img, 0, 0);
		drawer.draw(character, status);
		batch.draw(fg, 0, 0);
		batch.end();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float dt = Gdx.graphics.getDeltaTime();
		totalTime += dt;

		if (Gdx.input.isKeyPressed(Keys.SPACE) && jumpAvailable) {
			character.accelerateAtAngle(90);
			character.startJump();
			jumpAvailable = false;
		}
		if (Gdx.input.isKeyPressed(Keys.W)) { //Debug code
			character.accelerateAtAngle(90);
			character.startJump();
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			if (!character.getFlipX()) {
				character.flipX();
			}
			character.accelerateAtAngle(180);
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			character.accelerateAtAngle(270);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			if (character.getFlipX()) {
				character.flipX();
			}
			character.accelerateAtAngle(0);
		}
		character.applyPhysics(dt);
		edges.enforceEdges();
		
		status = 2;
		for (Boundary platform : floor) {
			if (character.overlaps(platform)) {
				character.rebound(character.preventOverlap(platform).angle(),0f);
				jumpAvailable = true;
				if (character.isMoving()) {
					status = 1;
				} else {
					status = 0;
				}
			}
		}
		for (Boundary wall : walls) {
			if (character.overlaps(wall)) {
				character.rebound(character.preventOverlap(wall).angle(),0f);
				if (character.isMoving()) {
					status = 1;
				} else {
					status = 0;
				}
			}
		}
		if (character.getXPos() == levelChange[0] && character.getYPos() == levelChange[1]) {
			character.setXPos(levelChange[2]);
			character.setYPos(levelChange[3]);
			sceneNumber++;
			loadScene(1,sceneNumber);
		}
		System.out.printf("%f %f\n", character.getXPos(), character.getYPos());

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(bg, 0, 0);
		//batch.draw(animation.getKeyFrame(totalTime, true), 0, 0);
		//batch.draw(img, 0, 0);
		drawer.draw(character, status);
		batch.draw(fg, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
