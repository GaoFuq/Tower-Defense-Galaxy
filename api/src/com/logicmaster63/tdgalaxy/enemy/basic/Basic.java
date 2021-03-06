package com.logicmaster63.tdgalaxy.enemy.basic;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.enemy.AttackingEnemy;
import com.logicmaster63.tdgalaxy.tools.Effects;
import com.logicmaster63.tdgalaxy.tools.Source;
import com.logicmaster63.tdgalaxy.tools.Types;
import com.logicmaster63.tdgalaxy.enemy.Enemy;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.tools.Asset;

import java.util.*;

public class Basic extends AttackingEnemy {

    public static final EnumSet<Types> TYPES = EnumSet.of(Types.SHARP);
    public static final int HP = 20;
    public static final float COOLDOWN = 1f;
    public static final int RANGE = 300;
    public static final String ATTACK_ANIMATION = "Spider_Armature|Attack";
    public static final double SPEED = 10;
    public static final Vector3 ATTACK_OFFSET = Vector3.Zero;

    private AnimationController.AnimationListener listener;

    public Basic(Matrix4 transform, double speeed, int hp, int health, int range, float coolDown, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionWorld world, IntMap<Entity> entities, List<Vector3> path, Map<String, Sound> sounds) {
        super(transform, speeed, hp, health, range, coolDown, types, effects, instance, new btCompoundShape(), world, entities, ATTACK_ANIMATION, ATTACK_OFFSET, path, sounds);
        ((btCompoundShape)shape).addChildShape(new Matrix4(new Vector3(0, 30, 0), new Quaternion().setEulerAngles(0, 0, 0), new Vector3(1, 1, 1)), new btBoxShape(new Vector3(75, 30, 90)));
        //System.out.println(getModelInstance().getAnimation("Spider_Armature|walk_ani_vor").id);
        listener = new AnimationController.AnimationListener() {
            @Override
            public void onEnd(AnimationController.AnimationDesc animationDesc) {

            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animationDesc) {

            }
        };
        //animation.setAnimation("Spider_Armature|walk_ani_vor");
        //animation.animate("Spider_Armature|walk_ani_vor", -1);
        //animation.action("Spider_Armature|walk_ani_vor", 0, 1000, -1, 1, listener, 0);
        //animation.animate("Spider_Armature|Attack", 0, 1000, 1, 1, listener, 0);
        //animation.queue("Spider_Armature|walk_ani_vor", 0, 1000, -1, 1, listener, 0);
    }

    public Basic(Matrix4 transform, Map<String, Model> models, btCollisionWorld world, IntMap<Entity> entities, List<Vector3> path, Map<String, Sound> sounds) {
        this(transform, SPEED, HP, HP, RANGE, COOLDOWN, TYPES, EnumSet.noneOf(Effects.class), new ModelInstance(models.get("Basic")), world, entities, path, sounds);
    }

    public static List<Asset> getAssets() {
        return Arrays.asList(new Asset(Source.INTERNAL,"theme/basic/enemy/Basic.g3db", Model.class));
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }
}
