package graphics;

import core.Scene;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    public void init(){
        Vector3f color = Scene.worldColor;
        glClearColor(color.x, color.y, color.z, 1.0f);
    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        // TODO
    }
}
