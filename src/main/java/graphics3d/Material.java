package graphics3d;

import core.ResourceLoader;
import graphics.Shader;
import org.joml.Vector3f;

public class Material {
    public Vector3f diffuse =  new Vector3f(1f);
    public Vector3f specular =  new Vector3f(0f);

    public String shaderType = "lit3d";

    public Shader shader;

    public Material(String shaderType) {
        this.shaderType = shaderType;
        shader = new Shader(
                ResourceLoader.load("shaders/"+shaderType+".vert"),
                ResourceLoader.load("shaders/"+shaderType+".frag")
        );
    }

    public void cleanup() {
        shader.cleanup();
    }
}
