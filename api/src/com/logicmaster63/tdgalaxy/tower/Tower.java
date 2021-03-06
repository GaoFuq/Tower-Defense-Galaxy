package com.logicmaster63.tdgalaxy.tower;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.tools.Effects;
import com.logicmaster63.tdgalaxy.tools.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;

public abstract class Tower extends Entity {

    public Tower(Matrix4 transform, int hp, int health, int range, float coolDown, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities, String attackAnimation, Vector3 attackOffset, Map<String, Sound> sounds) {
        super(transform, hp, health, types, effects, instance, shape, world, entities, sounds);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
