package com.reck.criminalbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class CriminalBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture danger1;
	Texture danger2;
	Texture danger3;
	float birdX = 0;
	float birdY = 0;
	int gameState = 0;
	float velocity = 0;
	float gravity = 0.7f;
	float enemyVelocity = 5;
	Random random;
	int score = 0;
	int scoreEnemy = 0;
	BitmapFont font;
	BitmapFont font2;

	Circle birdCircle;

	ShapeRenderer shapeRenderer;


	int DangerEnemies = 4;
	float[] enemyX = new float[DangerEnemies];
	float[] enemyOffSet1 = new float[DangerEnemies];
	float[] enemyOffSet2 = new float[DangerEnemies];
	float[] enemyOffSet3 = new float[DangerEnemies];

	float distance = 0;

	Circle[] enemyCircles1;
	Circle[] enemyCircles2;
	Circle[] enemyCircles3;


	@Override
	public void create() {  // oyun açıldığında ne olacağını anlattığımız kısım
		batch = new SpriteBatch();
		background = new Texture("background.png");
		bird = new Texture("bird.png");
		danger1 = new Texture("danger.png");
		danger2 = new Texture("danger.png");
		danger3 = new Texture("danger.png");

		distance = Gdx.graphics.getWidth() / 3;
		random = new Random();


		birdX = Gdx.graphics.getWidth() / 2 - bird.getHeight() / 2;
		birdY = Gdx.graphics.getHeight() / 3;

		shapeRenderer = new ShapeRenderer();


		birdCircle = new Circle();
		enemyCircles1 = new Circle[DangerEnemies];
		enemyCircles2 = new Circle[DangerEnemies];
		enemyCircles3 = new Circle[DangerEnemies];

		font = new BitmapFont();
		font.setColor(Color.SCARLET);
		font.getData().setScale(5);

		font2 = new BitmapFont();
		font2.setColor(Color.RED);
		font2.getData().setScale(6);



		for (int i = 0; i < DangerEnemies; i++) {

			enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 250);
			enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()) - 225;
			enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()) - 200;


			enemyX[i] = Gdx.graphics.getWidth() - danger1.getWidth() / 2 + i * distance;

			enemyCircles1[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();

		}


	}

	@Override
	public void render() { // oyun devam ettiği sürece devamlı çağıracağımız metot

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (gameState == 1) {

			if (enemyX[scoreEnemy] < Gdx.graphics.getWidth() / 2 - bird.getHeight() / 2) {
				score++;

				if (scoreEnemy < DangerEnemies - 1) {
					scoreEnemy++;
				} else {
					scoreEnemy = 0;
				}
			}


			if (Gdx.input.justTouched()) { // tıkladığımızda oyunu başlat

				velocity = -15;
			}

			for (int i = 0; i < DangerEnemies; i++) {

				if (enemyX[i] < - danger1.getWidth()) {
					enemyX[i] = enemyX[i] + DangerEnemies * distance;

					enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 250);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()) - 225;
					enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()) - 200;


				} else {
					enemyX[i] = enemyX[i] - enemyVelocity;
				}


				batch.draw(danger1, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet1[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 11);
				batch.draw(danger2, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 11);
				batch.draw(danger3, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 11);

				enemyCircles1[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, +Gdx.graphics.getHeight() / 2 + enemyOffSet1[i] + Gdx.graphics.getHeight() / 22, Gdx.graphics.getWidth() / 45);
				enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, +Gdx.graphics.getHeight() / 2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 22, Gdx.graphics.getWidth() / 45);
				enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, +Gdx.graphics.getHeight() / 2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 22, Gdx.graphics.getWidth() / 45);

			}

			if (birdY > 0) {
				velocity = velocity + gravity;
				birdY = birdY - velocity;
			} else {
				gameState = 2;
			}


		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) { // tıkladığımızda oyunu başlat
				gameState = 1;
			}
		} else if (gameState == 2) {

			font2.draw(batch, "Game Over! Tap To Play Again!", 450, Gdx.graphics.getHeight() / 2 + 150);

			if (Gdx.input.justTouched()) {
				gameState = 1;
				birdY = Gdx.graphics.getHeight() / 3;

				for (int i = 0; i < DangerEnemies; i++) {

					enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 250);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()) - 225;
					enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()) - 200;


					enemyX[i] = Gdx.graphics.getWidth() - danger1.getWidth() / 2 + i * distance ;

					enemyCircles1[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();

				}

				velocity = 0;
				scoreEnemy = 0;
				score = 0;

			}
		}

			batch.draw(bird, birdX, birdY, Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 11);

			font.draw(batch,String.valueOf("SCORE: "+score),50,250);

			batch.end();

			birdCircle.set(birdX + Gdx.graphics.getWidth() / 30, birdY + Gdx.graphics.getHeight() / 22, Gdx.graphics.getWidth() / 30);

			//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			//shapeRenderer.setColor(Color.BLACK);
			//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);


			for (int i = 0; i < DangerEnemies; i++) {
				//shapeRenderer.circle(enemyX[i]+ Gdx.graphics.getWidth() / 24, +Gdx.graphics.getHeight()/2 + enemyOffSet1[i] + Gdx.graphics.getHeight() / 16,Gdx.graphics.getWidth() / 24);
				//shapeRenderer.circle(enemyX[i]+ Gdx.graphics.getWidth() / 24, +Gdx.graphics.getHeight()/2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 16,Gdx.graphics.getWidth() / 24);
				//shapeRenderer.circle(enemyX[i]+ Gdx.graphics.getWidth() / 24, +Gdx.graphics.getHeight()/2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 16,Gdx.graphics.getWidth() / 24);

				if (Intersector.overlaps(birdCircle, enemyCircles1[i]) || Intersector.overlaps(birdCircle, enemyCircles2[i]) || Intersector.overlaps(birdCircle, enemyCircles3[i])) {
					gameState = 2;
				}

			}
			//shapeRenderer.end();

		}



	@Override
	public void dispose () {

	}
}
