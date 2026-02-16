package input;

import static org.lwjgl.glfw.GLFW.*;
import java.util.HashMap;
import java.util.Map;


public class MouseInput {
    private static double lastX;
    private static double lastY;
    private static boolean firstMouse = true;

    private static float deltaX;
    private static float deltaY;

    private static float positionX;
    private static float positionY;

    private static final Map<Integer, Boolean> currentButtons = new HashMap<>();
    private static final Map<Integer, Boolean> previousButtons = new HashMap<>();


    public static void init(long windowHandle){
        glfwSetCursorPosCallback(windowHandle, (window, xPos, yPos) -> {
            if(firstMouse){
                lastX = xPos;
                lastY = yPos;
                firstMouse = false;
            }

            positionX = (float)xPos;
            positionY = (float)yPos;

            deltaX = (float) (xPos - lastX);
            deltaY = (float) (lastY - yPos);

            lastX = xPos;
            lastY = yPos;
        });
        glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
            if(action == GLFW_PRESS){
                currentButtons.put(button, true);
            }else if(action == GLFW_RELEASE){
                currentButtons.put(button, false);
            }
        });
    }

    public static void update(){

    }

    public static float getMouseX(){
        return positionX;
    }
    public static float getMouseY(){
        return positionY;
    }

    public static float getDeltaX(){
        return deltaX;
    }
    public static float getDeltaY(){
        return deltaY;
    }

    public static boolean isButtonDown(int button){
        return currentButtons.getOrDefault(button, false);
    }

    public static boolean isButtonPressed(int button){
        return isButtonDown(button) && !previousButtons.getOrDefault(button, false);
    }

    public static boolean isButtonReleased(int button){
        return !isButtonDown(button) && previousButtons.getOrDefault(button, false);
    }


    public static void endFrame(){
        deltaX = 0;
        deltaY = 0;
        previousButtons.clear();
        previousButtons.putAll(currentButtons);
    }
}
