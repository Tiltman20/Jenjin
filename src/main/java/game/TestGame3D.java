package game;

import core.Game;
import core.ObjLoader;
import core.ResourceLoader;
import core.Scene;
import graphics.Shader;
import graphics3d.*;
import input.*;
import org.joml.Vector3f;

public class TestGame3D implements Game {
    @Override
    public void init() {
        Scene.worldCamera = new Camera3D(70f,1280f/720f,0.1f,100f);

        Material mat1 = new Material("lit3d");
        mat1.diffuse = new Vector3f(.5f, 1f, 0f);

        Mesh3D cube = ObjLoader.load("assets/cube.obj");
        Mesh3D cube2 = new Mesh3D(PrimitiveCube.VERTICES, PrimitiveCube.INDICES);
        Mesh3D plane = new Mesh3D(PrimitivePlane.VERTICES, PrimitivePlane.INDICES);

        GameObject cubeO = new GameObject(cube);
        GameObject cubeO2 = new GameObject(cube2);
        GameObject planeO = new GameObject(plane);

        cubeO.material = mat1;
        cubeO2.material = mat1;
        planeO.material = mat1;

        Scene.addGameObject("Cube 1", cubeO);
        Scene.addGameObject("Cube 2", cubeO2);
        Scene.addGameObject("Plane", planeO);
        Scene.addLight(new Light(new Vector3f(2f, 2f, 2f), new Vector3f(1f, 1f, 1f)));
        Scene.addLight(new Light(new Vector3f(-2f, 2f, -2f), new Vector3f(0.1f, 0.5f, 0.75f)));
        planeO.transform.rotation.x = -90f;
        planeO.transform.position.y -= 2f;
        planeO.transform.scale.set(25f);
        cubeO2.transform.position.y += 20f;

    }

    @Override
    public void update(float dt) {
        Scene.getGameObject("Cube 1").transform.rotation.y += 45f * dt;
        float sensitivity = 0.1f;

        Scene.worldCamera.rotate(
                MouseInput.getDeltaX() * sensitivity,
                MouseInput.getDeltaY() *sensitivity
        );

        float speed = 5f * dt;
        if (Input.isKeyDown(Keys.W)) Scene.worldCamera.moveForward(speed);
        if (Input.isKeyDown(Keys.S)) Scene.worldCamera.moveBackward(speed);
        if (Input.isKeyDown(Keys.A)) Scene.worldCamera.moveLeft(speed);
        if (Input.isKeyDown(Keys.D)) Scene.worldCamera.moveRight(speed);
        if (Input.isKeyDown(Keys.SPACE)) Scene.worldCamera.moveUp(speed);
        if (Input.isKeyDown(Keys.LSHIFT)) Scene.worldCamera.moveDown(speed);
    }

    @Override
    public void render() {
        Scene.render();
    }

    @Override
    public void cleanup() {
        Scene.cleanup();
    }
}
