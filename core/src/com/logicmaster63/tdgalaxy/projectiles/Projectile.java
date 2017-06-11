package com.logicmaster63.tdgalaxy.projectiles;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;

public abstract class Projectile extends com.logicmaster63.tdgalaxy.entity.Entity {

    Vector3 velocity;
    boolean isTower;
    protected float age;
    protected float lifetime;

    public Projectile(Vector3 pos, Vector3 velocity, int hp, int health, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<com.logicmaster63.tdgalaxy.entity.Entity> entities, boolean isTemplate, float lifetime) {
        super(pos, hp, health, types, effects, model, shape, world, entities, isTemplate);
        this.isTower = isTower;
        this.velocity = velocity;
    }

    public Projectile(Vector3 pos, Vector3 velocity, int hp, int types, int effects, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<com.logicmaster63.tdgalaxy.entity.Entity> entities, float lifetime) {
        this(pos, velocity, hp, hp, types, effects, model, shape, isTower, world, entities, false, lifetime);
    }

    public Projectile(int hp, int types, ModelInstance model, btCollisionShape shape, boolean isTower, btCollisionWorld world, IntMap<com.logicmaster63.tdgalaxy.entity.Entity> entities, float lifetime) {
        this(Vector3.Zero, Vector3.Zero, hp, hp, types, 0, model, shape, isTower, world, entities, true, lifetime);
    }

    public Projectile(Projectile projectile, Vector3 pos, Vector3 velocity) {
        this(pos, velocity, projectile.hp, projectile.health, projectile.types, projectile.effects, new ModelInstance(projectile.instance), projectile.shape, projectile.isTower, projectile.world, projectile.entities, false, projectile.lifetime);
    }

    public void tick(float delta) {
        tempVector.set(velocity);
        pos.add(tempVector.scl(delta));
        super.tick(delta);
        age += delta;
    }

    @Override
    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer) {
        //pos.add(velocity.x * delta, velocity.y * delta, velocity.z * delta);
        modelBatch.render(instance);
    }

    public abstract void collision(com.logicmaster63.tdgalaxy.entity.Entity entity);

    @Override
    public void dispose() {
        shape.dispose();
    }
}