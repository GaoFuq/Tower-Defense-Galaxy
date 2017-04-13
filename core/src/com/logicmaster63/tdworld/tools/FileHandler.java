package com.logicmaster63.tdworld.tools;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Disposable;
import com.logicmaster63.tdworld.TDWorld;
import com.logicmaster63.tdworld.enemy.Enemy;
import com.logicmaster63.tdworld.map.Curve;
import com.logicmaster63.tdworld.map.Spawn;
import com.logicmaster63.tdworld.map.Strait;
import com.logicmaster63.tdworld.map.Track;
import com.logicmaster63.tdworld.screens.GameScreen;
import com.logicmaster63.tdworld.tower.Tower;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FileHandler {

    private static ArrayList<Disposable> disposables = new ArrayList<Disposable>();

    public static ArrayList<Vector3> loadTrack(BufferedReader data, GameScreen screen) {
        if (data == null)
            return null;
        ArrayList<Track> track = new ArrayList<Track>();
        ArrayList<Vector3> path = new ArrayList<Vector3>();
        try {
            String line = "";
            int parsed[] = Tools.trippleParseInt(data.readLine());
            screen.setSpawnPos(new Vector3(parsed[0], parsed[1], parsed[2]));
            parsed = Tools.trippleParseInt(data.readLine());
            path.add(new Vector3(parsed[0], parsed[1], parsed[2]));
            line = data.readLine();
            while (!line.equals("@")) {
                int speeed = Integer.parseInt(data.readLine());
                if (line.equals("|")) { //Strait
                    line = data.readLine();
                    track.add(new Strait(speeed, new Vector3(Tools.trippleParseInt(line)[0], Tools.trippleParseInt(line)[1], Tools.trippleParseInt(line)[2])));
                } else if (line.equals(")")) { //Curve
                    parsed = Tools.trippleParseInt(data.readLine());
                    Vector3 k0 = new Vector3(parsed[0], parsed[1], parsed[2]);
                    parsed = Tools.trippleParseInt(data.readLine());
                    Vector3 k1 = new Vector3(parsed[0], parsed[1], parsed[2]);
                    parsed = Tools.trippleParseInt(data.readLine());
                    Vector3 k2 = new Vector3(parsed[0], parsed[1], parsed[2]);
                    track.add(new Curve(speeed, k0, k1, k2));
                } else {
                    Gdx.app.log("Error", "Track type '" + line + "'not found");
                    return path;
                }
                line = data.readLine();
            }
            for (Track t : track) {
                path.addAll(t.getPoints(TDWorld.res));
                //System.out.println(t.getPoints(TDWorld.res));
                //System.out.println(path);
            }
            parsed = Tools.trippleParseInt(data.readLine());
            path.add(new Vector3(parsed[0], parsed[1], parsed[2]));
        } catch (IOException e) {
            Gdx.app.log("Error", e.toString());
        }
        return path;
    }

    public static BufferedReader getReader(String path) {
        BufferedReader reader = null;
        if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            reader = new BufferedReader(new InputStreamReader(Gdx.files.internal(path).read()));
        } else {
            try {
                reader = new BufferedReader(new FileReader(path));
            } catch (FileNotFoundException e) {
                Gdx.app.log("Error", e.toString());
            }
        }
        return reader;
    }

    public static HashMap<String, Class<?>> loadTowers(BufferedReader data, String theme, GameScreen screen) {
        if (data == null)
            return null;
        HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();
        ArrayList<String> names = new ArrayList<String>();
        String line;
        try {
            for (int i = 0; i < TDWorld.TOWERS; i++) {
                line = data.readLine();
                classes.put(line, Class.forName("com.logicmaster63.tdworld.tower." + theme + "." + line));
                names.add(line);
            }
        } catch (Exception e) {
            Gdx.app.log("Error", e.toString());
        }
        screen.setTowerNames(names);
        return classes;
    }

    public static HashMap<String, Class<?>> loadEnemies(BufferedReader data, String theme, GameScreen screen) {
        if (data == null)
            return null;
        HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();
        ArrayList<String> names = new ArrayList<String>();
        String line;
        try {
            for (int i = 0; i < TDWorld.ENEMIES; i++) {
                line = data.readLine();
                classes.put(line, Class.forName("com.logicmaster63.tdworld.enemy." + theme + "." + line));
                names.add(line);
            }
        } catch (Exception e) {
            Gdx.app.log("Error", e.toString());
        }
        screen.setEnemyNames(names);
        return classes;
    }

    public static void loadPlanet(BufferedReader data, GameScreen screen) {
        //if(data == null)
        int coords[] = {0, 0, 0};
        if (data == null)
            return;
        try {
            screen.setHasPlanetModel(Boolean.parseBoolean(data.readLine()));
            coords = Tools.trippleParseInt(data.readLine());
            screen.setPlanetName(data.readLine());
        } catch (IOException e) {
            Gdx.app.log("Error", e.toString());
        }
        screen.setPlanetSize(new Vector3(coords[0], coords[1], coords[2]));
    }

    public static void loadDependencies(HashMap<String, Class<?>> classes) {
        int length = classes.size();
        ArrayList<Dependency> dependencies = null;
        ArrayList<Class<?>> classArray = new ArrayList<Class<?>>(classes.values());
        for (int i = 0; i < length; i++) {
            try {
                dependencies = (ArrayList<Dependency>) classArray.get(i).getMethod("getDependencies", null).invoke(null);
                if (dependencies != null)
                    for (Dependency dependency : dependencies) {
                        classes.put(dependency.getName(), dependency.getClazz());
                        Gdx.app.log("Dependency Loaded", dependency.getName() + ".class");
                    }
            } catch (NoSuchMethodException e) {

            } catch (Exception e) {
                Gdx.app.log("Error", "FileHandler:150: " + e.toString());
            }
        }
        System.out.println(classes);
    }

    public static ArrayList<Spawn> loadSpawns(BufferedReader data) {
        if (data == null)
            return null;
        ArrayList<Spawn> spawns = new ArrayList<Spawn>();
        try {
            int spawnNum = Integer.parseInt(data.readLine());
            for (int i = 0; i < spawnNum; i++) {
                int repeatNum = Integer.parseInt(data.readLine());
                String name = data.readLine();
                int delay = Integer.parseInt(data.readLine());
                for (int j = 0; j < repeatNum; j++)
                    spawns.add(new Spawn(name, delay));
            }
        } catch (IOException e) {
            Gdx.app.log("Error", e.toString());
        }
        return spawns;
    }

    public static void addDisposables(Disposable... d) {
        disposables.addAll(Arrays.asList(d));
    }

    public static void dispose() {
        if (disposables != null)
            for (Disposable d : disposables)
                d.dispose();
    }
}
