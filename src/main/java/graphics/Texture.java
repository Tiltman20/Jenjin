package graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Stack;

import core.ResourceLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;


public class Texture {
    private final int id;
    private final int width;
    private final int height;

    public Texture(String path) {
        // stbi_set_flip_vertically_on_load(true);

        ByteBuffer imageBuffer = ResourceLoader.loadAsByteBuffer(path);

        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer data = stbi_load_from_memory(imageBuffer, w, h, channels, 4);
            if(data == null) {
                throw new RuntimeException(
                        "Failed to load texture: " + path +
                                "\n" + stbi_failure_reason()
                );
            }
            width = w.get();
            height = h.get();

            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    width,
                    height,
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    data
            );

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            stbi_image_free(data);
        }
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind(){
        glDeleteTextures(id);
    }

}
