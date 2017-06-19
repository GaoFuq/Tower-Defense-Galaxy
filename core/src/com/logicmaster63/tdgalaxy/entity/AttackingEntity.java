package com.logicmaster63.tdgalaxy.entity;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.enemy.Enemy;
import com.logicmaster63.tdgalaxy.constants.TargetMode;
import com.logicmaster63.tdgalaxy.tools.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public abstract class AttackingEntity extends Entity {

    protected float coolDown, coolTime = 0, range;
    protected TargetMode targetMode = TargetMode.FIRSTEST;
    protected String attackAnimation;
    protected Vector3 attackOffset, centerOffset;
    private ClosestRayResultCallback callback;

    public AttackingEntity(Vector3 pos, int hp, int health, int range, EnumSet<Types> types, EnumSet<Effects> effects, float coolDown, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities, String attackAnimation, Vector3 attackOffset){
        super(pos, hp, health, types, effects, instance, shape, world, entities);
        this.coolDown = coolDown;
        this.range = range;
        this.attackAnimation = attackAnimation;
        this.attackOffset = attackOffset;
        callback = new ClosestRayResultCallback(new Vector3(), new Vector3());
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        coolTime += delta;
        if(coolTime > coolDown && canAttack()) {
            ArrayList<Entity> targets = target(pos, range, entities, TargetMode.CLOSEST, this instanceof Enemy ? com.logicmaster63.tdgalaxy.tower.Tower.class: Enemy.class);
            if(targets != null) {
                attack(targets);
                coolTime = 0;
            }
        }
    }

    public void attack(ArrayList<Entity> targets) {
        //if(this instanceof Basic)
            //System.out.println(this.toString() + " ---> " + target.toString());
    }

    protected boolean canPathTo(Entity target) {
        callback.setRayFromWorld(pos);
        callback.setRayToWorld(target.body.getWorldTransform().getTranslation(tempVector).sub(pos).setLength(range));
        btCollisionObject object = Tools.closestRayTestObject(world, callback);
        return (object != null && (object.getUserValue() == target.body.getUserValue()));
    }

    public void setTargetMode(TargetMode targetMode) {
        this.targetMode = targetMode;
    }

    public boolean canAttack() {
        return true;
    }

    public ArrayList<Entity> target(Vector3 pos, double range, IntMap<Entity> entityMap, TargetMode mode, Class targetClass) {
        List<Entity> entities = new ArrayList<Entity>(Arrays.asList(entityMap.values().toArray().toArray()));
        ArrayList<Entity> array = new ArrayList<Entity>();
        tempEntity = null;
        if(entities.size() < 1)
            return null;
        for(int i = 0; i < entities.size(); i++) {
            tempVector.set(entities.get(i).pos);
            //if(this instanceof Basic)
            //System.out.println(i + ": " + (Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2)));
            if(tempEntity == null) {
                if(targetClass.isInstance(entities.get(i)) && Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2) < Math.pow(range, 2) && canPathTo(entities.get(i)))
                    if(mode == TargetMode.GROUP)
                        array.add(entities.get(i));
                    else
                        tempEntity = entities.get(i);
            } else {
                if(targetClass.isInstance(entities.get(i)) && Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2) < Math.pow(range, 2) && canPathTo(entities.get(i))) {
                    switch (mode) {
                        case CLOSEST: //Consider using with another test vector object: pos.sub(tempVector).len2() < pos.sub(tempObject.pos).len2()
                            if(Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2) < Math.pow(pos.x - tempEntity.pos.x, 2) + Math.pow(pos.y - tempEntity.pos.y, 2) + Math.pow(pos.z - tempEntity.pos.z, 2))
                                tempEntity = entities.get(i);
                            break;
                        case FURTHEST:
                            if(Math.pow(pos.x - tempVector.x, 2) + Math.pow(pos.y - tempVector.y, 2) + Math.pow(pos.z - tempVector.z, 2) > Math.pow(pos.x - tempEntity.pos.x, 2) + Math.pow(pos.y - tempEntity.pos.y, 2) + Math.pow(pos.z - tempEntity.pos.z, 2))
                                tempEntity = entities.get(i);
                            break;
                        case FASTEST:
                            if (entities.get(i) instanceof Enemy && ((Enemy) entities.get(i)).getSpeeed() > ((Enemy) tempEntity).getSpeeed())
                                tempEntity = entities.get(i);
                            break;
                        case SLOWEST:
                            if (entities.get(i) instanceof Enemy && ((Enemy) entities.get(i)).getSpeeed() < ((Enemy) tempEntity).getSpeeed())
                                tempEntity = entities.get(i);
                            break;
                        case STRONGEST:
                            if (entities.get(i).health > tempEntity.health)
                                tempEntity = entities.get(i);
                            break;
                        case WEAKEST:
                            if (entities.get(i).health < tempEntity.health)
                                tempEntity = entities.get(i);
                            break;
                        case FIRSTEST:
                            if(entities.get(i) instanceof Enemy && ((Enemy) entities.get(i)).getDist() > ((Enemy) tempEntity).getDist())
                                tempEntity = entities.get(i);
                            break;
                        case LASTEST:
                            if(entities.get(i) instanceof Enemy && ((Enemy) entities.get(i)).getDist() < ((Enemy) tempEntity).getDist())
                                tempEntity = entities.get(i);
                            break;
                        case GROUP:
                            array.add(entities.get(i));
                            break;
                    }
                }
            }
        }
        if(mode == TargetMode.GROUP) {
            return array;
        } else {
            if(tempEntity == null)
                return null;
            array.add(tempEntity);
            return array;
        }
    }
}
