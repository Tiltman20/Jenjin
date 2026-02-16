package core;

import core.scene.components.Camera;
import game.GameObject;
import graphics.Transform;
import graphics3d.Camera3D;
import graphics3d.Light;
import graphics3d.Mesh3D;
import core.scene.Component;
import core.scene.systems.RenderSystem;

import graphics3d.Transform3D;
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
    public Camera3D activeGameCamera;

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

    public void cleanup() {}

    public Scene deepCopy(){
        Scene copy = new Scene();

        Node newRoot = copyNode(this.root);
        copy.getRoot().addChild(newRoot);
        copy.worldCamera =  this.worldCamera;
        return copy;
    }

    public Camera3D findMainCamera(){
        return findMainCameraRecursive(root);
    }

    private Camera3D findMainCameraRecursive(Node node) {
        Camera cam = node.getComponent(Camera.class);

        if(cam != null && cam.primary){
            cam.camera.setTransform(node.transform);
            return cam.camera;
        }
        for(Node child : node.getChildren()){
            Camera3D found = findMainCameraRecursive(child);
            if(found != null) return found;
        }
        return null;
    }

    private Node copyNode(Node original){
        Node nodeCopy = new Node(original.name);

        copyTransform(original.transform, nodeCopy.transform);

        for(Component c : original.getComponents()){
            nodeCopy.addComponent(copyComponent(c));
        }

        for (Node child : original.getChildren()){
            nodeCopy.addChild(copyNode(child));
        }
        return nodeCopy;
    }

    private void copyTransform(Transform3D src, Transform3D dst){
        dst.position.set(src.position);
        dst.rotation.set(src.rotation);
        dst.scale.set(src.scale);
    }

    private Component copyComponent(Component c){
        return c.cloneComponent();
    }

    public void render() {
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
