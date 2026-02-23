package editor;

import graphics3d.Camera3D;
import org.joml.Vector3f;

public class GizmoMath {

    private static final float GIZMO_SCREEN_SCALE = 0.1f;
    private static final float PICK_PIXEL_RADIUS  = 0.1f;

    public static float getGizmoScale(Camera3D cam, Vector3f gizmoPos){
        float distance = cam.getPosition().distance(gizmoPos);
        return distance * GIZMO_SCREEN_SCALE;
    }

    public static float getPickThreshold(Camera3D cam, Vector3f gizmoPos){
        return getGizmoScale(cam, gizmoPos) * PICK_PIXEL_RADIUS;
    }
}
