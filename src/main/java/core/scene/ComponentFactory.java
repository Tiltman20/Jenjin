package core.scene;

import core.scene.components.MaterialRenderer;
import core.scene.components.MeshFilter;
import core.scene.components.MeshRenderer;

public class ComponentFactory {
    public static Component create(String type){
        return switch (type){
            case "MeshRenderer" -> new MeshRenderer();
            case "MeshFilter" -> new MeshFilter();
            case "MaterialRenderer" -> new MaterialRenderer();
            default -> throw new RuntimeException("Unknown component type: " + type);
        };
    }
}
