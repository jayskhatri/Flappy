package com.jayskhatri.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture bg;
	Texture[] bird;
	Texture gameover;
	ShapeRenderer shapeRenderer;

	int flapState=0;
	float birdY=0;
	float velocity;
	float gravity=2;
	float gap=450;
	float maxTubeOffset;
	Random randomGenerator;
	Circle birdCircle;
	int Score=0;
	int scoringTube=0;
	BitmapFont font;

	Texture topTube,bottomTube;
	float tubeVelocity=4;
    int numberOfTubes=4;
	float[] tubeX=new float[numberOfTubes];
    float[] tubeOffset=new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;

	int gameState=0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		bg=new Texture("bg.png");
		bird=new Texture[2];
		bird[0]=new Texture("bird.png");
		bird[1]=new Texture("bird2.png");
		gameover=new Texture("gameover.png");

        //shapeRenderer=new ShapeRenderer();
        birdCircle=new Circle();
        topTubeRectangle= new Rectangle[numberOfTubes];
        bottomTubeRectangle=new Rectangle[numberOfTubes];
        font=new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);


		topTube=new Texture("toptube.png");
		bottomTube=new Texture("bottomtube.png");
		maxTubeOffset=Gdx.graphics.getHeight() / 2 -gap/2 -100;
		randomGenerator=new Random();
		distanceBetweenTubes=Gdx.graphics.getWidth() * 3/4;

		startGame();
	}

	public void startGame(){
        birdY=(Gdx.graphics.getHeight() / 2 ) - (bird[0].getHeight() / 2);

        for(int i=0;i<numberOfTubes;i++){
            tubeOffset[i]=(randomGenerator.nextFloat()-0.5f) * (Gdx.graphics.getHeight() - gap -200);
            tubeX[i]=Gdx.graphics.getWidth() / 2 -topTube.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes ;

            topTubeRectangle[i]=new Rectangle();
            bottomTubeRectangle[i]=new Rectangle();
        }
    }

	@Override
	public void render () {

        batch.begin();
        batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(gameState==1) {

            if(tubeX[scoringTube]< (Gdx.graphics.getWidth() / 2) -(bird[flapState].getWidth()*2)){
                Score++;
                Gdx.app.log("Score",String.valueOf(Score));
                if(scoringTube<numberOfTubes-1)
                    scoringTube++;
                else
                    scoringTube=0;
            }
            if(Gdx.input.justTouched()){
                velocity=-30;
            }
            for(int i=0;i<numberOfTubes;i++) {

                if(tubeX[i]< -topTube.getWidth()){
                    tubeX[i]+=numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i]=(randomGenerator.nextFloat()-0.5f) * (Gdx.graphics.getHeight() - gap -200);
                }
                else{
                    tubeX[i] -= tubeVelocity;

                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - topTube.getHeight() + tubeOffset[i]);

                topTubeRectangle[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
                bottomTubeRectangle[i]=new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - topTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

            }

            if(birdY>0) {
                velocity += gravity;
                birdY -= velocity;
            }
            else{
                gameState=2;
            }
        }
        else if(gameState==0)
        {
            if(Gdx.input.justTouched()){
                gameState=1;
            }
        }
        else if(gameState==2){
            batch.draw(gameover,Gdx.graphics.getWidth() / 2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
            if(Gdx.input.justTouched()){
                gameState=1;
                startGame();
                Score=0;
                scoringTube=0;
                velocity=0;
            }
        }

        if (flapState == 0)
            flapState = 1;
        else
            flapState = 0;

        batch.draw(bird[flapState], (Gdx.graphics.getWidth() / 2) - (bird[flapState].getWidth() * 2), birdY);

        font.draw(batch,String.valueOf(Score),100,200);
        batch.end();

        birdCircle.set((Gdx.graphics.getWidth() / 2) - (bird[flapState].getWidth() *1.51f),birdY + bird[flapState].getHeight() / 2,bird[flapState].getWidth()/1.9f);
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);

        //shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

        for(int i=0;i<numberOfTubes;i++){
          //  shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - topTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

            if(Intersector.overlaps(birdCircle,topTubeRectangle[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangle[i])){
                gameState=2;
            }
        }
        //shapeRenderer.end();
	}
	
	/*@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}*/
}
