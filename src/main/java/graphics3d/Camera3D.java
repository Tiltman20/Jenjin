package graphics3d;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera3D {
    private final Vector3f position =  new Vector3f(0f, 0f, 3f);


    private final Vector3f front = new Vector3f(0f, 0f, -1f);
    private final Vector3f up = new Vector3f(0f, 1f, 0f);
    private final Vector3f right = new Vector3f(1f, 0f, 0f);

    private float yaw = -90f;
    private float pitch = 0f;

    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f projectionMatrix = new Matrix4f();

    public Camera3D(float fovDeg, float aspect, float near, float far){
        projectionMatrix.setPerspective((float) Math.toRadians(fovDeg), aspect, near, far);
        updateVectors();
    }

    public Matrix4f getViewMatrix(){
        Vector3f center = new Vector3f(position).add(front);
        viewMatrix.identity().lookAt(position, center, up);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    public Vector3f getPosition(){
        return position;
    }

    private void updateVectors(){
        front.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.normalize();

        right.set(front).cross(0f, 1f, 0f).normalize();
        up.set(right).cross(front).normalize();
    }



    public void moveForward(float amount) {
        position.add(new Vector3f(front).mul(amount));
    }

    public void moveBackward(float amount) {
        position.sub(new Vector3f(front).mul(amount));
    }

    public void moveRight(float amount) {
        position.add(new Vector3f(right).mul(amount));
    }

    public void moveLeft(float amount) {
        position.sub(new Vector3f(right).mul(amount));
    }

    public void moveUp(float amount) {
        position.add(new Vector3f(up).mul(amount));
    }

    public void moveDown(float amount) {
        position.sub(new Vector3f(up).mul(amount));
    }

    public void rotate(float yawOffset, float pitchOffset) {
        yaw += yawOffset;
        pitch += pitchOffset;

        pitch = Math.max(-89f, Math.min(89f, pitch));
        updateVectors();
    }


}
