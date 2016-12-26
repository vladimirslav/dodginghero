package com.coldwild.dodginghero.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.coldwild.dodginghero.DodgingHero;
import com.coldwild.dodginghero.logic.GameProgress;
import com.coldwild.dodginghero.logic.objects.CharacterRecord;

/**
 * Created by comrad_gremlin on 12/12/2016.
 */

public class CharacterSelectionScreen extends DefaultScreen {

    Stage uiStage;

    private Label prepareStatLabel(String text, float x, float y, Label.LabelStyle textStyle)
    {
        Label lbl = new Label(text, textStyle);
        lbl.setAlignment(Align.left);
        lbl.setPosition(x, y);
        uiStage.addActor(lbl);
        return lbl;
    }

    void prepareUi()
    {
        uiStage.clear();

        Label.LabelStyle textStyle = new Label.LabelStyle(game.res.gamefont, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.res.gamefont;
        buttonStyle.fontColor = Color.WHITE;

        if (GameProgress.levels[GameProgress.currentCharacter] == 0) // char is locked
        {
            TextButton upgradeBtn = new TextButton("Unlock(1000 Gold)", buttonStyle);
            upgradeBtn.setPosition((uiStage.getWidth() - upgradeBtn.getWidth()) / 2,
                    uiStage.getHeight() / 6);
            upgradeBtn.addListener(new ClickListener()
            {
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    if (GameProgress.currentGold >= GameProgress.CHARACTER_PRICE)
                    {
                        GameProgress.currentGold -= GameProgress.CHARACTER_PRICE;
                        GameProgress.levels[GameProgress.currentCharacter] = 1;
                        prepareUi();
                    }
                }
            });

            uiStage.addActor(upgradeBtn);
        }
        else
        {
            TextButton startButton = new TextButton("START", buttonStyle);
            startButton.setPosition((uiStage.getWidth() - startButton.getWidth()) / 2,
                    uiStage.getHeight() * 5 / 6);
            startButton.addListener(new ClickListener()
            {
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    dispose();
                    game.setScreen(new GameScreen(game));
                }
            });
            uiStage.addActor(startButton);

            TextButton upgradeBtn = new TextButton("LvlUp(" +
                    GameProgress.getNextUpgradeCost(GameProgress.currentCharacter) + ")",
                    buttonStyle);
            upgradeBtn.setPosition((uiStage.getWidth() - upgradeBtn.getWidth()) / 2,
                    uiStage.getHeight() / 6);
            upgradeBtn.addListener(new ClickListener()
            {
                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    if (GameProgress.currentGold >= GameProgress.getNextUpgradeCost(GameProgress.currentCharacter))
                    {
                        GameProgress.currentGold -= GameProgress.getNextUpgradeCost(GameProgress.currentCharacter);
                        GameProgress.levels[GameProgress.currentCharacter] += 1;
                        prepareUi();
                    }
                }
            });
            uiStage.addActor(upgradeBtn);
        }

        Image heroSprite = new Image(
            game.res.playerSprites.get(CharacterRecord.CHARACTERS[GameProgress.currentCharacter].name)
        );

        heroSprite.setPosition((uiStage.getWidth() - heroSprite.getWidth()) / 4,
                (uiStage.getHeight() - heroSprite.getHeight()) / 2);
        uiStage.addActor(heroSprite);

        Label stat = prepareStatLabel("DMG:" + GameProgress.getPlayerDamage(),
                uiStage.getWidth() / 2,
                heroSprite.getY() + heroSprite.getHeight(),
                textStyle);

        stat = prepareStatLabel("HP:" + GameProgress.getPlayerMaxHp(),
                uiStage.getWidth() / 2,
                stat.getY() - 10,
                textStyle);

        stat = prepareStatLabel("HEAL:" + GameProgress.getPlayerHealthRestored(),
                uiStage.getWidth() / 2,
                stat.getY() - 10,
                textStyle);

        prepareStatLabel("BNS:" + GameProgress.getBonusReductionValue(),
                uiStage.getWidth() / 2,
                stat.getY() - 10,
                textStyle);

        int lvl = GameProgress.levels[GameProgress.currentCharacter];
        Label statusText = new Label(lvl > 0 ? "LVL: " + lvl : "LOCKED", textStyle);
        statusText.setPosition(heroSprite.getX() + (heroSprite.getWidth() - statusText.getWidth()) / 2,
                heroSprite.getY() - statusText.getHeight() - 5);
        uiStage.addActor(statusText);

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

                prepareUi();
            }
        });
        nextButton.setPosition(uiStage.getWidth() * 5 / 6 - nextButton.getWidth() /2,
                uiStage.getHeight() * 5 / 6);
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

                prepareUi();
            }
        });
        prevButton.setPosition(uiStage.getWidth() / 6, uiStage.getHeight() * 5 / 6);
        uiStage.addActor(prevButton);

        // draw the image
        Image coinImage = new Image(game.res.coinBonus);
        coinImage.setPosition(1, 1);
        uiStage.addActor(coinImage);

        // amount of coins
        Label coinAmntLbl = new Label("" + GameProgress.currentGold, textStyle);

        coinAmntLbl.setPosition(coinImage.getX() + coinImage.getWidth() + 3,
                coinImage.getY() + (coinImage.getHeight() - coinAmntLbl.getHeight()) / 2);
        uiStage.addActor(coinAmntLbl);
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

    @Override
    public void resize(int w, int h)
    {
        super.resize(w, h);
        uiStage.getViewport().update(w, h, true);
    }
}
