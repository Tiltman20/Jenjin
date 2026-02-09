package graphics;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    public void init(){
        glClearColor(0.1f, 0.1f, 0.15f, 1.0f);
    }

    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        // TODO
    }
}
