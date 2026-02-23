package editor;

import org.joml.Vector3f;

public class GizmoState {
    public static boolean dragging = false;
    public static Vector3f dragOffset = new Vector3f();
    public enum Axis{
        NONE, X, Y, Z
    }
    public static Axis activeAxis = Axis.NONE;
    public static Axis hoverAxis = Axis.NONE;
    public static Vector3f dragPlaneNormal = new Vector3f();
    public static Vector3f dragPlanePoint  = new Vector3f();
}
