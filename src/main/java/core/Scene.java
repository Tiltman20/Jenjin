package core;

import graphics3d.Light;
import graphics3d.Mesh3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Scene {
    private static final int MAX_MESHES = 10;
    public static Map<String, Mesh3D> meshes = new HashMap<String, Mesh3D>();
    public static ArrayList<Light> lights = new ArrayList<>();

    public static void addMesh(String name, Mesh3D mesh) {
        if(meshes.containsKey(name) || meshes.size() >= MAX_MESHES) return;
        meshes.put(name, mesh);
    }

    public static void addLight(Light light) {
        lights.add(light);
    }
}
