package core.scene.systems;

import core.Scene;
import core.Node;
import core.scene.components.MeshFilter;
import core.scene.components.MaterialRenderer;
import core.scene.components.MeshRenderer;
import graphics.Shader;
import graphics3d.Camera3D;
import graphics3d.Light;
import graphics3d.Material;

public class RenderSystem {
    public static void render(Node root){
        renderNode(root);
    }

    private static void renderNode(Node node){
        MeshFilter meshFilter = node.getComponent(MeshFilter.class);
        MaterialRenderer matRenderer = node.getComponent(MaterialRenderer.class);

        if(meshFilter != null && matRenderer != null){
            Camera3D camera = Scene.worldCamera;
            Material material = matRenderer.material;

            material.bind(camera, node.transform);
            applyLights(material.shader);
            meshFilter.mesh.render();
            material.unbind();
        }
        for(Node child : node.getChildren())
            renderNode(child);
    }

    private static void applyLights(Shader shader){
        shader.setInt("uLightCount", Scene.lights.size());

        for (int i = 0; i < Scene.lights.size(); i++) {
            Light l = Scene.lights.get(i);

            shader.setVec3("uLights["+i+"].position", l.position.x,  l.position.y, l.position.z);
            shader.setVec3("uLights["+i+"].color", l.color.x,  l.color.y, l.color.z);
        }

        shader.setVec3("uViewPos", Scene.worldCamera.getPosition().x, Scene.worldCamera.getPosition().y, Scene.worldCamera.getPosition().z);
    }
}
