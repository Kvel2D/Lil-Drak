package com.mygdx.lildrak.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.lildrak.AssetPaths;
import com.mygdx.lildrak.Constants;
import com.mygdx.lildrak.GameScreen;
import com.mygdx.lildrak.Lildrak;
import com.mygdx.lildrak.entity.components.*;
import com.mygdx.lildrak.entity.components.Transform;

public class EntityFactory
{
    World box2dWorld;

    public EntityFactory()
    {
        this.box2dWorld = GameScreen.world;
    }

    public Entity createWhip(float x, float y, float ySpeed)
    {
        Entity entity = new Entity();

        Texture texture = Lildrak.assets.get(AssetPaths.WHIP);
        TextureRegion region = new TextureRegion(texture);
        float width = texture.getWidth() * Constants.METER_TO_PIXEL;
        float height = texture.getHeight() * Constants.METER_TO_PIXEL;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(width / 2, height / 2); //setAsBox() takes half-width and half-height
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.density = 10f;
        fixtureDef.friction = 0f;
        Body body = box2dWorld.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        body.setLinearVelocity(0, ySpeed);
        fixture.setUserData(entity);
        rectangle.dispose();

        entity.add(new Transform(x, y, Constants.WHIP_Z, 0))
              .add(new TextureComponent(region))
              .add(new BodyComponent(body))
              .add(new VerticalLimit(Constants.VIEWPORT_HEIGHT + 1f))
              .add(new Damage(1));
        GameScreen.engine.addEntity(entity);
        return entity;
    }

    public Entity createCandy(float x, float y, float ySpeed)
    {
        return createCollectible(x, y, ySpeed, (Texture)Lildrak.assets.get(AssetPaths.CANDY), 1000);
    }
    public Entity createLollipop(float x, float y, float ySpeed)
    {
        return createCollectible(x, y, ySpeed, (Texture)Lildrak.assets.get(AssetPaths.LOLLIPOP), 2500);
    }
    public Entity createMoney(float x, float y, float ySpeed)
    {
        return createCollectible(x, y, ySpeed, (Texture)Lildrak.assets.get(AssetPaths.MONEY), 10000);
    }

    public Entity createCollectible(float x, float y, float ySpeed, Texture texture, int value)
    {
        Entity entity = new Entity();

        TextureRegion region = new TextureRegion(texture);
        float width = texture.getWidth() * Constants.METER_TO_PIXEL;
        float height = texture.getHeight() * Constants.METER_TO_PIXEL;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(width / 2, height / 2); //setAsBox() takes half-width and half-height
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.density = 10f;
        fixtureDef.friction = 0f;
        Body body = box2dWorld.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        body.setLinearVelocity(0, ySpeed);
        fixture.setUserData(entity);
        rectangle.dispose();

        Sound hurtSound = Lildrak.assets.get(AssetPaths.PICKUP);

        entity.add(new Transform(x, y, Constants.COLLECTIBLE_Z, 0))
            .add(new TextureComponent(region))
            .add(new BodyComponent(body))
            .add(new VerticalLimit(Constants.VIEWPORT_HEIGHT + 1f))
            .add(new Health(1))
            .add(new Score(value))
            .add(new HurtSound(hurtSound));
        GameScreen.engine.addEntity(entity);
        return entity;
    }

    public Entity createWindow(float x, float y, float originalX, float originalY, float ySpeed)
    {
        Entity entity = new Entity();

        Texture texture = Lildrak.assets.get(AssetPaths.WINDOW);
        TextureRegion region = new TextureRegion(texture);
        float width = texture.getWidth() * Constants.METER_TO_PIXEL;
        float height = texture.getHeight() * Constants.METER_TO_PIXEL;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(width / 2, height / 2); //setAsBox() takes half-width and half-height
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = rectangle;
        fixtureDef.density = 10f;
        fixtureDef.friction = 0f;
        fixtureDef.filter.groupIndex = -2;
        Body body = box2dWorld.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(entity);
        body.setLinearVelocity(0, ySpeed);
        rectangle.dispose();

        entity.add(new Transform(x, y, Constants.WINDOW_Z, 0))
            .add(new OriginalPosition(originalX, originalY))
            .add(new TextureComponent(region))
            .add(new BodyComponent(body));
        GameScreen.engine.addEntity(entity);
        return entity;
    }

    public Entity createSkull(float x, float y, float xSpeed)
    {
        Entity entity = new Entity();

        Texture texture = Lildrak.assets.get(AssetPaths.SKULL);
        TextureRegion region = new TextureRegion(texture);
        Color color = new Color(1.0f, 1.0f, 1.0f, 0.6f);
        float width = texture.getWidth() * Constants.METER_TO_PIXEL;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        CircleShape circle = new CircleShape();
        circle.setRadius(width / 3);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 20f;
        fixtureDef.friction = 0f;
        Body body = box2dWorld.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(entity);

        body.setLinearVelocity(xSpeed, 0);
        body.setAngularVelocity(0.2f);
        circle.dispose();

        entity.add(new Transform(x, y, Constants.SKULL_Z, 0))
            .add(new TextureComponent(region))
            .add(new ColorComponent(color))
            .add(new BodyComponent(body))
            .add(new Damage(1))
            .add(new HorizontalLimit(-1f));
        GameScreen.engine.addEntity(entity);
        return entity;
    }

    public Entity createBat(float x, float y)
    {
        Entity entity = new Entity();

        Array<Texture> frames = new Array<Texture>();
        frames.add((Texture) Lildrak.assets.get(AssetPaths.BAT1));
        frames.add((Texture) Lildrak.assets.get(AssetPaths.BAT2));
        frames.add((Texture) Lildrak.assets.get(AssetPaths.BAT3));
        frames.add((Texture) Lildrak.assets.get(AssetPaths.BAT4));
        Texture texture = frames.get(0);
        TextureRegion region = new TextureRegion(texture);

        float width = texture.getWidth() * Constants.METER_TO_PIXEL;
        float height = texture.getHeight() * Constants.METER_TO_PIXEL;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        CircleShape circle = new CircleShape();
        circle.setRadius(width / 4); //setAsBox() takes half-width and half-height
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 10f;
        fixtureDef.friction = 0f;
        fixtureDef.filter.groupIndex = Constants.BACKGROUND_INDEX;
        Body body = box2dWorld.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(entity);
        body.setLinearDamping(1f);
        circle.dispose();

        Sound hurtSound = Lildrak.assets.get(AssetPaths.HURT);

        entity.add(new Transform(x, y, Constants.BAT_Z, 0))
            .add(new TextureComponent(region))
            .add(new BatAnimation(frames))
            .add(new BodyComponent(body))
            .add(new PlayerMovement(Constants.MOVE_FORCE))
            .add(new Health(Constants.PLAYER_HEALTH))
            .add(new Damage(1))
            .add(new HurtSound(hurtSound))
            .add(new VerticalLimit(Constants.VIEWPORT_HEIGHT + 0.1f))
            .add(new VerticalLimit(Constants.VIEWPORT_HEIGHT + 0.1f))
            .add(new Invincibility(1f));
        GameScreen.engine.addEntity(entity);
        return entity;
    }
}
