package game;

import core.Game;
import core.ResourceLoader;
import core.Scene;
import graphics.Shader;
import graphics3d.*;
import input.*;
import org.joml.Vector3f;

public class TestGame3D implements Game {

    private Camera3D camera;
    private Mesh3D cube;
    private Mesh3D cube2;
    private Mesh3D plane;

    @Override
    public void init() {
        camera = new Camera3D(70f,1280f/720f,0.1f,100f);
        cube = new Mesh3D(PrimitiveCube.VERTICES, PrimitiveCube.INDICES);
        cube2 = new Mesh3D(PrimitiveCube.VERTICES, PrimitiveCube.INDICES);
        plane = new Mesh3D(PrimitivePlane.VERTICES, PrimitivePlane.INDICES);
        Shader lit = new Shader(
                ResourceLoader.load("shaders/lit3d.vert"),
                ResourceLoader.load("shaders/lit3d.frag")
        );
        cube.setShader(lit);
        cube2.setShader(lit);
        plane.setShader(lit);
        cube.color = new Vector3f(0.35f,0.8f,0.2f);
        cube2.color = new Vector3f(1.0f,0.35f,0.9f);
        plane.color = new Vector3f(0.8f,0.4f,0.65f);
        Scene.addMesh("cube", cube);
        Scene.addMesh("cube2", cube2);
        Scene.addMesh("plane", plane);
        Scene.addLight(new Light(new Vector3f(2f, 2f, 2f), new Vector3f(1f, 1f, 1f)));
        Scene.addLight(new Light(new Vector3f(-2f, 2f, -2f), new Vector3f(0.1f, 0.5f, 0.75f)));
        plane.transform.rotation.x = -90f;
        plane.transform.position.y -= 2f;
        plane.transform.scale.set(25f);
        cube2.transform.position.y += 20f;

    }

    @Override
    public void update(float dt) {
        cube.transform.rotation.y += 45f * dt;
        float sensitivity = 0.1f;

        camera.rotate(
                MouseInput.getDeltaX() * sensitivity,
                MouseInput.getDeltaY() *sensitivity
        );

        float speed = 5f * dt;
        if (Input.isKeyDown(Keys.W)) camera.moveForward(speed);
        if (Input.isKeyDown(Keys.S)) camera.moveBackward(speed);
        if (Input.isKeyDown(Keys.A)) camera.moveLeft(speed);
        if (Input.isKeyDown(Keys.D)) camera.moveRight(speed);
        if (Input.isKeyDown(Keys.SPACE)) camera.moveUp(speed);
        if (Input.isKeyDown(Keys.LSHIFT)) camera.moveDown(speed);
    }

    @Override
    public void render() {
        cube.render(camera);
        cube2.render(camera);
        plane.render(camera);
    }

    @Override
    public void cleanup() {
        cube.cleanup();
    }
}
