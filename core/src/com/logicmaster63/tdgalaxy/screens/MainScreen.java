package com.logicmaster63.tdgalaxy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.Constants;
import com.logicmaster63.tdgalaxy.interfaces.CameraRenderer;

public class MainScreen extends TDScreen implements CameraRenderer {

    private Button signInButton, signOutButton;
    private Texture background;

    private SettingsScreen settingsScreen;
    private CampaignScreen campaignScreen;
    private GameScreen gameScreen;

    public MainScreen(TDGalaxy game) {
        super(game);
        settingsScreen = new SettingsScreen(game);
        campaignScreen = new CampaignScreen(game);
        gameScreen = new GameScreen(game, 0, "basic");
    }

    @Override
    public void show () {
        super.show();

        background = new Texture("theme/basic/ui/Window.png");

        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.getFonts().get("moonhouse64");
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(background));

        //Sign in button
        signInButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/SignIn.png"))));
        signInButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.onlineServices != null)
                    TDGalaxy.onlineServices.signIn();
            }
        });
        signOutButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/SignOut.png"))));
        signOutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.onlineServices != null)
                    TDGalaxy.onlineServices.signOut();
                TDGalaxy.preferences.changePref("autoSignIn", false);
            }
        });
        signOutButton.setVisible(false);
        Stack signInStack = new Stack(signInButton, signOutButton);
        signInStack.setBounds(100, viewport.getWorldHeight() - 250, 600, 150);
        stage.addActor(signInStack);

        //Settings button
        ImageButton settingsButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("theme/basic/ui/SettingsButton.png"))));
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(settingsScreen);
            }
        });
        settingsButton.setPosition(viewport.getWorldWidth() - settingsButton.getWidth() - 100, viewport.getWorldHeight() - settingsButton.getHeight() - 100);
        stage.addActor(settingsButton);

        Table table = new Table();
        table.setBounds(0, 120, viewport.getWorldWidth(), viewport.getWorldHeight() - 120);

        //Play game button
        TextButton playButton = new TextButton("Play Game", textButtonStyle);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(game.getGameAssets() != null)
                    game.setScreen(gameScreen);
                //else
                    //game.loadExternalAssets();
            }
        });
        table.add(playButton);

        //Editor button
        TextButton editButton = new TextButton("Editor", textButtonStyle);
        editButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //game.setScreen(new SpectateScreen(game));
                //game.setScreen(new LevelSelectScreen(game, 0));
                game.setScreen(new EditorScreen(game));
            }
        });
        table.add(editButton);

        table.row();

        //Show achievements button
        TextButton achievementShowButton = new TextButton("Show Achievements", textButtonStyle);
        achievementShowButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.onlineServices != null)
                    TDGalaxy.onlineServices.showAchievements();
            }
        });
        table.add(achievementShowButton);

        //Rate game button
        TextButton rateButton = new TextButton("Rate game", textButtonStyle);
        rateButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.onlineServices != null)
                    TDGalaxy.onlineServices.rateGame();
            }
        });
        table.add(rateButton);

        table.row();

        //Show video add button
        TextButton rewardVideo = new TextButton("Free Money", textButtonStyle);
        rewardVideo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(TDGalaxy.onlineServices != null)
                    TDGalaxy.onlineServices.showVideoAd();
                //TDGalaxy.onlineServices.resetAchievement(Constants.ACHIEVEMENT_NO_LIFE);
            }
        });
        table.add(rewardVideo);

        //Campaign button
        if(TDGalaxy.preferences.isDebug()) {
            final TextButton debugButton = new TextButton("Campaign", textButtonStyle);
            debugButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.setScreen(campaignScreen);
                }
            });
            table.add(debugButton);
        }

        table.center().bottom();
        stage.addActor(table);

        if(game.getGameAssets() == null);
            //TDGalaxy.dialogs.needAssets.build().show();
    }

    @Override
    public void renderForCamera(Camera camera) {
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        game.getFonts().get("moonhouse64").draw(spriteBatch, "$" + TDGalaxy.preferences.getMoney(), viewport.getWorldWidth() / 2 - 4, viewport.getWorldHeight() - 30);
        spriteBatch.end();
    }

    @Override
    public void render (float delta) {
        signOutButton.setVisible(TDGalaxy.onlineServices.isSignedIn());
        signInButton.setVisible(!TDGalaxy.onlineServices.isSignedIn());

        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
    }
}