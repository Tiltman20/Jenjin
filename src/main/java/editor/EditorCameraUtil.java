package editor;

import graphics3d.Camera3D;
import editor.GizmoState.Axis;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Matrix4f;

public class EditorCameraUtil {
    public static Vector3f screenToWorldRay(
            float mouseX, float mouseY,
            int width, int height,
            Camera3D cam
    ){
        float x = (2.0f * mouseX) / width -1f;
        float y = 1.0f - (2.0f * mouseY) / height;

        Vector4f rayClip = new Vector4f(x, y, -1f, 1f);

        Matrix4f invProj = new Matrix4f(cam.getProjectionMatrix()).invert();
        Vector4f rayEye = invProj.transform(rayClip);

        rayEye.z = -1;
        rayEye.w = 0f;

        Matrix4f invView = new Matrix4f(cam.getViewMatrix()).invert();
        Vector4f rayWorld4 = invView.transform(rayEye);

        Vector3f rayWorld = new Vector3f(rayWorld4.x, rayWorld4.y, rayWorld4.z);
        rayWorld.normalize();

        return rayWorld;
    }

    public static Vector3f rayPlaneIntersection(
            Vector3f rayOrigin,
            Vector3f rayDir,
            float planeZ
    ){
        float t = (planeZ - rayOrigin.z) / rayDir.z;
        return new Vector3f(rayOrigin).add(new Vector3f(rayDir).mul(t));
    }

    public static float distanceRayToLine(
            Vector3f rayOrigin,
            Vector3f rayDir,
            Vector3f linePoint,
            Vector3f lineDir
    ){
        Vector3f w0 = new Vector3f(rayOrigin).sub(linePoint);

        float a = rayDir.dot(rayDir);
        float b = rayDir.dot(lineDir);
        float c = lineDir.dot(lineDir);
        float d = rayDir.dot(w0);
        float e = lineDir.dot(w0);

        float denom = a*c - b*b;
        if(Math.abs(denom) < 0.0001f) return Float.MAX_VALUE;

        float sc = (b*e - c*d) / denom;

        Vector3f pRay = new Vector3f(rayOrigin).add(new Vector3f(rayDir).mul(sc));

        float tc = (a*e - b*d) / denom;
        Vector3f pLine = new Vector3f(linePoint).add(new Vector3f(lineDir).mul(tc));

        return pRay.distance(pLine);
    }

    public static Vector3f rayAxisPlaneIntersection(
            Vector3f rayOrigin,
            Vector3f rayDir,
            Vector3f gizmoPos,
            Axis axis
    ){
        float t;

        switch(axis){
            case X -> {
                t = (gizmoPos.x - rayOrigin.x) / rayDir.x;
            }
            case Y -> {
                t = (gizmoPos.y - rayOrigin.y) / rayDir.y;
            }
            case Z -> {
                t = (gizmoPos.z - rayOrigin.z) / rayDir.z;
            }
            default -> {
                return new Vector3f(gizmoPos);
            }
        }
        return new Vector3f(rayOrigin).add(new Vector3f(rayDir).mul(t));
    }

    public static Vector3f closestPointRayToLine(
            Vector3f rayOrigin,
            Vector3f rayDir,
            Vector3f linePoint,
            Vector3f lineDir
    ){
        Vector3f w0 = new Vector3f(rayOrigin).sub(linePoint);

        float a = rayDir.dot(rayDir);
        float b = rayDir.dot(lineDir);
        float c = lineDir.dot(lineDir);
        float d = rayDir.dot(w0);
        float e = lineDir.dot(w0);

        float denom = a*c - b*b;

        if(Math.abs(denom) < 0.0001f)
            return new Vector3f(linePoint); // fallback

        float tc = (a*e - b*d) / denom;

        return new Vector3f(linePoint).add(new Vector3f(lineDir).mul(tc));
    }
}
