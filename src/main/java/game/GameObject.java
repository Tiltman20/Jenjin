package game;

import core.Scene;
import graphics.Shader;
import graphics.Transform;
import graphics3d.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GameObject {
    public Material material;
    public final Mesh3D mesh;
    public final Transform3D transform;

    public GameObject(){
        material = new Material("lit3d");
        mesh = new Mesh3D(new float[]{}, new int[]{});
        transform = new Transform3D();
    }

    public GameObject(Mesh3D mesh){
        material = new Material("lit3d");
        this.mesh = mesh;
        transform = new Transform3D();
    }

    public void render(){
        Shader shader = material.shader;
        Camera3D camera = Scene.worldCamera;
        shader.bind();
        shader.setMat4("uModel", transform.getModelMatrix());
        shader.setMat4("uView", camera.getViewMatrix());
        shader.setMat4("uProjection", camera.getProjectionMatrix());

        shader.setInt("uLightCount", Scene.lights.size());

        for (int i = 0; i < Scene.lights.size(); i++) {
            Light l =  Scene.lights.get(i);
            shader.setVec3("uLights["+i+"].position", l.position.x, l.position.y, l.position.z);
            shader.setVec3("uLights["+i+"].color", l.color.x, l.color.y, l.color.z);
        }

        shader.setVec3(
                "uViewPos",
                camera.getPosition().x,
                camera.getPosition().y,
                camera.getPosition().z
        );
        shader.setVec3("uColor", material.diffuse.x, material.diffuse.y, material.diffuse.z);

        mesh.render(shader);

        shader.unbind();
    }

    public void cleanup(){
        material.cleanup();
        mesh.cleanup();
    }
}
