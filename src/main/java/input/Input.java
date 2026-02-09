package input;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private static final Map<Integer, Boolean> currentKeys = new HashMap<>();
    private static final Map<Integer, Boolean> previousKeys = new HashMap<>();

    public static void init(long windowHandle){
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if(action == GLFW_PRESS){
                currentKeys.put(key, true);
            }else if (action == GLFW_RELEASE){
                currentKeys.put(key, false);
            }
        });
    }

    public static void endFrame(){
        previousKeys.clear();
        previousKeys.putAll(currentKeys);
    }

    public static boolean isKeyDown(int key){
        return currentKeys.getOrDefault(key, false);
    }

    public static boolean isKeyPressed(int key){
        return isKeyDown(key) && !previousKeys.getOrDefault(key, false);
    }

    public static boolean isKeyReleased(int key){
        return !isKeyDown(key) && previousKeys.getOrDefault(key, false);
    }
}
