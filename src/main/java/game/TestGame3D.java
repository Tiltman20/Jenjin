package game;

import core.*;
import core.scene.SceneLoader;
import core.scene.SceneSerializer;
import core.scene.components.MaterialRenderer;
import core.scene.components.MeshFilter;
import core.scene.components.MeshRenderer;
import graphics.Shader;
import graphics3d.*;
import input.*;
import org.joml.Vector3f;

public class TestGame3D implements Game {
    private Scene scene;
    private float time = 0f;
    @Override
    public void init() {
        Scene.worldCamera = new Camera3D(70f,1280f/720f,0.1f,100f);
        scene = new Scene();
        Scene.addLight(new Light(new Vector3f(0f, 4f, 0f), new Vector3f(1f, 1f, 1f)));
        scene = SceneLoader.load("C:\\\\Users\\\\tilma\\\\Desktop\\\\scene2.xml");
    }

    @Override
    public void update(float dt) {
//        for(Node child : scene.getRoot().getChildren()){
//            child.transform.rotation.y += dt * 45f;
//            for(Node child2 : child.getChildren()){
//                if(child2.name == "TestChild"){
//                    child2.transform.position.y += Math.sin(time) * 0.01f;
//                }
//            }
//        }
        time += dt;
        scene.update(dt);

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
        scene.render();
    }

    @Override
    public void cleanup() {
        scene.cleanup();
    }
}
