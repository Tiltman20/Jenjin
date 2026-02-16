package graphics3d;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;

public class Framebuffer {
    private int fbo;
    private int texture;
    private int rbo;

    private int width;
    private int height;

    public Framebuffer(int width, int height){
        this.width = width;
        this.height = height;
        create();
    }

    private void create(){
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        // Color texture
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,width,height,0,GL_RGB,GL_UNSIGNED_BYTE,0);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,texture,0);

        // Depth buffer
        rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new RuntimeException("Framebuffer not complete");

        glBindFramebuffer(GL_FRAMEBUFFER,0);
    }

    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        glViewport(0,0,width,height);
    }

    public void unbind(int windowWidth, int windowHeight){
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        glViewport(0,0,windowWidth,windowHeight);
    }

    public int getTexture(){
        return texture;
    }

    public void resize(int newWidth, int newHeight){
        if (newWidth == width && newHeight == height) return;

        width = newWidth;
        height = newHeight;

        glDeleteFramebuffers(fbo);
        glDeleteTextures(texture);
        glDeleteRenderbuffers(rbo);

        create();
    }
}
