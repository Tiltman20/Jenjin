package graphics3d;

import core.ResourceLoader;
import core.Scene;
import graphics.Shader;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh3D {
    public final int vao;
    private final int vbo;
    private final int ebo;
    private final int indexCount;

    public Mesh3D(float[] vertices, int[] indices){
        System.out.println("Vertex Count: " + vertices.length);
        System.out.println("Index Count: " + indices.length);
        for (int i = 0; i < vertices.length; i++) {
            System.out.println(i + ": " + vertices[i]);
        }
        for (int i = 0; i < indices.length; i++) {
            System.out.println(i + ": " + indices[i]);
        }
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

    public void render(Shader shader){
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void cleanup(){
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        glDeleteVertexArrays(vao);
    }
}
