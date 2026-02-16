package editor;

import core.ResourceLoader;
import graphics.Shader;
import graphics3d.Camera3D;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class LineRenderer {
    private static int vao;
    private static int vbo;
    private static Shader shader;

    public static void init(){
        shader = new Shader(
                ResourceLoader.load("shaders/line.vert"),
                ResourceLoader.load("shaders/line.frag"),
                "line"
        );

        vao = glGenVertexArrays();
        vbo = glGenBuffers();
    }

    public static void drawLine(
            Vector3f a, Vector3f b,
            Vector3f color,
            Camera3D camera
    ){
        float[] vertices = {
                a.x, a.y, a.z,
                b.x, b.y, b.z,
        };

        shader.bind();
        shader.setMat4("uView", camera.getViewMatrix());
        shader.setMat4("uProjection", camera.getProjectionMatrix());
        shader.setVec3("uColor", color.x, color.y, color.z);

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glDrawArrays(GL_LINES, 0, 2);

        glBindVertexArray(0);
    }
}
