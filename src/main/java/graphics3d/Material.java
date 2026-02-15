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
    public String albedoPath;
    public String specularMapPath;

    public Shader shader;
    public Texture3D albedo;
    public Texture3D specularMap;

    public Material(String shaderType) {
        this.shaderType = shaderType;
        shader = new Shader(
                ResourceLoader.load("shaders/"+shaderType+".vert"),
                ResourceLoader.load("shaders/"+shaderType+".frag"),
                shaderType
        );
    }

    public void bind(Camera3D camera, Transform3D transform){
        shader.bind();

        shader.setMat4("uModel", transform.getModelMatrix());
        shader.setMat4("uView", camera.getViewMatrix());
        shader.setMat4("uProjection", camera.getProjectionMatrix());

        shader.setVec3("uColor", diffuse.x, diffuse.y, diffuse.z);

        if(albedo != null){
            albedo.bind(0);
            shader.setInt("uTexture", 0);
        }
        if(specularMap != null){
            specularMap.bind(1);
            shader.setInt("uSpecularMap", 1);
        }
    }

    public void setAlbedo(String path){
        albedoPath = path;
        albedo = new Texture3D(path);
    }

    public void unbind(){
        shader.unbind();
    }

    public void cleanup() {
        shader.cleanup();
        if(albedo != null) albedo.cleanup();
        if(specularMap != null) specularMap.cleanup();
    }


}
