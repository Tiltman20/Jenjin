package graphics3d;

import core.ResourceLoader;
import core.Scene;
import graphics.Shader;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh3D {
    public Shader shader = new Shader(ResourceLoader.load("shaders/basic3d.vert"), ResourceLoader.load("shaders/basic3d.frag"));
    public Transform3D transform = new Transform3D();
    public Vector3f color = new Vector3f(0.8f);
    private final int vao;
    private final int vbo;
    private final int ebo;
    private final int indexCount;

    public Mesh3D(float[] vertices, int[] indices){
        indexCount = indices.length;

        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ebo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6* Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    public void setShader(Shader shader){
        this.shader = shader;
    }

    public void render(Camera3D camera){
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

//        // Licht 0
//        shader.setVec3("uLights[0].position", 2f, 2f, 2f);
//        shader.setVec3("uLights[0].color", 1f, 1f, 1f); // weiß
//
//        // Licht 1
//        shader.setVec3("uLights[1].position", -2f, 1f, 0f);
//        shader.setVec3("uLights[1].color", 1f, 0.2f, 0.2f); // rot

        shader.setVec3(
                "uViewPos",
                camera.getPosition().x,
                camera.getPosition().y,
                camera.getPosition().z
        );

        shader.setVec3("uColor", color.x, color.y, color.z);

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        shader.unbind();
    }

    public void cleanup(){
        shader.cleanup();
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        glDeleteVertexArrays(vao);
    }
}
