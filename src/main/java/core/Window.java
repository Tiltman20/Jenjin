package core;

import input.Input;
import input.MouseInput;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {
    private final String title;
    private final int width;
    private final int height;

    private long windowHandle;
    private static long staticWindowHandle;

    private int framebufferWidth;
    private int framebufferHeight;


    public Window(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()){
            throw new IllegalStateException("Unable to initialise GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        windowHandle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if(windowHandle == 0){
            throw new RuntimeException("Failed to create GLFW window");
        }
        staticWindowHandle = windowHandle;

        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSetInputMode(windowHandle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glClearColor(0f, 0f, 0f, 1f);

        Input.init(windowHandle);
        MouseInput.init(windowHandle);

        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowHandle);
    }

    public void update(){
        GLFW.glfwSwapBuffers(windowHandle);
        GLFW.glfwPollEvents();

        int[] w = new int[1];
        int[] h = new int[1];

        GLFW.glfwGetFramebufferSize(windowHandle, w, h);

        framebufferWidth = w[0];
        framebufferHeight = h[0];

    }

    public int getFramebufferWidth(){
        return framebufferWidth;
    }

    public int getFramebufferHeight(){
        return framebufferHeight;
    }


    public boolean shouldClose(){
        return GLFW.glfwWindowShouldClose(windowHandle);
    }

    public void cleanup(){
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public long getHandle() {
        return windowHandle;
    }

    public static void setCursorEnabled(long window, boolean enabled){
        GLFW.glfwSetInputMode(
                staticWindowHandle,
                GLFW.GLFW_CURSOR,
                enabled ? GLFW.GLFW_CURSOR_NORMAL : GLFW.GLFW_CURSOR_DISABLED
        );
    }
}
