package input;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
    private static double lastX;
    private static double lastY;
    private static boolean firstMouse = true;

    private static float deltaX;
    private static float deltaY;

    public static void init(long windowHandle){
        glfwSetCursorPosCallback(windowHandle, (window, xPos, yPos) -> {
            if(firstMouse){
                lastX = xPos;
                lastY = yPos;
                firstMouse = false;
            }

            deltaX = (float) (xPos - lastX);
            deltaY = (float) (lastY - yPos);

            lastX = xPos;
            lastY = yPos;
        });
    }

    public static void update(){

    }

    public static float getDeltaX(){
        return deltaX;
    }
    public static float getDeltaY(){
        return deltaY;
    }

    public static void endFrame(){
        deltaX = 0;
        deltaY = 0;
    }
}
