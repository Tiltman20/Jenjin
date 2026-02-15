package graphics3d;

import core.ResourceLoader;
import core.Scene;
import graphics.Shader;
import graphics.Texture;
import org.joml.Vector3f;

public class Material {
    public Vector3f diffuse =  new Vector3f(1f);
    public Vector3f specular =  new Vector3f(0f);

    public String shaderType = "lit3d";

    public Shader shader;
    public Texture3D albedo;

    public Material(String shaderType) {
        this.shaderType = shaderType;
        shader = new Shader(
                ResourceLoader.load("shaders/"+shaderType+".vert"),
                ResourceLoader.load("shaders/"+shaderType+".frag")
        );
    }

    public void bind(Transform3D transform){
        Camera3D camera = Scene.worldCamera;
        shader.bind();

        shader.setMat4("uModel", transform.getModelMatrix());
        shader.setMat4("uView", camera.getViewMatrix());
        shader.setMat4("uProjection", camera.getProjectionMatrix());

        shader.setVec3("uColor", diffuse.x, diffuse.y, diffuse.z);

        if(albedo != null){
            albedo.bind(0);
            shader.setInt("uTexture", 0);
        }
    }

    public void unbind(){
        shader.unbind();
    }

    public void cleanup() {
        shader.cleanup();
    }


}
