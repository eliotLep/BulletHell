package com.example.bullethell.core;

import com.example.bullethell.View.Animator;
import com.example.bullethell.View.Drawer;
import com.example.bullethell.View.GameView;
import com.example.bullethell.View.Sprite;

public class Player extends Entity{

    protected float precision;
    protected long lastAttack;
    protected long attackSpeed;
    protected int burst;
    protected final int sizeFactor;

    public Player(java.util.List<Entity> entities, Vector2 size, float hpMax, float speed, float invincibleTime, Vector2 position, Vector2 direction, int team, float damage, Animator animator){
        super(entities,size,hpMax,speed,invincibleTime,position,direction,team,damage,animator);
        this.precision=0.95f;
        this.layer=this.layer+1;
        this.attackSpeed=50;
        this.lastAttack=-1;
        this.burst=2;
        this.sizeFactor=10; //plus la valeur est haute plus la hitbox du joueur sera diminué (-2*(size/sizeFactor))
    }

    public void run(){
        super.run();
        this.attack();
    }

    private void attack(){
        int nbAttack=0;
        if(this.lastAttack==-1 || java.lang.System.currentTimeMillis()-this.lastAttack>1000){
            nbAttack=1;
            this.lastAttack = java.lang.System.currentTimeMillis()-16;
        }
        else if(java.lang.System.currentTimeMillis()-this.lastAttack>=this.attackSpeed ) {
            nbAttack = (int) ((java.lang.System.currentTimeMillis() - this.lastAttack) / this.attackSpeed);
        }
        if(nbAttack>0) {
            for (int i = 0; i < nbAttack; i++) {

                for(int j=0;j<this.burst;j++) {

                    float angle = (float) (-90 * (this.precision + (Math.random() * ((1 - this.precision) * 2))));
                    float angleRadiant = (float) (angle * (Math.PI / 180));
                    float x = (float) Math.cos(angleRadiant);
                    float y = (float) Math.sin(angleRadiant);

                    Vector2 direction = new Vector2(x, y);

                    Sprite spriteBullet = GameView.sprites.get("proj4");
                    Animator animatorBullet = new Animator(spriteBullet, 10000);
                    int bulletWidth = (int) Drawer.getValFromVal(30);
                    int bulletHeight = (int)Drawer.getValFromVal(30);
                    int bulletSpeed = (int)Drawer.getValFromVal(30);
                    int range = (int)Drawer.getValFromVal(1500);
                    float moveRetardX = direction.x * bulletSpeed * (float) DeltaTime.getDeltaTime(this.lastAttack, this.lastAttack + i * this.attackSpeed);
                    float moveRetardY = direction.y * bulletSpeed * (float) DeltaTime.getDeltaTime(this.lastAttack, this.lastAttack + i * this.attackSpeed);
                    float posX = (this.position.x + this.size.x / 2 - bulletWidth / 2);
                    float posY = (this.position.y + this.size.y / 2 - bulletHeight / 2);
                    Vector2 startPos = new Vector2(posX + moveRetardX, posY + moveRetardY);
                    range -= Vector2.distance2Vector2(new Vector2(posX, posY), startPos);

                    Bullet bullet = new Bullet(this.entities, new Vector2(bulletWidth, bulletHeight), this.damage, bulletSpeed, 0, startPos, direction, this.team, this.damage, range, animatorBullet);
                    this.entities.add(bullet);
                }
            }
            this.lastAttack = java.lang.System.currentTimeMillis();
        }

    }



    public void onContact(Entity entity) {
        if (entity.team != this.team && entity instanceof Mob) {
            this.takeDamage(entity.damage);
        }
        else if (entity.team != this.team && entity instanceof Bullet) {
            ((Bullet)entity).doDamage(this);
        }
    }





}
