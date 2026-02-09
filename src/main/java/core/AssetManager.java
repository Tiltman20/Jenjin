package core;


import java.util.HashMap;
import java.util.Map;

import graphics.Texture;

public class AssetManager {
    private static final Map<String, Texture> textures = new HashMap<>();

    private AssetManager() {}

    public static Texture getTexture(String path) {
        if(!textures.containsKey(path)) {
            textures.put(path, new Texture(path));
        }
        return textures.get(path);
    }

    public static void cleanup() {
        for (Texture tex : textures.values()) {
            tex.unbind();
        }
        textures.clear();
    }
}
