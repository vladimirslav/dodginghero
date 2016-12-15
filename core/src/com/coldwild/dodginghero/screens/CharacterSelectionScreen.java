package com.coldwild.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.coldwild.dodginghero.DodgingHero;
import com.coldwild.dodginghero.logic.GameProgress;
import com.coldwild.dodginghero.logic.objects.CharacterRecord;

/**
 * Created by comrad_gremlin on 12/12/2016.
 */

public class CharacterSelectionScreen extends DefaultScreen {

    Stage uiStage;

    void prepareUi()
    {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.res.gamefont;
        buttonStyle.fontColor = Color.WHITE;

        TextButton startButton = new TextButton("START", buttonStyle);
        startButton.setPosition((uiStage.getWidth() - startButton.getWidth()) / 2,
                uiStage.getHeight() / 6);
        startButton.addListener(new ClickListener()
        {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                game.setScreen(new GameScreen(game));
            }
        });

        uiStage.addActor(startButton);

        Image heroSprite = new Image(
            game.res.playerSprites.get(CharacterRecord.CHARACTERS[GameProgress.currentCharacter].name)
        );

        heroSprite.setPosition((uiStage.getWidth() - heroSprite.getWidth()) / 2,
                (uiStage.getHeight() - heroSprite.getHeight()) / 2);
        uiStage.addActor(heroSprite);

        TextButton nextButton = new TextButton(">>>", buttonStyle);
        nextButton.addListener(new ClickListener()
        {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                GameProgress.currentCharacter += 1;
                if (GameProgress.currentCharacter == CharacterRecord.CHARACTERS.length)
                {
                    GameProgress.currentCharacter = 0;
                }

                uiStage.clear();
                prepareUi();
            }
        });
        nextButton.setPosition(uiStage.getWidth() * 5 / 6 - nextButton.getWidth() /2,
                uiStage.getHeight() / 2);
        uiStage.addActor(nextButton);

        TextButton prevButton = new TextButton("<<<", buttonStyle);
        prevButton.addListener(new ClickListener()
        {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                GameProgress.currentCharacter -= 1;
                if (GameProgress.currentCharacter < 0)
                {
                    GameProgress.currentCharacter = CharacterRecord.CHARACTERS.length - 1;
                }

                uiStage.clear();
                prepareUi();
            }
        });
        prevButton.setPosition(uiStage.getWidth() / 6, uiStage.getHeight() / 2);
        uiStage.addActor(prevButton);
    }

    public CharacterSelectionScreen(DodgingHero _game) {
        super(_game);

        FitViewport viewport = new FitViewport(160, 120);
        uiStage = new Stage(viewport);
        Gdx.input.setInputProcessor(uiStage);
        prepareUi();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void dispose()
    {
        Gdx.input.setInputProcessor(null);
        uiStage.dispose();
        super.dispose();
    }
}
