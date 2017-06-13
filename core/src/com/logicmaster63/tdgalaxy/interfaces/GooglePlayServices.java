package com.logicmaster63.tdgalaxy.interfaces;

public interface GooglePlayServices {

    void signIn();

    void signOut();

    void rateGame();

    boolean isSignedIn();

    void unlockAchievement(String name);

    void showAchievements();

    void resetAchievement(String name);

    void showScore(String name);

    void submitScore(String name, long highScore);
}
