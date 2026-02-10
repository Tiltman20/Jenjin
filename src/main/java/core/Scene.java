package core;

import game.GameObject;
import graphics3d.Camera3D;
import graphics3d.Light;
import graphics3d.Mesh3D;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Scene {
    private static final int MAX_MESHES = 10;
    public static Map<String, Mesh3D> meshes = new HashMap<String, Mesh3D>();
    public static ArrayList<Light> lights = new ArrayList<>();
    public static Map<String, GameObject> objects = new HashMap<>();

    public static Vector3f worldColor = new Vector3f(0.2f,0.2f,0.2f);
    public static Camera3D worldCamera = new Camera3D(70f,1280f/720f,0.1f,100f);

    public static void addMesh(String name, Mesh3D mesh) {
        if(meshes.containsKey(name) || meshes.size() >= MAX_MESHES) return;
        meshes.put(name, mesh);
    }

    public static void addGameObject(String name, GameObject gameObject) {
        if(objects.containsKey(name)) return;
        objects.put(name, gameObject);
    }

    public static GameObject getGameObject(String name) {
        return objects.get(name);
    }

    public static void addLight(Light light) {
        lights.add(light);
    }

    public static void cleanup() {
        for(GameObject gameObject : objects.values()) gameObject.cleanup();
    }

    public static void render() {
        for(GameObject gameObject : objects.values()) gameObject.render();
    }
}
