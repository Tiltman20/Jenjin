package core;

import game.GameObject;
import graphics3d.Camera3D;
import graphics3d.Light;
import graphics3d.Mesh3D;
import core.scene.Component;
import core.scene.systems.RenderSystem;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Scene {
    private final int MAX_MESHES = 10;
    public Map<String, Mesh3D> meshes = new HashMap<String, Mesh3D>();
    public static ArrayList<Light> lights = new ArrayList<>();
    public Map<String, GameObject> objects = new HashMap<>();

    public static Vector3f worldColor = new Vector3f(0.2f,0.2f,0.2f);
    public static Camera3D worldCamera = new Camera3D(70f,1280f/720f,0.1f,100f);

    private final Node root = new Node("Root");

    public Node getRoot(){
        return root;
    }

    public void update(float dt){
        traverseUpdate(root, dt);
    }

    private void traverseUpdate(Node node, float dt) {
        for(Component c : node.getComponents()){
            c.update(dt);
        }
        for(Node child : node.getChildren()){
            traverseUpdate(child, dt);
        }
    }

    public static void addLight(Light light) {
        lights.add(light);
    }

    public void cleanup() {

    }

    public void render() {
//        traverseRender(root);
        RenderSystem.render(root);
    }

    private void traverseRender(Node node) {
        for(Component c : node.getComponents()){
            c.render();
        }
        for(Node child : node.getChildren()){
            traverseRender(child);
        }
    }
}
