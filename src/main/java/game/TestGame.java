package game;

import core.Game;
import core.AssetManager;
import graphics.*;
import input.Input;
import input.Keys;
import math.Vector2f;

public class TestGame implements Game {

    private Sprite player;
    private Sprite enemy;
    private Camera2D camera;

    private final float speed = 200f;

    @Override
    public void init() {
        camera = new Camera2D(1280, 720);
        Texture playerTexture = AssetManager.getTexture("assets/player.png");
        Texture enemyTexture = AssetManager.getTexture("assets/enemy.png");
        player = new Sprite(playerTexture, 100, 100);
        enemy = new Sprite(enemyTexture, 100, 100);
    }

    @Override
    public void update(float dt) {
        if (Input.isKeyDown(Keys.W)) player.getTransform().position.y += speed * dt;
        if (Input.isKeyDown(Keys.S)) player.getTransform().position.y -= speed * dt;
        if (Input.isKeyDown(Keys.A)) player.getTransform().position.x -= speed * dt;
        if (Input.isKeyDown(Keys.D)) player.getTransform().position.x += speed * dt;
    }

    @Override
    public void render() {
        player.render(camera);
        enemy.render(camera);
    }

    @Override
    public void cleanup() {
        player.cleanup();
        enemy.cleanup();
    }
}
